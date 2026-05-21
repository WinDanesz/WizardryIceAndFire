package com.windanesz.ifspellpack.world;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.util.NBTExtras;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class SlayerTracker {

	public static Map<String, Integer> VALID_ENTITIES = new HashMap<>();

	public static final IStoredVariable<Map<String, Integer>> KILL_TRACKER = new IStoredVariable.StoredVariable<Map<String, Integer>, NBTTagList>("ifspellpack:KillTracker", s -> NBTExtras.mapToNBT(s, NBTTagString::new, NBTTagInt::new), t -> new HashMap<>(NBTExtras.NBTToMap(t, NBTTagString::getString, NBTTagInt::getInt)), Persistence.ALWAYS).setSynced();

	public static final IStoredVariable<Integer> POINT_TRACKER = IStoredVariable.StoredVariable.ofInt("ifspellpack:PointTracker", Persistence.ALWAYS).setSynced();

	public static void init() {
		WizardData.registerStoredVariables(KILL_TRACKER, POINT_TRACKER);
		for (String string : IFSpellPack.settings.slayerTrackerEntities) {
			String[] args = string.split(" ");
			if (args.length != 2) {
				IFSpellPack.logger.warn("Invalid entry in slayer tracker: {}", string);
			} else {
				try {
					VALID_ENTITIES.put(args[0], Integer.parseInt(args[1]));
				} catch (NumberFormatException exception) {
					Wizardry.logger.warn("Invalid integer in slayer tracker: {}", args[1]);
				}
			}
		}
	}

	//Entity.getEntityString is protected so this does the same thing
	public static boolean isEntityValid(Entity entity) {
		ResourceLocation resourceLocation = EntityList.getKey(entity);
		return resourceLocation != null && VALID_ENTITIES.containsKey(resourceLocation.toString());
	}

	public static int getPoints(Entity entity) {
		int points = VALID_ENTITIES.get(EntityList.getKey(entity).toString());
		if (IFSpellPack.settings.shouldSlayerKillsScale) {
			if (entity instanceof EntityDragonBase) {
				EntityDragonBase dragon = (EntityDragonBase)entity;
				points *= dragon.getDragonStage();
			} else if (entity instanceof EntitySeaSerpent) {
				EntitySeaSerpent serpent = (EntitySeaSerpent)entity;
				points *= (int)Math.ceil(serpent.getSeaSerpentScale());
			} else if (entity instanceof EntityDeathWorm) {
				EntityDeathWorm deathWorm = (EntityDeathWorm)entity;
				points *= (int)Math.ceil(deathWorm.getScale());
			}
		}
		return points;
	}

}
