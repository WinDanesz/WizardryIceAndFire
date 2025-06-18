package com.windanesz.ifspellpack.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.windanesz.ifspellpack.accessor.AccessorEntityTroll;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.pathfinding.PathNavigateGround;

public class TrollAIRestrictSun extends EntityAIBase
{
    private final EntityTroll troll;

    public TrollAIRestrictSun(EntityTroll troll)
    {
        this.troll = troll;
    }

    public boolean shouldExecute()
    {
        return this.troll.world.isDaytime() && !((AccessorEntityTroll)this.troll).ifspellpack$isSunlightImmune();
    }

    public void startExecuting()
    {
        ((PathNavigateGround)this.troll.getNavigator()).setAvoidSun(true);
    }

    public void resetTask()
    {
        ((PathNavigateGround)this.troll.getNavigator()).setAvoidSun(false);
    }
}