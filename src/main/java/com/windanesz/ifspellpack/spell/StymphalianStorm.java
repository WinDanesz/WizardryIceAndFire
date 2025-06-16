package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.entity.EntityStymphalianFeather;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.accessor.AccessorEntityStymphalianFeather;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class StymphalianStorm extends Spell {

	public StymphalianStorm() {
		super(IFSpellPack.MODID, "stymphalian_storm", SpellActions.SUMMON, false);
		this.addProperties(DAMAGE, BLAST_RADIUS, RANGE);
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return true;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		float rotation = caster.rotationYawHead;
		int featherCount = (int)(this.getProperty(BLAST_RADIUS).intValue() * modifiers.get(BLAST_RADIUS));
		float damage = this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		float range = this.getProperty(RANGE).floatValue() * modifiers.get(BLAST_RADIUS);
		float rotate = 360f / featherCount;
		for (int i = 0; i < featherCount; i++) {
			EntityStymphalianFeather feather = new EntityStymphalianFeather(world, caster);
			((AccessorEntityStymphalianFeather)feather).ifspellpack$setDroppable(false);
			feather.setDamage(damage);
			feather.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
			feather.shoot(caster, 0, rotation, 0.0F, range, 1.0F);
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
		int featherCount = (int)(this.getProperty(BLAST_RADIUS).intValue() * modifiers.get(BLAST_RADIUS));
		float damage = this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		float range = this.getProperty(RANGE).floatValue() * modifiers.get(BLAST_RADIUS);
		float rotate = 360f/featherCount;
		for (int i = 0; i < featherCount; i++) {
			EntityStymphalianFeather feather = new EntityStymphalianFeather(world, caster);
			((AccessorEntityStymphalianFeather)feather).ifspellpack$setDroppable(false);
			feather.setDamage(damage);
			feather.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
			feather.shoot(caster, 0, rotation, 0.0F, range, 1.0F);
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
