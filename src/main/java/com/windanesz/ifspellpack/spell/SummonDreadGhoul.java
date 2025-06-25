package com.windanesz.ifspellpack.spell;

import baubles.api.BaublesApi;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.living.EntityDreadGhoulMinion;
import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class SummonDreadGhoul extends SpellMinion<EntityDreadGhoulMinion> {

	public static final String STAT_SCALE = "stat_scale";

	public SummonDreadGhoul() {
		super(IFSpellPack.MODID, "summon_dread_ghoul", EntityDreadGhoulMinion::new);
		this.addProperties(STAT_SCALE);
	}

	@Override
	protected void addMinionExtras(EntityDreadGhoulMinion minion, BlockPos pos, @Nullable EntityLivingBase caster, SpellModifiers modifiers, int alreadySpawned) {
		if (caster instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)caster;
			if (ItemArtefact.isArtefactActive(player, IFSPItems.CHARM_DREAD_HEART)) {
				if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(6))) {
					minion.setLifetime((int)(minion.getLifetime() * IFSpellPack.settings.heartOfDreadDurationMultiplier));
				}
			}
		}
		float statScale = this.getProperty(STAT_SCALE).floatValue();
		modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * statScale, false);
		//needed for the spawn animation
		minion.onInitialSpawn(minion.world.getDifficultyForLocation(new BlockPos(minion)), null);
	}
}
