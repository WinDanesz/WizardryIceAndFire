package com.windanesz.ifspellpack.command;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.school.School;
import electroblob.wizardry.spell.Spell;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;
import java.util.Set;

public class CommandSchoolInfo extends CommandBase {

	private final String SPELLS = "spells";
	private final String SCHOOLS = "schools";

	@Override
	public String getName(){
		return IFSpellPack.settings.schoolinfo;
	}

	@Override
	public int getRequiredPermissionLevel(){
		return 0;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender){
		return true;
	}

	@Override
	public String getUsage(ICommandSender sender){
		return "/" + this.getName() + " <schools/spells> <spell/school>";
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] arguments,
			BlockPos pos){
		switch(arguments.length){
			case 1:
				return getListOfStringsMatchingLastWord(arguments, SPELLS, SCHOOLS);
			case 2:
				if (arguments[0].equals(SPELLS)) {
					return getListOfStringsMatchingLastWord(arguments, School.getSchoolNames());
				} else if (arguments[0].equals(SCHOOLS)) {
					return getListOfStringsMatchingLastWord(arguments, Spell.getSpellNames());
				}
		}
		return super.getTabCompletions(server, sender, arguments, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] arguments) throws CommandException {
		if (arguments.length < 1) {
			throw new WrongUsageException("command." + IFSpellPack.MODID + ":schoolinfo.usage", this.getName());
		}
		if (arguments.length != 2) {
			throw new WrongUsageException("Need two arguments");
		} else {
			Set<String> output;
			if (arguments[0].equals(SPELLS)) {
				School school = School.get(arguments[1]);
				if (school == null) {
					throw new NumberInvalidException("command." + IFSpellPack.MODID + ":schoolinfo.school_not_found", arguments[1]);
				} else {
					output = School.getSpellNames(school);
					if (output.isEmpty()) {
						throw new NumberInvalidException("command." + IFSpellPack.MODID + ":schoolinfo.no_spells", arguments[1]);
					}
				}
				Object spellNames = joinNiceStringFromCollection(output);
				sender.sendMessage(new TextComponentTranslation("command." + IFSpellPack.MODID + ":schoolinfo.list_spells", arguments[1], spellNames));
			}
			if (arguments[0].equals(SCHOOLS)) {
				Spell spell = Spell.get(arguments[1]);
				if (spell == null) {
					throw new NumberInvalidException("command." + IFSpellPack.MODID + ":schoolinfo.spell_not_found", arguments[1]);
				} else {
					output = School.getSchoolNames(spell);
					if (output.isEmpty()) {
						throw new NumberInvalidException("command." + IFSpellPack.MODID + ":schoolinfo.no_schools", arguments[1]);
					}
				}
				Object schoolNames = joinNiceStringFromCollection(output);
				sender.sendMessage(new TextComponentTranslation("command." + IFSpellPack.MODID + ":schoolinfo.list_schools", arguments[1], schoolNames));
			}
		}
	}

}
