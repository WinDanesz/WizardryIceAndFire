package com.windanesz.ifspellpack.entity.living;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ifspellpack.entity.SlayerMerchantTrade;
import com.windanesz.ifspellpack.entity.SlayerMerchantTradeList;
import com.windanesz.ifspellpack.registry.IFSPItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Collections;

public class EntityArchivist extends EntityAbstractSlayerMerchant {

	public EntityArchivist(World worldIn) {
		super(worldIn);
	}

	@Override
	public SlayerMerchantTradeList initializeTrades() {
		SlayerMerchantTradeList trades = new SlayerMerchantTradeList();
		trades.add(new SlayerMerchantTrade(new ItemStack(IafItemRegistry.manuscript, 8 + this.getRNG().nextInt(5)), 5 + this.getRNG().nextInt(5)));
		trades.add(new SlayerMerchantTrade(new ItemStack(IFSPItems.CHARM_ENCHANTED_MANUSCRIPT, 1, IFSPItems.CHARM_ENCHANTED_MANUSCRIPT.getMaxDamage()), 50 + this.getRNG().nextInt(10)));
		Collections.shuffle(trades);
		return trades;
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityLivingBase.class, entity -> entity instanceof IMob, (float)this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue(), this.getMovementSpeed() * 1.2, this.getMovementSpeed() * 1.4));
	}
}
