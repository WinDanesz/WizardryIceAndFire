package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.item.ItemCharmLoversHeart;
import com.windanesz.ifspellpack.potion.PotionAllure;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellBuff;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Allure extends SpellRay {

	public Allure() {
		super(IFSpellPack.MODID, "allure", SpellActions.POINT, false);
		this.addProperties(EFFECT_DURATION);
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return false;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (EntityUtils.isLiving(target) && caster != null) {
			EntityLivingBase entity = (EntityLivingBase)target;
			if (!EntitySiren.isWearingEarplugs(entity)) {
				int duration = (int)(this.getProperty(EFFECT_DURATION).intValue() * modifiers.get(WizardryItems.duration_upgrade));
				int amplifier = ItemCharmLoversHeart.getAmplifier(caster, modifiers);
				entity.getEntityData().setUniqueId(PotionAllure.UUID_KEY, caster.getUniqueID());
				entity.addPotionEffect(new PotionEffect(IFSPPotions.ALLURE, duration, amplifier));
			} else {
				if(!world.isRemote && caster instanceof EntityPlayer) {
					((EntityPlayer) caster).sendStatusMessage(new TextComponentTranslation("spell.resist", target.getName(), this.getNameForTranslationFormatted()), true);
				}
			}
		}
		return true;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return true;
	}

	@Override
	protected void spawnParticleRay(World world, Vec3d origin, Vec3d direction, EntityLivingBase caster, double distance) {
		float radius = -0.9F;
		float angle = (0.01745329251F * caster.renderYawOffset) - 3F;
		double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
		double extraY = 1.2F;
		double extraZ = radius * MathHelper.cos(angle);
		IceAndFire.PROXY.spawnParticle("siren_music", caster.posX + extraX + world.rand.nextFloat() - 0.5, caster.posY + extraY + world.rand.nextFloat() - 0.5, caster.posZ + extraZ + world.rand.nextFloat() - 0.5, 0, 0, 0);
	}
}
