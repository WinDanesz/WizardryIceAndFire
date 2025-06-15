package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.living.EntityTrollMinion;
import com.windanesz.ifspellpack.registry.IFSPItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TrollTroop extends SpellMinion<EntityTrollMinion> {

    private int trollVariant;

    public TrollTroop() {
        super(IFSpellPack.MODID, "troll_troop", EntityTrollMinion::new);
    }

    @Override
    public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
        trollVariant = world.rand.nextInt(EnumTroll.values().length);
        return super.cast(world, caster, hand, ticksInUse, modifiers);
    }

    @Override
    public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
        trollVariant = world.rand.nextInt(EnumTroll.values().length);
        return super.cast(world, caster, hand, ticksInUse, target, modifiers);
    }

    @Override
    public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers) {
        trollVariant = world.rand.nextInt(EnumTroll.values().length);
        return super.cast(world, x, y, z, direction, ticksInUse, duration, modifiers);
    }

    @Override
    protected void addMinionExtras(EntityTrollMinion minion, BlockPos pos, @Nullable EntityLivingBase caster, SpellModifiers modifiers, int alreadySpawned) {
        int i = EnumTroll.values().length;
        minion.setType(EnumTroll.values()[(alreadySpawned + trollVariant) % i]);
        minion.setWeaponType(EnumTroll.getWeaponForType(minion.getType()));
        if (caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)caster, IFSPItems.CHARM_STONEBREAKER_SIGIL)) {
            minion.setSunlightImmune(true);
        }
    }
}
