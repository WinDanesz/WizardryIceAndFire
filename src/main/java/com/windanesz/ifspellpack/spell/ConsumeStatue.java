package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ConsumeStatue extends SpellRay {

	public static final String TICK_RATE = "tick_rate";

	public ConsumeStatue() {
		super(IFSpellPack.MODID, "consume_statue", SpellActions.POINT, true);
		this.addProperties(HEALTH, TICK_RATE);
	}

	@Override
	protected SoundEvent[] createSounds(){
		return this.createContinuousSpellSounds();
	}

	@Override
	protected void playSound(World world, EntityLivingBase entity, int ticksInUse, int duration, SpellModifiers modifiers, String... sounds){
		this.playSoundLoop(world, entity, ticksInUse);
	}

	@Override
	protected void playSound(World world, double x, double y, double z, int ticksInUse, int duration, SpellModifiers modifiers, String... sounds){
		this.playSoundLoop(world, x, y, z, ticksInUse, duration);
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return false;
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return false;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (caster instanceof EntityPlayer) {
			float heal = this.getProperty(HEALTH).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			if (target instanceof EntityStoneStatue) {
				if (ticksInUse % this.getProperty(TICK_RATE).intValue() == 0) {
					EntityStoneStatue statue = (EntityStoneStatue) target;
					statue.setCrackAmount(statue.getCrackAmount() + 1);
					caster.heal(heal);
					statue.playSound(SoundEvents.BLOCK_STONE_HIT, 1, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 0.5F);
					if (statue.getCrackAmount() > 9) {
						statue.playSound(SoundEvents.BLOCK_STONE_BREAK, 1, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 0.5F);
						statue.setDead();
						if (!world.isRemote) {
							statue.dropItem(Item.getItemFromBlock(Blocks.COBBLESTONE), 2 + world.rand.nextInt(4));
						}
					}
				}
				if (world.isRemote) {
					ParticleBuilder.create(ParticleBuilder.Type.BEAM).entity(caster)
							.pos(origin.subtract(caster.getPositionVector())).target(target)
							.clr(0xFFFF55).spawn(world);
				}
				return true;
			} else if (target instanceof EntityLiving) {
				EntityLiving entityLiving = (EntityLiving)target;
				StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entityLiving, StoneEntityProperties.class);
				if (properties.isStone) {
					if (ticksInUse % this.getProperty(TICK_RATE).intValue() == 0) {
						properties.breakLvl++;
						caster.heal(heal);
						entityLiving.playSound(SoundEvents.BLOCK_STONE_HIT, 1, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 0.5F);
						if (properties.breakLvl > 9) {
							entityLiving.playSound(SoundEvents.BLOCK_STONE_BREAK, 1, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 0.5F);
							entityLiving.setDead();
							if (!world.isRemote) {
								entityLiving.dropItem(Item.getItemFromBlock(Blocks.COBBLESTONE), 2 + world.rand.nextInt(4));
							}
						}
					}
					if (world.isRemote) {
						ParticleBuilder.create(ParticleBuilder.Type.BEAM).entity(caster)
								.pos(origin.subtract(caster.getPositionVector())).target(target)
								.clr(0xFFFF55).spawn(world);
					}
					return true;
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
