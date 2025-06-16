package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.entity.EntityStymphalianFeather;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
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

public class StymphalianBarrage extends Spell {

	public static final String DAMAGE_SCALE = "damage_scale";

	public StymphalianBarrage() {
		super(IFSpellPack.MODID, "stymphalian_barrage", SpellActions.SUMMON, true);
		this.addProperties(DAMAGE_SCALE, RANGE);
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return true;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (ticksInUse <= 7) {
			if (ticksInUse == 0 || ticksInUse == 7) {
				float velocity = this.getProperty(RANGE).floatValue() * modifiers.get(WizardryItems.range_upgrade);
				float damage_multiplier = this.getProperty(DAMAGE_SCALE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
				for (int i = 0; i < 4; i++) {
					float wingX = (float) (caster.posX + 1.8F * 0.5F * Math.cos((caster.rotationYaw + 180 * (i % 2)) * Math.PI / 180));
					float wingZ = (float) (caster.posZ + 1.8F * 0.5F * Math.sin((caster.rotationYaw + 180 * (i % 2)) * Math.PI / 180));
					float wingY = (float) (caster.posY + 1F);
					EntityStymphalianFeather feather = new EntityStymphalianFeather(world, caster);
					feather.setPosition(wingX, wingY, wingZ);
					((AccessorEntityStymphalianFeather)feather).ifspellpack$setDroppable(false);
					feather.setDamage(feather.getDamage() * damage_multiplier);
					feather.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
					feather.shoot(caster, caster.rotationPitch, caster.rotationYaw, 0, velocity, 2);
					caster.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (world.rand.nextFloat() * 0.4F + 0.8F));
					world.spawnEntity(feather);
				}
			}
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		if (ticksInUse <= 7) {
			if (ticksInUse == 0 || ticksInUse == 7) {
				float velocity = this.getProperty(RANGE).floatValue() * modifiers.get(WizardryItems.range_upgrade);
				float damage_multiplier = this.getProperty(DAMAGE_SCALE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
				for (int i = 0; i < 4; i++) {
					float wingX = (float) (caster.posX + 1.8F * 0.5F * Math.cos((caster.rotationYaw + 180 * (i % 2)) * Math.PI / 180));
					float wingZ = (float) (caster.posZ + 1.8F * 0.5F * Math.sin((caster.rotationYaw + 180 * (i % 2)) * Math.PI / 180));
					float wingY = (float) (caster.posY + 1F);
					EntityStymphalianFeather feather = new EntityStymphalianFeather(world, caster);
					feather.setPosition(wingX, wingY, wingZ);
					((AccessorEntityStymphalianFeather)feather).ifspellpack$setDroppable(false);
					feather.setDamage(feather.getDamage() * damage_multiplier);
					feather.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
					feather.shoot(caster, caster.rotationPitch, caster.rotationYaw, 0, velocity, 2);
					caster.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (world.rand.nextFloat() * 0.4F + 0.8F));
					world.spawnEntity(feather);
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
}
