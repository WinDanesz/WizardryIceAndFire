package com.windanesz.ifspellpack.event;

import com.github.alexthe666.iceandfire.entity.*;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.living.*;
import com.windanesz.ifspellpack.potion.PotionDragonrend;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import com.windanesz.ifspellpack.registry.IFSPSpells;
import com.windanesz.ifspellpack.spell.DreadLichSkull;
import com.windanesz.ifspellpack.spell.TrollSkin;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class IFSPServerEvents {

	@SubscribeEvent
	public static void onLivingDeathEvent(LivingDeathEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (event.getSource().getImmediateSource() instanceof EntityDreadLichSkull && event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityDreadLichSkull skull = (EntityDreadLichSkull)event.getSource().getImmediateSource();
			EntityPlayer player = (EntityPlayer)event.getSource().getTrueSource();
			float potency = skull.getEntityData().getFloat(DreadLichSkull.POTENCY_KEY);
			if (ItemArtefact.isArtefactActive(player, WizardryItems.amulet_anchoring)) {
				WizardData data = WizardData.get(player);
				List<UUID> dreadMinionList = data.getVariable(DreadLichSkull.DREAD_MINION_ARMY_UUIDS);
				if (dreadMinionList == null) {
					dreadMinionList = new ArrayList<>();
					data.setVariable(DreadLichSkull.DREAD_MINION_ARMY_UUIDS, dreadMinionList);
				}
				dreadMinionList.removeIf(uuid -> EntityUtils.getEntityByUUID(player.world, uuid) == null);
				if (dreadMinionList.size() < DreadLichSkull.DREAD_MINION_ARMY_BASE_SIZE * potency) {
					//refactored from EntityDreadMob.necromancyEntity(EntityLivingBase entity)
					Entity summonedEntity = null;
					if (entity.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
						summonedEntity = new EntityDreadScuttlerMinion(entity.world);
						float readInScale = (entity.width / 1.5F);
						((EntityDreadScuttler) summonedEntity).onInitialSpawn(entity.world.getDifficultyForLocation(new BlockPos(entity)), null);
						((EntityDreadScuttler) summonedEntity).setScale(readInScale);
					}
					else if (entity instanceof EntityZombie || entity instanceof IHumanoid) {
						summonedEntity = new EntityDreadGhoulMinion(entity.world);
						float readInScale = (entity.width / 0.6F);
						((EntityDreadGhoul) summonedEntity).onInitialSpawn(entity.world.getDifficultyForLocation(new BlockPos(entity)), null);
						((EntityDreadGhoul) summonedEntity).setScale(readInScale);
					}
					else if (entity.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD || entity instanceof AbstractSkeleton || entity instanceof EntityPlayer) {
						summonedEntity = new EntityDreadThrallMinion(entity.world);
						EntityDreadThrall thrall = (EntityDreadThrall) summonedEntity;
						thrall.onInitialSpawn(entity.world.getDifficultyForLocation(new BlockPos(entity)), null);
						thrall.setCustomArmorHead(false);
						thrall.setCustomArmorChest(false);
						thrall.setCustomArmorLegs(false);
						thrall.setCustomArmorFeet(false);
						for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
							thrall.setItemStackToSlot(slot, entity.getItemStackFromSlot(slot));
						}
					}
					else if (entity instanceof AbstractHorse) {
						summonedEntity = new EntityDreadHorseMinion(entity.world);
					}
					else if (entity instanceof EntityAnimal) {
						summonedEntity = new EntityDreadBeastMinion(entity.world);
						float readInScale = (entity.width / 1.2F);
						((EntityDreadBeast) summonedEntity).onInitialSpawn(entity.world.getDifficultyForLocation(new BlockPos(entity)), null);
						((EntityDreadBeast) summonedEntity).setScale(readInScale);
					}
					if (summonedEntity != null) {
						((ISummonedCreature)summonedEntity).setCaster(player);
						dreadMinionList.add(summonedEntity.getUniqueID());
						summonedEntity.copyLocationAndAnglesFrom(entity);
						if (!player.world.isRemote) {
							player.world.spawnEntity(summonedEntity);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		DamageSource source = event.getSource();
		float damage = event.getAmount();
		float amount = event.getAmount();
		//Troll Skin potion
		if (entity.isPotionActive(IFSPPotions.TROLL_SKIN) && source.getDamageType().contains("arrow")) {
			event.setAmount((float)(amount * Math.pow(1 - IFSPSpells.TROLL_SKIN.getProperty(TrollSkin.DAMAGE_REDUCTION).doubleValue(), entity.getActivePotionEffect(IFSPPotions.TROLL_SKIN).getAmplifier() + 1)));
		}
		//Dragonrend potion
		if (entity.isPotionActive(IFSPPotions.DRAGONREND) && entity instanceof EntityDragonBase) {
			PotionEffect potionEffect = entity.getActivePotionEffect(IFSPPotions.DRAGONREND);
			int amplifier = potionEffect.getAmplifier();
			if (amplifier > 0) {
				event.setAmount(damage + (amplifier * PotionDragonrend.DAMAGE_INCREASE));
			}
		}
	}

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class);
		//Breaking stone summoned creatures
		if (properties != null && properties.isStone && entity instanceof ISummonedCreature) {
			entity.playSound(SoundEvents.BLOCK_STONE_BREAK, 1, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 0.5F);
			//Cant do all 3 because it causes a crash. Possibly because the world refers to the entity being removed?
			//entity.setDead();
			world.removeEntity(entity);
			//world.removeEntityDangerously(entity);
		}
	}

	@SubscribeEvent
	public static void onSpellCastEventPre(SpellCastEvent.Pre event) {
		Spell spell = event.getSpell();
		SpellModifiers modifiers = event.getModifiers();
		//Global IFSpellpack potency modifier
		if (spell.getRegistryName().getNamespace().equals(IFSpellPack.MODID)) {
			modifiers.set(SpellModifiers.POTENCY, IFSpellPack.settings.iafPotencyModifier, false);
		}
	}

}
