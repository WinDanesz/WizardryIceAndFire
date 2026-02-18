package com.windanesz.ifspellpack.command;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.school.School;
import com.windanesz.ifspellpack.world.SlayerTracker;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.spell.Spell;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandSlayerTracker extends CommandBase {

	private final String points = "points";
	private final String entities = "entities";
	private final String killcount = "killcount";

	@Override
	public String getName(){
		return IFSpellPack.settings.slayerTrackerInfo;
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
		return "/" + this.getName() + " <" + this.points + "/" + this.entities + "/" + this.killcount + ">";
	}

	public String getResourceLocationTranslation() {
		return "command." + IFSpellPack.MODID + ":" + this.getName();
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] arguments,
			BlockPos pos){
		switch(arguments.length){
			case 1:
				return getListOfStringsMatchingLastWord(arguments, this.points, this.entities, this.killcount);
			case 2:
				if (arguments[0].equals(this.killcount)) {
					return getListOfStringsMatchingLastWord(arguments, SlayerTracker.VALID_ENTITIES.keySet());
				}
		}
		return super.getTabCompletions(server, sender, arguments, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] arguments) throws CommandException {
		if (arguments.length < 1) {
			throw new WrongUsageException(this.getResourceLocationTranslation() + ".usage", this.getName());
		}
		EntityPlayerMP player = null;
		try{
			player = getCommandSenderAsPlayer(sender);
		}catch (PlayerNotFoundException exception){
			// Nothing here since the player specifying is done later, I just don't want it to throw an exception here.
		}
		if (arguments.length == 1) {
			if (arguments[0].equals(this.points)) {
				if (player != null) {
					WizardData data = WizardData.get(player);
					if (data != null) {
						Integer points = data.getVariable(SlayerTracker.POINT_TRACKER);
						if (points == null) {
							points = 0;
						}
						sender.sendMessage(new TextComponentTranslation(this.getResourceLocationTranslation() + ".points", points, points == 1 ? "point" : "points"));
					}
				} else {
					throw new WrongUsageException(this.getResourceLocationTranslation() + ".player", this.getName());
				}
			} else if (arguments[0].equals(this.entities)) {
				sender.sendMessage(new TextComponentTranslation(this.getResourceLocationTranslation() + ".entities", joinNiceStringFromCollection(SlayerTracker.VALID_ENTITIES.keySet())));
			}
		} else if (arguments.length == 2) {
			if (arguments[0].equals(this.killcount)) {
				if (SlayerTracker.VALID_ENTITIES.containsKey(arguments[1])) {
					if (player != null) {
						WizardData data = WizardData.get(player);
						if (data != null) {
							Map<String, Integer> kills = data.getVariable(SlayerTracker.KILL_TRACKER);
							int killcount = 0;
							if (kills != null && kills.containsKey(arguments[1])) {
								killcount = kills.get(arguments[1]);
							}
							sender.sendMessage(new TextComponentTranslation(this.getResourceLocationTranslation() + ".kills", arguments[1], killcount, killcount == 1 ? "time" : "times"));
						}
					} else {
						throw new WrongUsageException(this.getResourceLocationTranslation() + ".player", this.getName());
					}
				}
			}
		} else {
			throw new WrongUsageException(this.getResourceLocationTranslation() + ".usage", this.getName());
		}
	}

}
