package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.entity.ChainEntityProperties;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.accessor.AccessorChainEntityProperties;
import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Shackles extends SpellRay {

	public static final String PULL_STRENGTH = "pull_strength";

	public Shackles() {
		super(IFSpellPack.MODID, "shackles", SpellActions.POINT, true);
		this.addProperties(PULL_STRENGTH);
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return false;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (caster != null) {
			if (ticksInUse <= 5) {
				if (ticksInUse == 0) {
					if (caster.isSneaking()) {
						List<Entity> entityList = EntityUtils.getEntitiesWithinRadius(10, caster.posX, caster.posY, caster.posZ, world, Entity.class);
						entityList.remove(caster);
						for (Entity entity : entityList) {
							ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(entity, ChainEntityProperties.class);
							if (chainProperties != null && chainProperties.isConnectedToEntity(entity, caster)) {
								chainProperties.removeChain(entity, caster);
								this.playSound(world, caster, ticksInUse, -1, modifiers);
								if (((AccessorChainEntityProperties) chainProperties).ifspellpack$getDropsChain()) {
									EntityItem entityitem = new EntityItem(caster.world, caster.posX, caster.posY + (double) 1, caster.posZ, new ItemStack(IafItemRegistry.chain));
									entityitem.setDefaultPickupDelay();
									if (!world.isRemote) {
										caster.world.spawnEntity(entityitem);
									}
								}
							}
						}
						return false;
					} else {
						return super.cast(world, caster, hand, ticksInUse, modifiers);
					}
				}
				if (ticksInUse == 5 && ItemArtefact.isArtefactActive(caster, IFSPItems.CHARM_DWARVEN_GEARBOX)) {
					double pullStrength = this.getProperty(PULL_STRENGTH).doubleValue();
					for (Entity entity : EntityUtils.getEntitiesWithinRadius(10, caster.posX, caster.posY, caster.posZ, world, Entity.class)) {
						ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(entity, ChainEntityProperties.class);
						if (chainProperties != null && chainProperties.isConnectedToEntity(entity, caster)) {
							this.playSound(world, caster, ticksInUse, -1, modifiers);
							entity.motionX = (caster.posX - entity.posX) * pullStrength;
							entity.motionY = (caster.posY - entity.posY) * pullStrength;
							entity.motionZ = (caster.posZ - entity.posZ) * pullStrength;
						}
					}
					return true;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (ticksInUse == 0) {
			return super.cast(world, caster, hand, ticksInUse, target, modifiers);
		}
		return false;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(target, ChainEntityProperties.class);
		if (chainProperties != null) {
			if (!chainProperties.isConnectedToEntity(target, caster)) {
				chainProperties.addChain(target, caster);
				((AccessorChainEntityProperties)chainProperties).ifspellpack$setDropsChain(false);
				this.playSound(world, caster, ticksInUse, -1, modifiers);
				return true;
			} else {
				chainProperties.removeChain(target, caster);
				this.playSound(world, caster, ticksInUse, -1, modifiers);
				if (((AccessorChainEntityProperties) chainProperties).ifspellpack$getDropsChain()) {
					EntityItem entityitem = new EntityItem(caster.world, caster.posX, caster.posY + (double) 1, caster.posZ, new ItemStack(IafItemRegistry.chain));
					entityitem.setDefaultPickupDelay();
					if (!world.isRemote) {
						caster.world.spawnEntity(entityitem);
					}
				}
			}
		}
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}
}
