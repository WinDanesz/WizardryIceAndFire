package com.windanesz.ifspellpack.event;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.enchantment.EnchantmentDragonbane;
import com.windanesz.ifspellpack.enchantment.EnchantmentSilverLining;
import com.windanesz.ifspellpack.entity.living.*;
import com.windanesz.ifspellpack.item.ItemChargedArtefact;
import com.windanesz.ifspellpack.item.ItemCharmLoversHeart;
import com.windanesz.ifspellpack.potion.PotionAllure;
import com.windanesz.ifspellpack.potion.PotionDragonrend;
import com.windanesz.ifspellpack.potion.PotionMyrmexBlessing;
import com.windanesz.ifspellpack.registry.*;
import com.windanesz.ifspellpack.school.School;
import com.windanesz.ifspellpack.spell.DreadLichSkull;
import com.windanesz.ifspellpack.spell.GorgonGaze;
import com.windanesz.ifspellpack.spell.TrollSkin;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.data.IVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.ImbueWeapon;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class IFSPServerEvents {

	public static final IVariable<Boolean> ENCHANTED_MANUSCRIPT_ACTIVE = new IVariable.Variable<>(Persistence.NEVER);
	public static final IVariable<Boolean> FIRE_DRAGON_CORE_FIRE_ACTIVE = new IVariable.Variable<>(Persistence.NEVER);
	public static final IVariable<Boolean> FIRE_DRAGON_CORE_DRACONIC_ACTIVE = new IVariable.Variable<>(Persistence.NEVER);
	public static final IVariable<Boolean> ICE_DRAGON_CORE_ICE_ACTIVE = new IVariable.Variable<>(Persistence.NEVER);
	public static final IVariable<Boolean> ICE_DRAGON_CORE_DRACONIC_ACTIVE = new IVariable.Variable<>(Persistence.NEVER);
	public static final IVariable<Boolean> LIGHTNING_DRAGON_CORE_LIGHTNING_ACTIVE = new IVariable.Variable<>(Persistence.NEVER);
	public static final IVariable<Boolean> LIGHTNING_DRAGON_CORE_DRACONIC_ACTIVE = new IVariable.Variable<>(Persistence.NEVER);

	@SubscribeEvent
	public static void onAttackEntityEvent(AttackEntityEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		if (event.getTarget() instanceof EntityLivingBase) {
			EntityLivingBase target = (EntityLivingBase)event.getTarget();
			if (ItemArtefact.isArtefactActive(player, IFSPItems.RING_STINGER)) {
				for (int i : BaubleType.RING.getValidSlots()) {
					if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(i))) {
						target.addPotionEffect(new PotionEffect(MobEffects.POISON, 60, 1));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof EntityArrow) {
			EntityArrow arrow = (EntityArrow)entity;
			if (arrow.shootingEntity instanceof EntityLivingBase) {
				EntityLivingBase archer = (EntityLivingBase)arrow.shootingEntity;
				ItemStack bow = archer.getHeldItemMainhand();
				if(!ImbueWeapon.isBow(bow)) {
					bow = archer.getHeldItemOffhand();
				}
				if (ImbueWeapon.isBow(bow)) {
					int level = EnchantmentHelper.getEnchantmentLevel(IFSPEnchantments.DRAGONBANE, bow);
					if (level > 0) {
						arrow.getEntityData().setInteger(EnchantmentDragonbane.DRAGONBANE_KEY, level);
						float velocityMultiplier = 1f + level * EnchantmentDragonbane.DRAGONBANE_VELOCITY_INCREASE;
						arrow.motionX *= velocityMultiplier;
						arrow.motionY *= velocityMultiplier;
						arrow.motionZ *= velocityMultiplier;
					}
				}
			}
		}
	}

	//after living hurt
	@SubscribeEvent
	public static void onLivingDamageEvent(LivingDamageEvent event) {
		EntityLivingBase entityLivingBase = event.getEntityLiving();
		if (entityLivingBase instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entityLivingBase;
			if (event.getAmount() >= player.getHealth()) {
				if (ItemArtefact.isArtefactActive(player, IFSPItems.CHARM_REGENERATING_HEAD)) {
					if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(6))) {
						event.setCanceled(true);
						IFSPSpells.HYDRA_PULSE.cast(player.world, player, EnumHand.MAIN_HAND, 0, new SpellModifiers());
					}
				}
			}
		} else {
			if (entityLivingBase.getEntityData().hasKey(GorgonGaze.CHECK_GAZE_DEATH_KEY) && event.getAmount() >= entityLivingBase.getHealth()) {
				event.setCanceled(true);
				entityLivingBase.getEntityData().setBoolean(GorgonGaze.CHECK_GAZE_DEATH_KEY, true);
			}
		}
	}


	@SubscribeEvent
	public static void onLivingDeathEvent(LivingDeathEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (!(entity instanceof ISummonedCreature) && event.getSource().getImmediateSource() instanceof EntityDreadLichSkull && event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityDreadLichSkull skull = (EntityDreadLichSkull)event.getSource().getImmediateSource();
			EntityPlayer player = (EntityPlayer)event.getSource().getTrueSource();
			float potency = skull.getEntityData().getFloat(DreadLichSkull.POTENCY_KEY);
			if (ItemArtefact.isArtefactActive(player, IFSPItems.AMULET_DAMNED)) {
				if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(0))) {
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
						} else if (entity instanceof EntityZombie || entity instanceof IHumanoid) {
							summonedEntity = new EntityDreadGhoulMinion(entity.world);
							float readInScale = (entity.width / 0.6F);
							((EntityDreadGhoul) summonedEntity).onInitialSpawn(entity.world.getDifficultyForLocation(new BlockPos(entity)), null);
							((EntityDreadGhoul) summonedEntity).setScale(readInScale);
						} else if (entity.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD || entity instanceof AbstractSkeleton || entity instanceof EntityPlayer) {
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
						} else if (entity instanceof AbstractHorse) {
							summonedEntity = new EntityDreadHorseMinion(entity.world);
						} else if (entity instanceof EntityAnimal) {
							summonedEntity = new EntityDreadBeastMinion(entity.world);
							float readInScale = (entity.width / 1.2F);
							((EntityDreadBeast) summonedEntity).onInitialSpawn(entity.world.getDifficultyForLocation(new BlockPos(entity)), null);
							((EntityDreadBeast) summonedEntity).setScale(readInScale);
						}
						if (summonedEntity != null) {
							((ISummonedCreature) summonedEntity).setCaster(player);
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
	}

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		DamageSource source = event.getSource();
		float damage = event.getAmount();
		//For attacks against the player
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entity;
			for (ItemArtefact artefact : ItemArtefact.getActiveArtefacts(player)) {
				//Lightward Amulet Damage Reduction
				if (artefact == IFSPItems.AMULET_LIGHTWARD && source.getTrueSource() instanceof IDreadMob) {
					damage *= 0.9f;
				}
				//Dragon Slayer Amulet Damage Reduction
				if (artefact == IFSPItems.AMULET_DRAGON_SLAYER && source.getTrueSource() instanceof EntityDragon || source.getTrueSource() instanceof EntityDragonBase) {
					damage *= 0.9f;
				}
			}
		}
		//For attacks from the player
		if (source.getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)source.getTrueSource();
			for (ItemArtefact artefact : ItemArtefact.getActiveArtefacts(player)) {
				//Lightward Amulet Damage Increase
				if (artefact == IFSPItems.AMULET_LIGHTWARD && entity instanceof IDreadMob) {
					damage *= 1.1f;
				}
				//Dragon Slayer Amulet Damage Increase
				if (artefact == IFSPItems.AMULET_DRAGON_SLAYER && entity instanceof EntityDragon || entity instanceof EntityDragonBase) {
					damage *= 1.1f;
				}
			}
		}
		//For attacks against EntityLivingBase
		//For attacks from EntityLivingBase
		if (source.getTrueSource() instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase)source.getTrueSource();
			//Allure bonus damage from Lover's Heart
			PotionEffect potion = attacker.getActivePotionEffect(IFSPPotions.ALLURE);
			if (potion != null) {
				damage *= ItemCharmLoversHeart.damageMultiplier(potion.getAmplifier());
			}
			if (EntityUtils.isMeleeDamage(source)) {
				ItemStack sword = attacker.getHeldItemMainhand();
				if (ImbueWeapon.isSword(sword)) {
					int level = EnchantmentHelper.getEnchantmentLevel(IFSPEnchantments.SILVER_LINING, sword);
					if (level > 0) {
						if (entity instanceof IDreadMob) {
							damage *= 1 + (level * EnchantmentSilverLining.DAMAGE_INCREASE);
							entity.setFire(EnchantmentSilverLining.BURN_TIME);
						}
					}
				}
			}
		}
		//Troll Skin potion
		if (entity.isPotionActive(IFSPPotions.TROLL_SKIN) && source.isProjectile()) {
			damage = (float)(damage * Math.pow(1 - IFSPSpells.TROLL_SKIN.getProperty(TrollSkin.DAMAGE_REDUCTION).doubleValue(), entity.getActivePotionEffect(IFSPPotions.TROLL_SKIN).getAmplifier() + 1));
		}
		//Dragonrend potion
		if (entity.isPotionActive(IFSPPotions.DRAGONREND) && (entity instanceof EntityDragonBase || entity instanceof EntityDragon)) {
			PotionEffect potionEffect = entity.getActivePotionEffect(IFSPPotions.DRAGONREND);
			int amplifier = potionEffect.getAmplifier();
			if (amplifier > 0) {
				damage *= (1 + amplifier * PotionDragonrend.DAMAGE_INCREASE);
			}
		}
		//Dragonbane enchantment
		if (source.getImmediateSource() != null) {
			int level = source.getImmediateSource().getEntityData().getInteger(EnchantmentDragonbane.DRAGONBANE_KEY);
			if (level > 0) {
				if (entity instanceof EntityDragonBase || entity instanceof EntityDragon) {
					damage *= (1 + level * EnchantmentDragonbane.DRAGONBANE_DAMAGE_INCREASE);
				}
			}
		}
		//set the damage value after all the calculations
		event.setAmount(damage);
	}


	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onLivingHurtEventEarly(LivingHurtEvent event) {
		EntityLivingBase target = event.getEntityLiving();
		DamageSource source = event.getSource();
		//Myrmex Blessing
		if (source.getTrueSource() instanceof EntityLiving && EntityUtils.isMeleeDamage(source)) {
			EntityLiving attacker = (EntityLiving)source.getTrueSource();
			PotionEffect effect = attacker.getActivePotionEffect(IFSPPotions.MYRMEX_BLESSING);
			if (effect != null) {
				float bonusDamage = (effect.getAmplifier() + 1) * PotionMyrmexBlessing.DAMAGE_INCREASE;
				event.setAmount(event.getAmount() + bonusDamage);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
		EntityLivingBase attacker = event.getEntityLiving();
		EntityLivingBase target = event.getTarget();
		if (target != null) {
			//Sentinel Shell
			if (target.isPotionActive(IFSPPotions.SENTINEL_SHELL)) {
				if (attacker instanceof EntityLiving) {
					int equipmentCount = target.getActivePotionEffect(IFSPPotions.SENTINEL_SHELL).getAmplifier();
					for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
						if (target.getItemStackFromSlot(entityequipmentslot) != ItemStack.EMPTY) {
							equipmentCount++;
						}
					}
					if (equipmentCount > 0) {
						float equipmentPercentage = (float) equipmentCount / EntityEquipmentSlot.values().length;
						IAttributeInstance attribute = attacker.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
						double followRange = attribute == null ? 16 : attribute.getAttributeValue();
						if (event.getTarget().isSneaking()) followRange *= 0.5;
						followRange *= equipmentPercentage;
						if (target.getDistance(attacker) > followRange) {
							((EntityLiving) attacker).setAttackTarget(null);
						}
					} else {
						((EntityLiving) attacker).setAttackTarget(null);
					}
				}
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
			entity.setDead();
		}
		//Allure potion effect
		if (entity.isPotionActive(IFSPPotions.ALLURE)) {
			UUID uuid = entity.getEntityData().getUniqueId(PotionAllure.UUID_KEY);
			if (uuid != null) {
				Entity allurer = EntityUtils.getEntityByUUID(world, uuid);
				if (allurer == null) {
					entity.removePotionEffect(IFSPPotions.ALLURE);
				} else {
					if (!(allurer instanceof EntityLivingBase)) {
						entity.removePotionEffect(IFSPPotions.ALLURE);
					} else {
						if (entity.isRiding()) {
							entity.dismountRidingEntity();
						}
						if (entity.collidedHorizontally) {
							if (entity.onGround) {
								if (entity instanceof EntityPlayer) {
									((EntityPlayer) entity).jump();
								} else if (entity instanceof EntityLiving) {
									((EntityLiving) entity).getJumpHelper().setJumping();
								}
							}
						}
						double d0 = allurer.posX - entity.posX;
						double d1 = allurer.posY - entity.posY;
						double d2 = allurer.posZ - entity.posZ;
						entity.motionX += (Math.signum(d0) * 0.5D - entity.motionX) * 0.100000000372529;
						entity.motionY += (Math.signum(d1) * 0.5D - entity.motionY) * 0.100000000372529;
						entity.motionZ += (Math.signum(d2) * 0.5D - entity.motionZ) * 0.100000000372529;
						if (entity.isRiding()) {
							entity.dismountRidingEntity();
						}
						double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
						float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
						float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
						if (!(entity instanceof EntityPlayer)) {
							entity.rotationPitch = ServerEvents.updateRotation(entity.rotationPitch, f1, 30F);
							entity.rotationYaw = ServerEvents.updateRotation(entity.rotationYaw, f, 30F);
						}
						if (world.rand.nextInt(7) == 0) {
							for (int i = 0; i < 5; i++) {
								event.getEntityLiving().world.spawnParticle(EnumParticleTypes.HEART, event.getEntityLiving().posX + ((world.rand.nextDouble() - 0.5D) * 3), event.getEntityLiving().posY + ((world.rand.nextDouble() - 0.5D) * 3), event.getEntityLiving().posZ + ((world.rand.nextDouble() - 0.5D) * 3), 0, 0, 0);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onSpellCastEventPre(SpellCastEvent.Pre event) {
		Spell spell = event.getSpell();
		SpellModifiers modifiers = event.getModifiers();
		//Global IFSpellpack potency modifier
		if (spell.getRegistryName().getNamespace().equals(IFSpellPack.MODID)) {
			modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * IFSpellPack.settings.iafPotencyModifier, false);
		}
		if (event.getCaster() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.getCaster();
			WizardData data = WizardData.get(player);
			if (data != null) {
				int distributedCost = ItemChargedArtefact.getDistributedCost(spell.getCost(), 0);
				//Enchanted manuscript
				if (ItemArtefact.isArtefactActive(player, IFSPItems.CHARM_ENCHANTED_MANUSCRIPT)) {
					if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(6), distributedCost)) {
						modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * 1.2f, false);
						data.setVariable(ENCHANTED_MANUSCRIPT_ACTIVE, true);
					}
				}
				//Fire Dragon Core
				if (ItemArtefact.isArtefactActive(player, IFSPItems.BODY_FIRE_DRAGON_CORE)) {
					boolean fire = spell.getElement() == Element.FIRE;
					boolean draconic = School.containsSpell(IFSPSchools.DRACONIC, spell);
					if (fire || draconic) {
						if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(5), distributedCost)) {
							if (fire) {
								modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * 1.2f, false);
								data.setVariable(FIRE_DRAGON_CORE_FIRE_ACTIVE, true);
							}
							if (draconic) {
								modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * 1.2f, false);
								data.setVariable(FIRE_DRAGON_CORE_DRACONIC_ACTIVE, true);
							}
						}
					}
				}
				//Ice Dragon Core
				if (ItemArtefact.isArtefactActive(player, IFSPItems.BODY_ICE_DRAGON_CORE)) {
					boolean ice = spell.getElement() == Element.ICE;
					boolean draconic = School.containsSpell(IFSPSchools.DRACONIC, spell);
					if (ice || draconic) {
						if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(5), distributedCost)) {
							if (ice) {
								modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * 1.2f, false);
								data.setVariable(ICE_DRAGON_CORE_ICE_ACTIVE, true);
							}
							if (draconic) {
								modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * 1.2f, false);
								data.setVariable(ICE_DRAGON_CORE_DRACONIC_ACTIVE, true);
							}
						}
					}
				}
				//Lightning Dragon Core
				if (ItemArtefact.isArtefactActive(player, IFSPItems.BODY_LIGHTNING_DRAGON_CORE)) {
					boolean lightning = spell.getElement() == Element.LIGHTNING;
					boolean draconic = School.containsSpell(IFSPSchools.DRACONIC, spell);
					if (lightning || draconic) {
						if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(5), distributedCost)) {
							if (lightning) {
								modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * 1.2f, false);
								data.setVariable(LIGHTNING_DRAGON_CORE_LIGHTNING_ACTIVE, true);
							}
							if (draconic) {
								modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * 1.2f, false);
								data.setVariable(LIGHTNING_DRAGON_CORE_DRACONIC_ACTIVE, true);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onSpellCastEventTick(SpellCastEvent.Tick event) {
		Spell spell = event.getSpell();
		SpellModifiers modifiers = event.getModifiers();
		if (event.getCaster() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.getCaster();
			WizardData data = WizardData.get(player);
			if (data != null) {
				int distributedCost = ItemChargedArtefact.getDistributedCost(spell.getCost(), event.getCount());
				//Enchanted manuscript
				if (ItemArtefact.isArtefactActive(player, IFSPItems.CHARM_ENCHANTED_MANUSCRIPT)) {
					Boolean active = data.getVariable(ENCHANTED_MANUSCRIPT_ACTIVE);
					if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(6), distributedCost)) {
						if (active != null && !active) {
							modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * 1.2f, false);
							data.setVariable(ENCHANTED_MANUSCRIPT_ACTIVE, true);
						}
					} else {
						if (active != null && active) {
							modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) / 1.2f, false);
							data.setVariable(ENCHANTED_MANUSCRIPT_ACTIVE, false);
						}
					}
				}
				//Fire Dragon Core
				if (ItemArtefact.isArtefactActive(player, IFSPItems.BODY_FIRE_DRAGON_CORE)) {
					Boolean fire = data.getVariable(FIRE_DRAGON_CORE_FIRE_ACTIVE);
					Boolean draconic = data.getVariable(FIRE_DRAGON_CORE_DRACONIC_ACTIVE);
					if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(5), distributedCost)) {
						if (fire != null && !fire) {
							modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * 1.2f, false);
						}
						if (draconic != null && !draconic) {
							modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * 1.2f, false);
						}
					} else {
						if (fire != null && fire) {
							modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) / 1.2f, false);
						}
						if (draconic != null && draconic) {
							modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) / 1.2f, false);
						}
					}
				}
				//Ice Dragon Core
				if (ItemArtefact.isArtefactActive(player, IFSPItems.BODY_ICE_DRAGON_CORE)) {
					Boolean ice = data.getVariable(ICE_DRAGON_CORE_ICE_ACTIVE);
					Boolean draconic = data.getVariable(ICE_DRAGON_CORE_DRACONIC_ACTIVE);
					if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(5), distributedCost)) {
						if (ice != null && !ice) {
							modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * 1.2f, false);
						}
						if (draconic != null && !draconic) {
							modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * 1.2f, false);
						}
					} else {
						if (ice != null && ice) {
							modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) / 1.2f, false);
						}
						if (draconic != null && draconic) {
							modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) / 1.2f, false);
						}
					}
				}
				//Lightning Dragon Core
				if (ItemArtefact.isArtefactActive(player, IFSPItems.BODY_LIGHTNING_DRAGON_CORE)) {
					Boolean lightning = data.getVariable(LIGHTNING_DRAGON_CORE_LIGHTNING_ACTIVE);
					Boolean draconic = data.getVariable(LIGHTNING_DRAGON_CORE_DRACONIC_ACTIVE);
					if (player.isCreative() || ItemChargedArtefact.consumeCharge(BaublesApi.getBaublesHandler(player).getStackInSlot(5), distributedCost)) {
						if (lightning != null && !lightning) {
							modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * 1.2f, false);
						}
						if (draconic != null && !draconic) {
							modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) * 1.2f, false);
						}
					} else {
						if (lightning != null && lightning) {
							modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) / 1.2f, false);
						}
						if (draconic != null && draconic) {
							modifiers.set(SpellModifiers.POTENCY, modifiers.get(SpellModifiers.POTENCY) / 1.2f, false);
						}

					}
				}
			}
		}
	}

}
