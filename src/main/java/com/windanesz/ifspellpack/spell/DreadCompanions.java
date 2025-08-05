package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.entity.living.EntityDreadBeastMinion;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class DreadCompanions extends SpellMinionIFSP<EntityDreadBeastMinion> {

	public DreadCompanions() {
		super("dread_companions", EntityDreadBeastMinion::new);
	}

	@Override
	protected void addMinionExtras(EntityDreadBeastMinion minion, BlockPos pos, @Nullable EntityLivingBase caster, SpellModifiers modifiers, int alreadySpawned) {
		super.addMinionExtras(minion, pos, caster, modifiers, alreadySpawned);
		minion.setScale(MathHelper.clamp(0.85f * modifiers.get(SpellModifiers.POTENCY), 0.85f, 1.35f));
		minion.setVariant(alreadySpawned % 2);
	}
}
