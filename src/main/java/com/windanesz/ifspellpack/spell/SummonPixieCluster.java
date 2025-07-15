package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.living.EntityPixieMinion;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.entity.living.EntitySpiritWolf;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.NBTExtras;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class SummonPixieCluster extends Spell {

	public static final IStoredVariable<List<UUID>> PIXIE_MINION_CLUSTER_UUIDS = new IStoredVariable.StoredVariable<List<UUID>, NBTTagList>("ifspellpack:pixieMinionCluster",
			s -> NBTExtras.listToNBT(s, NBTUtil::createUUIDTag), t -> new ArrayList<>(NBTExtras.NBTToList(t, NBTUtil::getUUIDFromTag)), Persistence.ALWAYS).setSynced();

	private static final String POTENCY_ATTRIBUTE_MODIFIER = "potency";
	private static final String STAT_SCALE = "stat_scale";

	public SummonPixieCluster() {
		super(IFSpellPack.MODID, "summon_pixie_cluster", SpellActions.SUMMON, false);
		this.addProperties(SpellMinion.MINION_COUNT, SpellMinion.SUMMON_RADIUS, STAT_SCALE);
		WizardData.registerStoredVariables(PIXIE_MINION_CLUSTER_UUIDS);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		int pixieCount = (int)(this.getProperty(SpellMinion.MINION_COUNT).intValue() * modifiers.get(SpellModifiers.POTENCY));
		WizardData data = WizardData.get(caster);
		List<UUID> pixieUUIDs = data.getVariable(PIXIE_MINION_CLUSTER_UUIDS);
		if (pixieUUIDs == null) {
			pixieUUIDs = new ArrayList<>();
			data.setVariable(PIXIE_MINION_CLUSTER_UUIDS, pixieUUIDs);
		}
		//Filter any pixies that have died
		pixieUUIDs.removeIf(uuid -> EntityUtils.getEntityByUUID(caster.world, uuid) == null);
		List<EntityPixieMinion> pixies = new ArrayList<>();
		//Create a list of pixies from the UUIDs
		for (UUID uuid : pixieUUIDs) {
			Entity pixie = EntityUtils.getEntityByUUID(world, uuid);
			if (pixie instanceof EntityPixieMinion) {
				pixies.add((EntityPixieMinion)pixie);
			}
		}
		//Sneak casting despawns all pixies
		if (caster.isSneaking()) {
			for (EntityPixieMinion pixie : pixies) {
				pixie.onDespawn();
				pixie.setDead();
			}
			return false;
		} else {
			int missingPixies = pixieCount - pixieUUIDs.size();
			if (missingPixies <= 0) {
				for (EntityPixieMinion pixie : pixies) {
					BlockPos pos = BlockUtils.findNearbyFloorSpace(caster, 2, 4);
					if (pos != null) {
						pos = pos.up(2);
						pixie.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					}
				}
				return false;
			} else {
				int totalColors = EntityPixie.PARTICLE_RGB.length;
				int[] numberOfEachColor = new int[totalColors];
				int min = Integer.MAX_VALUE;
				for (int i = 0; i < missingPixies; i++) {
					ArrayList<Integer> colorsToSpawn = new ArrayList<>();
					//calculates the lowest number of any pixie color
					for (int i2 = 0; i2 < totalColors; i2++) {
						int color = i2;
						int count = (int) (pixies.stream().filter(e -> e.getColor() == color).count());
						numberOfEachColor[i2] = count;
						if (count < min) {
							min = count;
						}
					}
					//adds all pixie colors with the lowest number to a list
					for (int i3 = 0; i3 < totalColors; i3++) {
						if (numberOfEachColor[i3] == min) {
							colorsToSpawn.add(i3);
						}
					}
					BlockPos pos = BlockUtils.findNearbyFloorSpace(caster, 2, 4);
					if (pos == null) {
						return false;
					}
					pos = pos.up(2);
					EntityPixieMinion pixie = new EntityPixieMinion(world);
					pixie.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					pixie.setTamed(true);
					//picks a pixie from the lowest colors
					pixie.setColor(colorsToSpawn.get(world.rand.nextInt(colorsToSpawn.size())));
					pixie.setOwnerId(caster.getUniqueID());
					float statScale = this.getProperty(STAT_SCALE).floatValue();
					pixie.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(POTENCY_ATTRIBUTE_MODIFIER, (modifiers.amplified(SpellModifiers.POTENCY, 1.5f) - 1) * statScale, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
					pixie.setHealth(pixie.getMaxHealth());
					if (!world.isRemote) {
						world.spawnEntity(pixie);
					}
					pixies.add(pixie);
					pixieUUIDs.add(pixie.getUniqueID());
				}
			}
		}
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

}
