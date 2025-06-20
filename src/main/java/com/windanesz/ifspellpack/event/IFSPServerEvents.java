package com.windanesz.ifspellpack.event;

import com.github.alexthe666.iceandfire.entity.ChainEntityProperties;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.Settings;
import com.windanesz.ifspellpack.accessor.AccessorChainEntityProperties;
import com.windanesz.ifspellpack.potion.PotionTrollSkin;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import com.windanesz.ifspellpack.registry.IFSPSpells;
import com.windanesz.ifspellpack.spell.TrollSkin;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.item.ISpellCastingItem;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.InventoryUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class IFSPServerEvents {

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		DamageSource source = event.getSource();
		float amount = event.getAmount();
		if (entity.isPotionActive(IFSPPotions.TROLL_SKIN) && source.getDamageType().contains("arrow")) {
			event.setAmount((float)(amount * Math.pow(1 - IFSPSpells.TROLL_SKIN.getProperty(TrollSkin.DAMAGE_REDUCTION).doubleValue(), entity.getActivePotionEffect(IFSPPotions.TROLL_SKIN).getAmplifier() + 1)));
		}
	}

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class);
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
		if (spell.getRegistryName().getNamespace().equals(IFSpellPack.MODID)) {
			modifiers.set(SpellModifiers.POTENCY, IFSpellPack.settings.iafPotencyModifier, false);
		}
	}

}
