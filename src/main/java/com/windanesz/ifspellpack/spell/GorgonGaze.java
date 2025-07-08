package com.windanesz.ifspellpack.spell;

import baubles.api.BaublesApi;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.message.MessageStoneStatue;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.util.IsImmune;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.List;

public class GorgonGaze extends Spell {

	public static final String CHECK_GAZE_DEATH_KEY = "ifspellpack:check_gaze_death";
	public static final String VIEW_RADIUS = "view_radius";
	public static final String MASK_BONUS_DAMAGE = "mask_bonus_damage";

	public GorgonGaze() {
		super(IFSpellPack.MODID, "gorgon_gaze", SpellActions.POINT, false);
		this.addProperties(DAMAGE, RANGE, VIEW_RADIUS, MASK_BONUS_DAMAGE);
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return true;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		boolean success = false;
		if (!EntityGorgon.isBlindfolded(caster)) {
			boolean artefactActive = ItemArtefact.isArtefactActive(caster, IFSPItems.HEAD_GORGON_MASK);
			float damage = this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			if (artefactActive && (ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(caster).getStackInSlot(4)) || caster.isCreative())) {
				if (this.getProperty(MASK_BONUS_DAMAGE).floatValue() >= 0) {
					damage += this.getProperty(MASK_BONUS_DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
				} else {
					damage = Float.MAX_VALUE;
				}
			}
			double range = this.getProperty(RANGE).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
			float view_radius = this.getProperty(VIEW_RADIUS).floatValue();
			List<EntityLivingBase> targets = EntityUtils.getLivingWithinRadius(range, caster.posX, caster.posY, caster.posZ, world);
			targets.removeIf(e -> e == caster || !EntityGorgon.isEntityLookingAt(caster, e, view_radius) || !EntityGorgon.isEntityLookingAt(e, caster, view_radius) || !EntityUtils.isLiving(e) || EntityGorgon.isBlindfolded(e) || !AllyDesignationSystem.isValidTarget(caster, e));
			if (!targets.isEmpty()) {
				this.playSound(world, caster, ticksInUse, -1, modifiers);
				success = true;
			}
			for (EntityLivingBase target : targets) {
				if (target instanceof EntityPlayer) {
					if (!world.isRemote) {
						target.attackEntityFrom(IceAndFire.gorgon, damage);
						if (!target.isEntityAlive()) {
							target.playSound(IafSoundRegistry.GORGON_TURN_STONE, 1, 1);
							EntityStoneStatue statue = new EntityStoneStatue(world);
							statue.setPositionAndRotation(target.posX, target.posY, target.posZ, target.rotationYaw, target.rotationPitch);
							statue.smallArms = true;
							world.spawnEntity(statue);
							statue.prevRotationYaw = target.rotationYaw;
							statue.rotationYaw = target.rotationYaw;
							statue.rotationYawHead = target.rotationYaw;
							statue.renderYawOffset = target.rotationYaw;
							statue.prevRenderYawOffset = target.rotationYaw;

						}
					}
				} else {
					target.getEntityData().setBoolean(CHECK_GAZE_DEATH_KEY, false);
					target.attackEntityFrom(IceAndFire.gorgon, damage);
					if (target.getEntityData().getBoolean(CHECK_GAZE_DEATH_KEY)) {
						if (target instanceof EntityLiving && (!(target instanceof IBlacklistedFromStatues) || !IsImmune.toStone(target) && ((IBlacklistedFromStatues) target).canBeTurnedToStone())) {
							StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(target, StoneEntityProperties.class);
							EntityLiving attackTarget = (EntityLiving) target;
							if (properties != null && !properties.isStone) {
								properties.isStone = true;
								target.playSound(IafSoundRegistry.GORGON_TURN_STONE, 1, 1);
								if (world.isRemote) {
									IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageStoneStatue(attackTarget.getEntityId(), true));
								} else {
									IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageStoneStatue(attackTarget.getEntityId(), true));
								}

							}
							if (attackTarget instanceof EntityDragonBase) {
								EntityDragonBase dragon = (EntityDragonBase) attackTarget;
								dragon.setFlying(false);
								dragon.setHovering(false);
							}
							if (attackTarget instanceof EntityHippogryph) {
								EntityHippogryph dragon = (EntityHippogryph) attackTarget;
								dragon.setFlying(false);
								dragon.setHovering(false);
								dragon.airTarget = null;
							}
						}
					}
				}
			}
		}
		return success;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		boolean success = false;
		if (!EntityGorgon.isBlindfolded(caster)) {
			float damage = this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			double range = this.getProperty(RANGE).doubleValue() * modifiers.get(WizardryItems.range_upgrade);
			float view_radius = this.getProperty(VIEW_RADIUS).floatValue();
			List<EntityLivingBase> targets = EntityUtils.getLivingWithinRadius(range, caster.posX, caster.posY, caster.posZ, world);
			targets.removeIf(e -> e == caster || !EntityGorgon.isEntityLookingAt(caster, e, view_radius) || !EntityGorgon.isEntityLookingAt(e, caster, view_radius) || !EntityUtils.isLiving(e) || EntityGorgon.isBlindfolded(e) || !AllyDesignationSystem.isValidTarget(caster, e));
			if (!targets.isEmpty()) {
				this.playSound(world, caster, ticksInUse, -1, modifiers);
				success = true;
			}
			for (EntityLivingBase entityLivingBase : targets) {
				if (entityLivingBase instanceof EntityPlayer) {
					if (!world.isRemote) {
						entityLivingBase.attackEntityFrom(IceAndFire.gorgon, damage);
						if (!entityLivingBase.isEntityAlive()) {
							target.playSound(IafSoundRegistry.GORGON_TURN_STONE, 1, 1);
							EntityStoneStatue statue = new EntityStoneStatue(world);
							statue.setPositionAndRotation(entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, entityLivingBase.rotationYaw, entityLivingBase.rotationPitch);
							statue.smallArms = true;
							world.spawnEntity(statue);
							statue.prevRotationYaw = entityLivingBase.rotationYaw;
							statue.rotationYaw = entityLivingBase.rotationYaw;
							statue.rotationYawHead = entityLivingBase.rotationYaw;
							statue.renderYawOffset = entityLivingBase.rotationYaw;
							statue.prevRenderYawOffset = entityLivingBase.rotationYaw;
						}
					}
				} else {
					target.getEntityData().setBoolean(CHECK_GAZE_DEATH_KEY, false);
					target.attackEntityFrom(IceAndFire.gorgon, damage);
					if (target.getEntityData().getBoolean(CHECK_GAZE_DEATH_KEY)) {
						if (target instanceof EntityLiving && (!(target instanceof IBlacklistedFromStatues) || !IsImmune.toStone(target) && ((IBlacklistedFromStatues) target).canBeTurnedToStone())) {
							StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(target, StoneEntityProperties.class);
							EntityLiving attackTarget = (EntityLiving) target;
							if (properties != null && !properties.isStone) {
								properties.isStone = true;
								target.playSound(IafSoundRegistry.GORGON_TURN_STONE, 1, 1);
								if (world.isRemote) {
									IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageStoneStatue(attackTarget.getEntityId(), true));
								} else {
									IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageStoneStatue(attackTarget.getEntityId(), true));
								}
							}
							if (attackTarget instanceof EntityDragonBase) {
								EntityDragonBase dragon = (EntityDragonBase) attackTarget;
								dragon.setFlying(false);
								dragon.setHovering(false);
							}
							if (attackTarget instanceof EntityHippogryph) {
								EntityHippogryph dragon = (EntityHippogryph) attackTarget;
								dragon.setFlying(false);
								dragon.setHovering(false);
								dragon.airTarget = null;
							}
						}
					}
				}
			}
		}
		return success;
	}
}
