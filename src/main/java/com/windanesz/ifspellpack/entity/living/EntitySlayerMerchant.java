package com.windanesz.ifspellpack.entity.living;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.client.IFSPGuiHandler;
import com.windanesz.ifspellpack.entity.ISlayerMerchant;
import com.windanesz.ifspellpack.entity.SlayerMerchantTrade;
import com.windanesz.ifspellpack.entity.SlayerMerchantTradeList;
import com.windanesz.ifspellpack.registry.IFSPSchools;
import com.windanesz.ifspellpack.school.School;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.INpc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EntitySlayerMerchant extends EntityCreature implements ISlayerMerchant, INpc {

	public static final String TRADE_RESET_TIME_KEY = "TradeResetTime";
	public static final String HAS_TRADES_ON_COOLDOWN_KEY = "HasTradesOnCooldown";
	public static final String TRADES_KEY = "Trades";
	public static final int TRADE_RESET_TIMER = 200;
	//public static final int TRADE_RESET_TIMER = 12000;
	@Nullable
	private SlayerMerchantTradeList trades;
	@Nullable
	private EntityPlayer customer;
	private long tradeResetTime;
	private boolean hasTradesOnCooldown;

	public EntitySlayerMerchant(World worldIn) {
		super(worldIn);
		this.setTrades(this.initializeTrades());
	}

	@Override
	public void setCustomer(@Nullable EntityPlayer player) {
		this.customer = player;
	}

	@Nullable
	@Override
	public EntityPlayer getCustomer() {
		return this.customer;
	}

	public boolean isTrading() {
		return this.customer != null;
	}

	@Nullable
	@Override
	public SlayerMerchantTradeList getTrades() {
		return this.trades;
	}

	@Override
	public void setTrades(@Nullable SlayerMerchantTradeList recipes) {
		this.trades = recipes;
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		if(this.isEntityAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking() && this.getAttackTarget() != player){
			//if(!this.world.isRemote){
				this.setCustomer(player);
				player.openGui(IFSpellPack.MODID, IFSPGuiHandler.SLAYER_TRADE, world, this.getEntityId(), 0, 0);
			//}
			return true;
		}
		return super.processInteract(player, hand);
	}

	public SlayerMerchantTradeList initializeTrades() {
		SlayerMerchantTradeList trades = new SlayerMerchantTradeList();
		List<Spell> spells = School.getSpells(IFSPSchools.SLAYER);
		for (Spell spell : spells) {
			int cost = this.costForTier(spell.getTier());
			ItemStack book = new ItemStack(WizardryItems.spell_book, 1, spell.metadata());
			trades.add(new SlayerMerchantTrade(book, cost));
		}
		return trades;
	}

	public int costForTier(Tier tier) {
/*		if (tier == Tier.NOVICE) {
			return 5;
		}
		else if (tier == Tier.APPRENTICE) {
			return 10;
		}
		else if (tier == Tier.ADVANCED) {
			return 20;
		}
		else if (tier == Tier.MASTER) {
			return 50;
		}
		else {
			return 5;
		}*/
		return 0;
	}

	@Override
	public void purchaseItem(SlayerMerchantTrade recipe) {
		recipe.incrementCurrentTradeUses();
		this.livingSoundTime = -this.getTalkInterval();
		this.playSound(WizardrySounds.ENTITY_WIZARD_YES, this.getSoundVolume(), this.getSoundPitch());
		if (recipe.isTradeDisabled()) {
			this.hasTradesOnCooldown = true;
			this.tradeResetTime = this.world.getTotalWorldTime() + TRADE_RESET_TIMER;
		}
		//Trade achievement?
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.hasTradesOnCooldown) {
			if (this.world.getTotalWorldTime() >= this.tradeResetTime) {
				this.hasTradesOnCooldown = false;
				for (SlayerMerchantTrade trade : this.getTrades()) {
					//Reset all trades, even the ones not on cooldown
					trade.setCurrentTradeUses(0);
				}
				this.playSound(WizardrySounds.ENTITY_WIZARD_TRADING, this.getSoundVolume(), this.getSoundPitch());
			}
		}
	}

	@Override
	protected boolean canDespawn(){
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound(){
		return this.isTrading() ? WizardrySounds.ENTITY_WIZARD_TRADING : WizardrySounds.ENTITY_WIZARD_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source){
		return WizardrySounds.ENTITY_WIZARD_HURT;
	}

	@Override
	protected SoundEvent getDeathSound(){
		return WizardrySounds.ENTITY_WIZARD_DEATH;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setLong(TRADE_RESET_TIME_KEY, this.tradeResetTime);
		compound.setBoolean(HAS_TRADES_ON_COOLDOWN_KEY, this.hasTradesOnCooldown);
		if (this.trades != null) {
			compound.setTag(TRADES_KEY, this.trades.getRecipiesAsTags());
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.tradeResetTime = compound.getLong(TRADE_RESET_TIME_KEY);
		this.hasTradesOnCooldown = compound.getBoolean(HAS_TRADES_ON_COOLDOWN_KEY);
		if (compound.hasKey(TRADES_KEY, 10)) {
			NBTTagCompound nbttagcompound = compound.getCompoundTag(TRADES_KEY);
			this.trades = new SlayerMerchantTradeList(nbttagcompound);
		}
	}
}
