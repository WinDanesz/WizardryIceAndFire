package com.windanesz.ifspellpack.spell;

import baubles.api.BaublesApi;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.living.EntityDreadHorseMinion;
import com.windanesz.ifspellpack.entity.living.EntityDreadThrallMinion;
import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.entity.living.EntitySpiritHorse;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Function;

public class SummonDreadHorse extends SpellMinionIFSP<EntityDreadHorseMinion> {


	public SummonDreadHorse() {
		super("summon_dread_horse", EntityDreadHorseMinion::new);
	}

	@Override
	protected void addMinionExtras(EntityDreadHorseMinion minion, BlockPos pos, @Nullable EntityLivingBase caster, SpellModifiers modifiers, int alreadySpawned) {
		if (caster instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)caster;
			if (ItemArtefact.isArtefactActive(player, IFSPItems.CHARM_DREAD_HEART)) {
				if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(6))) {
					minion.setLifetime((int)(minion.getLifetime() * IFSpellPack.settings.heartOfDreadDurationMultiplier));
				}
			}
		}
		//Speed and Jump modifiers copied from SummonSpiritHorse
		minion.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(POTENCY_ATTRIBUTE_MODIFIER, modifiers.get(SpellModifiers.POTENCY) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
		minion.getEntityAttribute(EntitySpiritHorse.JUMP_STRENGTH).applyModifier(new AttributeModifier(POTENCY_ATTRIBUTE_MODIFIER, modifiers.amplified(SpellModifiers.POTENCY, 0.25f) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
		minion.setHorseTamed(true);
		minion.setHorseSaddled(true);
		//needed for the spawn animation
		super.addMinionExtras(minion, pos, caster, modifiers, alreadySpawned);
	}
}
