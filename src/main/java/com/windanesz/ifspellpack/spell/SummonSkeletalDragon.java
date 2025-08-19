package com.windanesz.ifspellpack.spell;

import baubles.api.BaublesApi;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.living.EntitySkeletalDragon;
import com.windanesz.ifspellpack.entity.living.EntitySkeletalFireDragon;
import com.windanesz.ifspellpack.entity.living.EntitySkeletalIceDragon;
import com.windanesz.ifspellpack.entity.living.EntitySkeletalLightningDragon;
import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import com.windanesz.ifspellpack.item.ItemCharmDwarvenPocketForge;
import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class SummonSkeletalDragon extends Spell {

	public static final String MINION_LIFETIME = "minion_lifetime";
	public static final String MINION_COUNT = "minion_count";
	public static final String SUMMON_RADIUS = "summon_radius";
	public static final String HEALTH_MODIFIER = "minion_health";
	public static final String DRAGON_AGE_IN_DAYS = "dragon_age_in_days";

	public SummonSkeletalDragon() {
		super(IFSpellPack.MODID, "summon_skeletal_dragon", SpellActions.SUMMON, false);
		addProperties(MINION_LIFETIME, MINION_COUNT, SUMMON_RADIUS, DRAGON_AGE_IN_DAYS, SpellMinionIFSP.STAT_SCALE);
		this.npcSelector((e, o) -> true);
	}

	@Override
	public boolean requiresPacket() {
		return false;
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return true;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers){
		if(!this.checkSpawn(world, caster, modifiers)) return false;
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers){
		if(!this.checkSpawn(world, caster, modifiers)) return false;
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers){
		if(!world.isRemote) {
			for(int i = 0; i < getProperty(MINION_COUNT).intValue(); i++) {
				this.spawnDragon(world, null, modifiers, x, y, z);
			}
		}
		this.playSound(world, x - direction.getXOffset(), y - direction.getYOffset(), z - direction.getZOffset(), ticksInUse, duration, modifiers);

		return true;
	}

	protected boolean checkSpawn(World world, EntityLivingBase caster, SpellModifiers modifiers){
		if(!world.isRemote) {
			for(int i = 0; i < getProperty(MINION_COUNT).intValue(); i++) {
				int range = getProperty(SUMMON_RADIUS).intValue();
				BlockPos pos = BlockUtils.findNearbyFloorSpace(caster, range, range*2);
				if(pos == null) {
					return false;
				}
				this.spawnDragon(world, caster, modifiers, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}

	public void spawnDragon(World world, @Nullable EntityLivingBase caster, SpellModifiers modifiers, double x, double y, double z) {
		EntitySkeletalDragon dragon = generateDragon(world);
		dragon.setPosition(x + 0.5, y, z + 5);
		if (caster != null) {
			dragon.setCaster(caster);
		}
		dragon.setLifetime((int)(getProperty(MINION_LIFETIME).floatValue() * modifiers.get(WizardryItems.duration_upgrade)));
		dragon.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(HEALTH_MODIFIER, modifiers.get(HEALTH_MODIFIER) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
		dragon.setAgeInDays((int)(this.getProperty(DRAGON_AGE_IN_DAYS).intValue() * modifiers.get(SpellModifiers.POTENCY)));
		dragon.setGender(world.rand.nextBoolean());
		dragon.setVariant(new Random().nextInt(4));
		dragon.setSleeping(false);
		dragon.updateAttributes();
		if (caster instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)caster;
			if (ItemArtefact.isArtefactActive(player, IFSPItems.CHARM_DWARVEN_POCKET_FORGE)) {
				ItemCharmDwarvenPocketForge.armorDragon(player, dragon, BaublesApi.getBaublesHandler(player).getStackInSlot(6));
			}
		}
		dragon.setHealth(dragon.getMaxHealth());
		world.spawnEntity(dragon);
	}

	public static EntitySkeletalDragon generateDragon(World world) {
		switch (world.rand.nextInt(3)) {
			case 0: {
				return new EntitySkeletalFireDragon(world);
			}
			case 1: {
				return new EntitySkeletalIceDragon(world);
			}
			default: {
				return new EntitySkeletalLightningDragon(world);
			}
		}
	}

}
