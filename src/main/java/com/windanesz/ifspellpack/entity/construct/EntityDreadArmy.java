package com.windanesz.ifspellpack.entity.construct;

import baubles.api.BaublesApi;
import com.github.alexthe666.iceandfire.entity.*;
import com.windanesz.ifspellpack.entity.living.EntityDreadBeastMinion;
import com.windanesz.ifspellpack.entity.living.EntityDreadGhoulMinion;
import com.windanesz.ifspellpack.entity.living.EntityDreadScuttlerMinion;
import com.windanesz.ifspellpack.entity.living.EntityDreadThrallMinion;
import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import com.windanesz.ifspellpack.registry.IFSPItems;
import com.windanesz.ifspellpack.registry.IFSPSpells;
import com.windanesz.ifspellpack.spell.DreadArmy;
import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.NBTExtras;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityDreadArmy extends EntityMagicConstruct {

	public static final String SPAWN_TIMER_KEY = "SpawnTimer";
	public static final String MINIONS_SUMMONED_KEY = "MinionsSummoned";
	public static final String MODIFIERS_KEY = "Modifiers";

	private int spawnTimer = 10;
	private int minionsSummoned = 0;
	private SpellModifiers modifiers = new SpellModifiers();

	public EntityDreadArmy(World world) {
		super(world);
		this.setSize(0, 0);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.minionsSummoned < IFSPSpells.DREAD_ARMY.getProperty(SpellMinion.MINION_COUNT).intValue()) {
			if (this.spawnTimer-- == 0) {
				if (!this.world.isRemote) {
					int range = IFSPSpells.DREAD_ARMY.getProperty(SpellMinion.SUMMON_RADIUS).intValue();
					BlockPos pos = BlockUtils.findNearbyFloorSpace(this, range, range * 2);
					if (pos != null) {
						EntityLivingBase caster = this.getCaster();
						EntityDreadMob minion = this.getRandomNewMinion();
						if (minion instanceof EntityDreadThrallMinion && caster instanceof EntityPlayer) {
							EntityPlayer player = (EntityPlayer) caster;
							if (ItemArtefact.isArtefactActive(player, IFSPItems.HEAD_DREAD_CROWN)) {
								if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(4))) {
									((EntityDreadThrallMinion) minion).setEquipment(true);
								}
							}
						}
						minion.setPosition(pos.getX(), pos.getY(), pos.getZ());
						((ISummonedCreature) minion).setCaster(this.getCaster());
						((ISummonedCreature) minion).setLifetime((int) (IFSPSpells.DREAD_ARMY.getProperty(SpellMinion.MINION_LIFETIME).intValue() * this.modifiers.get(WizardryItems.duration_upgrade)));
						IAttributeInstance attribute = minion.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
						attribute.applyModifier(new AttributeModifier(SpellMinion.POTENCY_ATTRIBUTE_MODIFIER, damageMultiplier - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
						minion.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(SpellMinion.HEALTH_MODIFIER, this.modifiers.get(SpellMinion.HEALTH_MODIFIER) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
						minion.setHealth(minion.getMaxHealth());
						minion.onInitialSpawn(minion.world.getDifficultyForLocation(pos), null);
						world.spawnEntity(minion);
						this.minionsSummoned++;
						this.spawnTimer += IFSPSpells.DREAD_ARMY.getProperty(DreadArmy.MINION_SPAWN_INTERVAL).intValue() + this.rand.nextInt(10);
					}
				}
			}
		} else {
			this.despawn();
		}
	}

	public <T extends EntityDreadMob & ISummonedCreature> T getRandomNewMinion() {
		EntityDreadMob mob;
		float chance = this.rand.nextFloat();
		chance /= this.damageMultiplier;
		if (chance > 0.35F) {
			mob = new EntityDreadThrallMinion(this.world);
		} else if (chance > 0.20F) {
			mob = new EntityDreadGhoulMinion(this.world);
		} else if (chance > 0.10F) {
			mob = new EntityDreadBeastMinion(this.world);
		} else {
			mob = new EntityDreadScuttlerMinion(this.world);
		}
		return (T)mob;
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		return EnumActionResult.PASS;
	}

	public SpellModifiers getModifiers() {
		return this.modifiers;
	}

	public void setModifiers(SpellModifiers modifiers) {
		this.modifiers = modifiers;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger(SPAWN_TIMER_KEY, this.spawnTimer);
		nbttagcompound.setInteger(MINIONS_SUMMONED_KEY, this.minionsSummoned);
		NBTExtras.storeTagSafely(nbttagcompound, MODIFIERS_KEY, this.modifiers.toNBT());
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.spawnTimer = nbttagcompound.getInteger(SPAWN_TIMER_KEY);
		this.minionsSummoned = nbttagcompound.getInteger(MINIONS_SUMMONED_KEY);;
		this.modifiers = SpellModifiers.fromNBT(nbttagcompound);
	}
}
