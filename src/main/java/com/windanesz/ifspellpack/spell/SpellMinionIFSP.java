package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.function.Function;

public class SpellMinionIFSP<T extends EntityLiving & ISummonedCreature> extends SpellMinion<T> {

	public static final String STAT_SCALE = "stat_scale";

	public SpellMinionIFSP(String name, Function<World, T> minionFactory) {
		super(IFSpellPack.MODID, name, minionFactory);
		this.addProperties(STAT_SCALE);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		float statScale = this.getProperty(STAT_SCALE).floatValue();
		modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * statScale, false);
		return super.cast(world, caster, hand, ticksInUse, modifiers);
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		float statScale = this.getProperty(STAT_SCALE).floatValue();
		modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * statScale, false);
		return super.cast(world, caster, hand, ticksInUse, target, modifiers);
	}

}
