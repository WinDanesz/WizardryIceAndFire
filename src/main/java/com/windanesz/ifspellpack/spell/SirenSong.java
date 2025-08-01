package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.item.ItemCharmLoversHeart;
import com.windanesz.ifspellpack.potion.PotionAllure;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellAreaEffect;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SirenSong extends SpellAreaEffect {


	public SirenSong() {
		super(IFSpellPack.MODID, "siren_song", SpellActions.POINT_UP, false);
		this.addProperties(EFFECT_DURATION);
		this.alwaysSucceed(true);
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return false;
	}

	@Override
	protected boolean affectEntity(World world, Vec3d origin, EntityLivingBase caster, EntityLivingBase target, int targetCount, int ticksInUse, SpellModifiers modifiers) {
		if (!EntitySiren.isWearingEarplugs(target)) {
			int duration = (int)(this.getProperty(EFFECT_DURATION).intValue() * modifiers.get(WizardryItems.duration_upgrade));
			int amplifier = ItemCharmLoversHeart.getAmplifier(caster, modifiers);
			target.getEntityData().setUniqueId(PotionAllure.UUID_KEY, caster.getUniqueID());
			target.addPotionEffect(new PotionEffect(IFSPPotions.ALLURE, duration, amplifier));
		}
		return true;
	}

	@Override
	protected void spawnParticleEffect(World world, Vec3d origin, double radius, EntityLivingBase caster, SpellModifiers modifiers) {
		if (caster != null) {
			float f = -0.9F;
			float angle = (0.01745329251F * caster.renderYawOffset) - 3F;
			double extraX = f * MathHelper.sin((float) (Math.PI + angle));
			double extraY = 1.2F;
			double extraZ = f * MathHelper.cos(angle);
			IceAndFire.PROXY.spawnParticle("siren_music", caster.posX + extraX + world.rand.nextFloat() - 0.5, caster.posY + extraY + world.rand.nextFloat() - 0.5, caster.posZ + extraZ + world.rand.nextFloat() - 0.5, 0, 0, 0);
		}
	}
}
