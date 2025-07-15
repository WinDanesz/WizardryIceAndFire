package com.windanesz.ifspellpack.spell;

import baubles.api.BaublesApi;
import com.windanesz.ifspellpack.entity.living.EntityMyrmexSentinelMinion;
import com.windanesz.ifspellpack.entity.living.EntityMyrmexWorkerMinion;
import com.windanesz.ifspellpack.item.ItemBodyReinforcedChitinousPlating;
import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SummonMyrmexWorker extends SpellMinionIFSP<EntityMyrmexWorkerMinion> {

	public static final String ARMOR_TOUGHNESS_MODIFIER = "minion_armor_toughness";

	public SummonMyrmexWorker() {
		super("summon_myrmex_worker", EntityMyrmexWorkerMinion::new);
	}

	@Override
	protected boolean spawnMinions(World world, EntityLivingBase caster, SpellModifiers modifiers) {
		boolean explosive = false;
		boolean reinforcedChitin = false;
		if (caster instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)caster;
			if (ItemArtefact.isArtefactActive(player, IFSPItems.CHARM_VOLATILE_RESIN)) {
				if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(6))) {
					explosive = true;
				}
			}
			if (ItemArtefact.isArtefactActive(player, IFSPItems.BODY_REINFORCED_CHITINOUS_PLATING)) {
				if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(5))) {
					reinforcedChitin = true;
				}
			}
		}
		if(!world.isRemote){
			for(int i=0; i<getProperty(MINION_COUNT).intValue(); i++){
				int range = getProperty(SUMMON_RADIUS).intValue();
				// Try and find a nearby floor space
				BlockPos pos = BlockUtils.findNearbyFloorSpace(caster, range, range*2);
				if(flying){
					if(pos != null){
						// Make sure the flying entity spawns above the ground
						pos = pos.up(2); // Adding 2 will suffice, it's not exactly a game-changer...
					}else{
						// If there was no floor around to spawn them on, just pick any spot in mid-air
						pos = caster.getPosition().north(world.rand.nextInt(range*2) - range).east(world.rand.nextInt(range*2) - range);
					}
				}else{
					// If there was no floor around and the entity isn't a flying one, the spell fails.
					// As per the javadoc for findNearbyFloorSpace, there's no point trying the rest of the minions.
					if(pos == null) return false;
				}
				EntityMyrmexWorkerMinion minion = createMinion(world, caster, modifiers);
				minion.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				minion.setCaster(caster);
				if (explosive) {
					minion.setExplosive(true);
				}
				// Modifier implementation
				// Attribute modifiers are pretty opaque, see https://minecraft.gamepedia.com/Attribute#Modifiers
				minion.setLifetime((int)(getProperty(MINION_LIFETIME).floatValue() * modifiers.get(WizardryItems.duration_upgrade)));
				IAttributeInstance attribute = minion.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
				// Apparently some things don't have an attack damage
				if(attribute != null) {
					attribute.applyModifier(new AttributeModifier(POTENCY_ATTRIBUTE_MODIFIER, modifiers.get(SpellModifiers.POTENCY) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
				}
				if (reinforcedChitin) {
					minion.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).applyModifier(new AttributeModifier(ARMOR_TOUGHNESS_MODIFIER, ItemBodyReinforcedChitinousPlating.INCREASED_ARMOR, EntityUtils.Operations.ADD));
				}
				// This is only used for artefacts, but it's a nice example of custom spell modifiers
				minion.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(HEALTH_MODIFIER, modifiers.get(HEALTH_MODIFIER) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
				minion.setHealth(minion.getMaxHealth()); // Need to set this because we may have just modified the value
				this.addMinionExtras(minion, pos, caster, modifiers, i);
				world.spawnEntity(minion);
			}
		}
		return true;
	}
}
