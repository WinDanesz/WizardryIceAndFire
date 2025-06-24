package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.living.EntityDreadKnightMinion;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class SummonDreadKnight extends SpellMinion<EntityDreadKnightMinion> {

	public static final String STAT_SCALE = "stat_scale";

	public SummonDreadKnight() {
		super(IFSpellPack.MODID, "summon_dread_knight", EntityDreadKnightMinion::new);
		this.addProperties(STAT_SCALE);
	}

	@Override
	protected void addMinionExtras(EntityDreadKnightMinion minion, BlockPos pos, @Nullable EntityLivingBase caster, SpellModifiers modifiers, int alreadySpawned) {
		float statScale = this.getProperty(STAT_SCALE).floatValue();
		modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * statScale, false);
		//needed for the spawn animation
		minion.onInitialSpawn(minion.world.getDifficultyForLocation(new BlockPos(minion)), null);
	}
}
