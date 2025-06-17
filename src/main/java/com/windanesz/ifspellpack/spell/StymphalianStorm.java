package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.entity.EntityStymphalianFeather;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.accessor.AccessorEntityStymphalianFeather;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class StymphalianStorm extends Spell {

	public static final String DAMAGE_SCALE = "damage_scale";
	public static final String VELOCITY = "velocity";

	public StymphalianStorm() {
		super(IFSpellPack.MODID, "stymphalian_storm", SpellActions.SUMMON, false);
		this.addProperties(DAMAGE_SCALE, BLAST_RADIUS, VELOCITY);
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return true;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		float rotation = caster.rotationYawHead;
		int featherCount = (int)(this.getProperty(BLAST_RADIUS).floatValue() * modifiers.get(WizardryItems.blast_upgrade));
		float damage_multiplier = this.getProperty(DAMAGE_SCALE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		float velocity = this.getProperty(VELOCITY).floatValue() * modifiers.get(WizardryItems.range_upgrade);
		float rotate = 360f / featherCount;
		for (int i = 0; i < featherCount; i++) {
			EntityStymphalianFeather feather = new EntityStymphalianFeather(world, caster);
			((AccessorEntityStymphalianFeather)feather).ifspellpack$setDroppable(false);
			feather.setDamage(feather.getDamage() * damage_multiplier);
			feather.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
			feather.shoot(caster, 0, rotation, 0.0F, velocity, 1.0F);
			rotation += rotate;
			if (!world.isRemote) {
				world.spawnEntity(feather);
			}
		}
		//temporarily here until I find this sound to use in sounds.json
		caster.playSound(SoundEvents.ENTITY_EGG_THROW, 1, 1);
		//this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		float rotation = caster.rotationYawHead;
		int featherCount = (int)(this.getProperty(BLAST_RADIUS).floatValue() * modifiers.get(WizardryItems.blast_upgrade));
		float damage_multiplier = this.getProperty(DAMAGE_SCALE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		float velocity = this.getProperty(VELOCITY).floatValue() * modifiers.get(WizardryItems.range_upgrade);
		float rotate = 360f / featherCount;
		for (int i = 0; i < featherCount; i++) {
			EntityStymphalianFeather feather = new EntityStymphalianFeather(world, caster);
			((AccessorEntityStymphalianFeather)feather).ifspellpack$setDroppable(false);
			feather.setDamage(feather.getDamage() * damage_multiplier);
			feather.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
			feather.shoot(caster, 0, rotation, 0.0F, velocity, 1.0F);
			rotation += rotate;
			if (!world.isRemote) {
				world.spawnEntity(feather);
			}
		}
		caster.playSound(SoundEvents.ENTITY_EGG_THROW, 1, 1);
		//this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}
}
