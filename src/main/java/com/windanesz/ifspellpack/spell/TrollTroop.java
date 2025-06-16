package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.living.EntityTrollMinion;
import com.windanesz.ifspellpack.registry.IFSPItems;
import com.windanesz.wizardryutils.tools.WizardryUtilsTools;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.GeometryUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected boolean spawnMinions(World world, EntityLivingBase caster, SpellModifiers modifiers) {
        if (!world.isRemote) {
            boolean hasArtefact = caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer) caster, IFSPItems.CHARM_STONEBREAKER_SIGIL);
            for(int i = 0; i < this.getProperty("minion_count").intValue(); ++i) {
                int range = this.getProperty("summon_radius").intValue();
                BlockPos pos = findNearbyFloorSpace(caster, range, range * 2, hasArtefact);
                if (this.flying) {
                    if (pos != null) {
                        pos = pos.up(2);
                    } else {
                        pos = caster.getPosition().north(world.rand.nextInt(range * 2) - range).east(world.rand.nextInt(range * 2) - range);
                    }
                } else if (pos == null) {
                    WizardryUtilsTools.sendMessage(caster, "spell.ifspellpack:troll_troop.no_space", true);
                    return false;
                }

                EntityTrollMinion minion = this.createMinion(world, caster, modifiers);
                minion.setPosition((double)pos.getX() + (double)0.5F, (double)pos.getY(), (double)pos.getZ() + (double)0.5F);
                minion.setCaster(caster);
                minion.setLifetime((int)(this.getProperty("minion_lifetime").floatValue() * modifiers.get(WizardryItems.duration_upgrade)));
                IAttributeInstance attribute = minion.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
                if (attribute != null) {
                    attribute.applyModifier(new AttributeModifier("potency", (double)(modifiers.get("potency") - 1.0F), 2));
                }

                minion.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("minion_health", (double)(modifiers.get("minion_health") - 1.0F), 2));
                minion.setHealth(minion.getMaxHealth());
                this.addMinionExtras(minion, pos, caster, modifiers, i);
                world.spawnEntity(minion);
            }
        }

        return true;
    }

    @Nullable
    public static BlockPos findNearbyFloorSpace(Entity entity, int horizontalRange, int verticalRange, boolean hasArtefact) {
        World world = entity.world;
        BlockPos origin = new BlockPos(entity);
        return findNearbyFloorSpace(world, origin, horizontalRange, verticalRange, hasArtefact);
    }

    @Nullable
    public static BlockPos findNearbyFloorSpace(World world, BlockPos origin, int horizontalRange, int verticalRange, boolean hasArtefact) {
        return findNearbyFloorSpace(world, origin, horizontalRange, verticalRange, true, hasArtefact);
    }

    @Nullable
    public static BlockPos findNearbyFloorSpace(World world, BlockPos origin, int horizontalRange, int verticalRange, boolean lineOfSight, boolean hasArtefact) {
        List<BlockPos> possibleLocations = new ArrayList();
        Vec3d centre = GeometryUtils.getCentre(origin);

        for(int x = -horizontalRange; x <= horizontalRange; ++x) {
            for(int z = -horizontalRange; z <= horizontalRange; ++z) {
                Integer y = getNearestFloor(world, origin.add(x, 0, z), verticalRange);
                if (y != null) {
                    BlockPos location = new BlockPos(origin.getX() + x, y, origin.getZ() + z);
                    if (lineOfSight) {
                        RayTraceResult rayTrace = world.rayTraceBlocks(centre, GeometryUtils.getCentre(location), false, true, false);
                        if (rayTrace != null && rayTrace.typeOfHit == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
                            continue;
                        }
                    }

                    possibleLocations.add(location);
                }
            }
        }

        // only blocks with no sunlight exposure, or if it is night time or the troll is sunlight immune
        if (!hasArtefact && world.isDaytime()) {
            possibleLocations.removeIf(world::canSeeSky);
        }

        if (possibleLocations.isEmpty()) {
            return null;
        } else {
            return (BlockPos)possibleLocations.get(world.rand.nextInt(possibleLocations.size()));
        }
    }


    @Nullable
    public static Integer getNearestFloor(World world, BlockPos pos, int range) {
        return getNearestSurface(world, pos, EnumFacing.UP, range, true, BlockUtils.SurfaceCriteria.COLLIDABLE);
    }

    @Nullable
    public static Integer getNearestSurface(World world, BlockPos pos, EnumFacing direction, int range, boolean doubleSided, BlockUtils.SurfaceCriteria criteria) {
        Integer surface = null;
        int currentBest = Integer.MAX_VALUE;

        for(int i = doubleSided ? -range : 0; i <= range && i < currentBest; ++i) {
            BlockPos testPos = pos.offset(direction, i);
            if (criteria.test(world, testPos, direction)) {
                surface = (int)GeometryUtils.component(GeometryUtils.getFaceCentre(testPos, direction), direction.getAxis());
                currentBest = Math.abs(i);
            }
        }

        return surface;
    }

}
