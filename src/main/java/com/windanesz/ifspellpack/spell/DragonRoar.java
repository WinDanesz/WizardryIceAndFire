package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellAreaEffect;
import electroblob.wizardry.spell.SpellBuff;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DragonRoar extends SpellAreaEffect {

	public static final String STAT_SCALE = "stat_scale";

	public DragonRoar() {
		super(IFSpellPack.MODID, "dragon_roar", SpellActions.POINT_UP, false);
		this.addProperties(EFFECT_DURATION, EFFECT_STRENGTH, STAT_SCALE);
	}

	@Override
	protected boolean findAndAffectEntities(World world, Vec3d origin, @Nullable EntityLivingBase caster, int ticksInUse, SpellModifiers modifiers) {
		float statScale = this.getProperty(STAT_SCALE).floatValue();
		float potency = modifiers.get(SpellModifiers.POTENCY) * statScale;
		this.volume = potency;
		double radius = this.getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(WizardryItems.blast_upgrade) * potency;
		int duration = (int)(this.getProperty(EFFECT_DURATION).floatValue() * modifiers.get(WizardryItems.duration_upgrade) *potency);
		int amplifier = (int)(SpellBuff.getStandardBonusAmplifier(modifiers.get(SpellModifiers.POTENCY)) * statScale);
		List<EntityLivingBase> targets = EntityUtils.getLivingWithinRadius(radius, origin.x, origin.y, origin.z, world);
		for (EntityLivingBase target : targets) {
			if (AllyDesignationSystem.isValidTarget(caster, target) && target.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() != IafItemRegistry.earplugs) {
				target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, duration, amplifier));
			} else if (AllyDesignationSystem.isAllied(caster, target) || target == caster) {
				target.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, duration, amplifier));
			}
		}
		return true;
	}

	@Override
	protected boolean affectEntity(World world, Vec3d origin, @Nullable EntityLivingBase caster, EntityLivingBase target, int targetCount, int ticksInUse, SpellModifiers modifiers) {
		return true;
	}

}
