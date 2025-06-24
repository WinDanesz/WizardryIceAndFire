package com.windanesz.ifspellpack.spell;

import baubles.api.BaublesApi;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.living.EntityDreadThrallMinion;
import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class SummonDreadThrall extends SpellMinion<EntityDreadThrallMinion> {

	public static final String STAT_SCALE = "stat_scale";

	public SummonDreadThrall() {
		super(IFSpellPack.MODID, "summon_dread_thrall", EntityDreadThrallMinion::new);
		this.addProperties(STAT_SCALE);
	}

	@Override
	protected void addMinionExtras(EntityDreadThrallMinion minion, BlockPos pos, @Nullable EntityLivingBase caster, SpellModifiers modifiers, int alreadySpawned) {
		if (caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)caster, IFSPItems.HEAD_DREAD_CROWN)) {
			if (((EntityPlayer)caster).isCreative() || ItemChargedArtefact.consumeCharge(IFSPItems.HEAD_DREAD_CROWN, BaublesApi.getBaublesHandler((EntityPlayer)caster).getStackInSlot(4))) {
				minion.setEquipment(true);
			}
		}
		float statScale = this.getProperty(STAT_SCALE).floatValue();
		modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * statScale, false);
		//needed for the spawn animation
		minion.onInitialSpawn(minion.world.getDifficultyForLocation(new BlockPos(minion)), null);
	}
}
