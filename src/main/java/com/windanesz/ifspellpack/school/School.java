package com.windanesz.ifspellpack.school;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.spell.Spell;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class School extends IForgeRegistryEntry.Impl<School> {

	private int id;
	private static int counter;
	private static final Gson gson = new Gson();
	private static HashMap<School, List<Spell>> schoolSpells = new HashMap<>();
	public static IForgeRegistry<School> registry;

	public School(String modid, String name) {
		this.setRegistryName(modid, name);
		this.id = counter;
		counter++;
		schoolSpells.put(this, new ArrayList<>());
	}

	public School (String name) {
		this(IFSpellPack.MODID, name);
	}

	public int getId() {
		return this.id;
	}

	public static List<School> getAllSchools() {
		return getSchools(s -> true);
	}

	public static List<School> getSchools(Predicate<School> filter) {
		return registry.getValuesCollection().stream().filter(filter).collect(Collectors.toList());
	}

	public static School get(String name) {
		ResourceLocation key = new ResourceLocation(name);
		if(key.getNamespace().equals("minecraft")) key = new ResourceLocation(IFSpellPack.MODID, name);
		return registry.getValue(key);
	}

	public static Collection<ResourceLocation> getSchoolNames() {
		return new HashSet<>(registry.getKeys());
	}

	public static Set<String> getSpellNames(School school) {
		Set<String> spellNames = new HashSet<>();
		for (Spell spell : getSpells(school)) {
			spellNames.add(spell.getRegistryName().toString());
		}
		return spellNames;
	}

	public static Set<String> getSchoolNames(Spell spell) {
		Set<String> schoolNames = new HashSet<>();
		for (School school : getSchoolsForSpell(spell)) {
			schoolNames.add(school.getRegistryName().toString());
		}
		return schoolNames;
	}

	public static List<School> getSchoolsForSpell(Spell spell) {
		List<School> schools = new ArrayList<>();
		for (School school : schoolSpells.keySet()) {
			if (containsSpell(school, spell)) {
				schools.add(school);
			}
		}
		return schools;
	}

	public static void init() {
		// Collecting to a set should give us one of each mod ID
		Set<String> modIDs = School.getAllSchools().stream().map(s -> s.getRegistryName().getNamespace()).collect(Collectors.toSet());
		boolean flag = true;
		for (String modID : modIDs) {
			flag &= loadSchoolData(modID);
		}
		if (!flag)
			IFSpellPack.logger.warn("Some school property files did not load correctly; this will likely cause problems later!");
	}

	public static List<Spell> getSpells(School school) {
		return schoolSpells.get(school);
	}

	public static boolean containsSpell(School school, Spell spell) {
		return getSpells(school).contains(spell);
	}

	private static boolean loadSchoolData(String modID) {
		ModContainer mod = Loader.instance().getModList().stream().filter(m -> m.getModId().equals(modID)).findFirst().orElse(null);
		if (mod == null) {
			IFSpellPack.logger.warn("Tried to load built-in spell properties for mod with ID '" + modID + "', but no such mod was loaded");
			return false;
		}
		List<School> schools = School.getSchools(s -> s.getRegistryName().getNamespace().equals(modID));
		IFSpellPack.logger.info("Loading built-in school properties for " + schools.size() + " schools in mod " + modID);
		boolean success = CraftingHelper.findFiles(mod, "assets/" + modID + "/schools", null, (root, file) -> {
			String relative = root.relativize(file).toString();
			if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_")) {
				return true;
			}
			String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
			ResourceLocation key = new ResourceLocation(modID, name);
			School school = School.registry.getValue(key);
			if (school == null) {
				IFSpellPack.logger.info("Spell properties file " + name + ".json does not match any registered spells; ensure the filename is spelled correctly.");
				return true;
			}
			if (!schools.remove(school)) IFSpellPack.logger.warn("What's going on?!");
			BufferedReader reader = null;
			try {
				reader = Files.newBufferedReader(file);
				JsonArray json = JsonUtils.fromJson(gson, reader, JsonArray.class);
				for (JsonElement element : json) {
					String spellName = element.getAsString();
					Spell spell = Spell.get(spellName);
					if (spell == null) {
						IFSpellPack.logger.warn("Could not identify spell for: " + spellName);
					} else {
						schoolSpells.get(school).add(spell);
					}
				}
			} catch (JsonParseException jsonparseexception) {
				IFSpellPack.logger.error("Parsing error loading school property file for " + key, jsonparseexception);
				return false;
			} catch (IOException ioexception) {
				IFSpellPack.logger.error("Couldn't read school property file for " + key, ioexception);
				return false;
			} finally {
				IOUtils.closeQuietly(reader);
			}
			return true;
		},
		true, true);
		return success;
	}
}
