package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.construct.EntityDreadArmy;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellConstruct;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class DreadArmy extends SpellConstruct<EntityDreadArmy> {

	public static final String MINION_SPAWN_INTERVAL = "minion_spawn_interval";

	public DreadArmy() {
		super(IFSpellPack.MODID, "dread_army", SpellActions.POINT_DOWN, EntityDreadArmy::new, true);
		this.addProperties(SpellMinion.MINION_COUNT, SpellMinion.MINION_LIFETIME, SpellMinion.SUMMON_RADIUS, MINION_SPAWN_INTERVAL, SpellMinionIFSP.STAT_SCALE);
		this.overlap(true);
		this.floor(true);
	}

	@Override
	protected void addConstructExtras(EntityDreadArmy construct, EnumFacing side, @Nullable EntityLivingBase caster, SpellModifiers modifiers) {
		super.addConstructExtras(construct, side, caster, modifiers);
		modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * this.getProperty(SpellMinionIFSP.STAT_SCALE).floatValue(), true);
		construct.setModifiers(modifiers);
	}
}
