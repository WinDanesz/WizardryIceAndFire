package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.spell.SpellMinion;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

import java.util.function.Function;

public class SpellMinionIF<T extends EntityLiving & ISummonedCreature> extends SpellMinion<T> {

    public static String SUMMONING_COOLDOWN = "summoning_cooldown";

    public SpellMinionIF(String name, Function minionFactory) {
        this(IFSpellPack.MODID, name, minionFactory);
    }

    public SpellMinionIF(String modID, String name, Function<World, T> minionFactory) {
        super(modID, name, minionFactory);
        this.addProperties(SUMMONING_COOLDOWN);
    }
}
