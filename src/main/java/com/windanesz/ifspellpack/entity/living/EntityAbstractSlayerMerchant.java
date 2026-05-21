package com.windanesz.ifspellpack.entity.living;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.client.IFSPGuiHandler;
import com.windanesz.ifspellpack.entity.ISlayerMerchant;
import com.windanesz.ifspellpack.entity.SlayerMerchantTrade;
import com.windanesz.ifspellpack.entity.SlayerMerchantTradeList;
import electroblob.wizardry.registry.WizardrySounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class EntityAbstractSlayerMerchant extends EntityCreature implements ISlayerMerchant, INpc {

	public static final String TRADE_RESET_TIME_KEY = "TradeResetTime";
	public static final String HAS_TRADES_ON_COOLDOWN_KEY = "HasTradesOnCooldown";
	public static final String TRADES_KEY = "Trades";
	@Nullable
	private SlayerMerchantTradeList trades;
	@Nullable
	private EntityPlayer customer;
	private long tradeResetTime;
	private boolean hasTradesOnCooldown;

	public EntityAbstractSlayerMerchant(World worldIn) {
		super(worldIn);
		this.setSize(0.6f, 1.8f);
	}

	@Override
	protected void initEntityAI(){
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAITradePlayer(this));
		this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
		this.tasks.addTask(4, new EntityAIRestrictOpenDoor(this));
		this.tasks.addTask(5, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(6, new EntityAIMoveTowardsRestriction(this, this.getAIMoveSpeed()));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(7, new EntityAIWander(this, this.getAIMoveSpeed()));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
	}

	@Override
	public boolean isOnSameTeam(Entity entityIn) {
		return super.isOnSameTeam(entityIn) || entityIn instanceof EntityAbstractSlayerMerchant;
	}

	public double getMovementSpeed() {
		return this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
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
		if (this.trades == null) {
			this.trades = this.initializeTrades();
		}
		return this.trades;
	}

	@Nullable
	@Override
	public void setTrades(SlayerMerchantTradeList trades) {
		this.trades = trades;
	}

	abstract public SlayerMerchantTradeList initializeTrades();

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		if(this.isEntityAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking() && this.getAttackTarget() != player){
			if(!this.world.isRemote){
				this.setCustomer(player);
				player.openGui(IFSpellPack.MODID, IFSPGuiHandler.SLAYER_TRADE, world, this.getEntityId(), 0, 0);
			}
			return true;
		}
		return super.processInteract(player, hand);
	}

	@Override
	public void purchaseItem(SlayerMerchantTrade recipe) {
		recipe.incrementCurrentTradeUses();
		this.livingSoundTime = -this.getTalkInterval();
		this.playSound(WizardrySounds.ENTITY_WIZARD_YES, this.getSoundVolume(), this.getSoundPitch());
		int i = 3 + this.rand.nextInt(4);
		if (!this.world.isRemote) {
			this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 0.5D, this.posZ, i));
		}
		if (recipe.isTradeDisabled()) {
			this.hasTradesOnCooldown = true;
			this.tradeResetTime = this.world.getTotalWorldTime() + IFSpellPack.settings.tradeResetTimer;
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
				this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0));
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

	public static class EntityAILookAtTradePlayer extends EntityAIWatchClosest {

		private final EntityAbstractSlayerMerchant merchant;

		public EntityAILookAtTradePlayer(EntityAbstractSlayerMerchant merchant) {
			super(merchant, EntityPlayer.class, 8.0F);
			this.merchant = merchant;
		}

		@Override
		public boolean shouldExecute() {
			if (this.merchant.isTrading()) {
				this.closestEntity = this.merchant.getCustomer();
				return true;
			} else {
				return false;
			}
		}
	}

	public static class EntityAITradePlayer extends EntityAIBase {

		private final EntityAbstractSlayerMerchant merchant;

		public EntityAITradePlayer(EntityAbstractSlayerMerchant merchant) {
			this.merchant = merchant;
			this.setMutexBits(5);
		}

		@Override
		public boolean shouldExecute(){
			if (!this.merchant.isEntityAlive()) {
				return false;
			} else if (this.merchant.isInWater()) {
				return false;
			} else if (!this.merchant.onGround) {
				return false;
			} else if (this.merchant.velocityChanged) {
				return false;
			} else {
				EntityPlayer entityplayer = this.merchant.getCustomer();
				if (entityplayer == null) {
					return false;
				} else if (this.merchant.getDistanceSq(entityplayer) > 16.0D) {
					return false;
				} else {
					return entityplayer.openContainer != null;
				}
			}
		}

		@Override
		public void startExecuting(){
			this.merchant.getNavigator().clearPath();
		}

		@Override
		public void resetTask(){
			this.merchant.setCustomer(null);
		}
	}

}
