package com.windanesz.ifspellpack.entity.living;

import com.windanesz.ifspellpack.registry.IFSPSpells;
import electroblob.wizardry.entity.living.EntityAIAttackSpell;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class EntityCultistMage extends EntityAbstractCultist implements ISpellCaster {

	private final EntityAIAttackSpell<EntityCultistMage> spellCastingAI = new EntityAIAttackSpell<>(this, this.getMovementSpeed(), 14.0F, 10, 50);

	private static final DataParameter<Integer> HEAL_COOLDOWN = EntityDataManager.createKey(EntityCultistMage.class, DataSerializers.VARINT);
	private static final DataParameter<String> CONTINUOUS_SPELL = EntityDataManager.createKey(EntityCultistMage.class, DataSerializers.STRING);
	private static final DataParameter<Integer> SPELL_COUNTER = EntityDataManager.createKey(EntityCultistMage.class, DataSerializers.VARINT);

	protected List<Spell> spells = new ArrayList<>();

	public EntityCultistMage(World worldIn) {
		super(worldIn);
		this.tasks.addTask(3, this.spellCastingAI);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(HEAL_COOLDOWN, -1);
		this.dataManager.register(CONTINUOUS_SPELL, "ebwizardry:none");
		this.dataManager.register(SPELL_COUNTER, 0);
	}

	public int getHealCooldown() {
		return this.dataManager.get(HEAL_COOLDOWN);
	}

	public void setHealCooldown(int cooldown) {
		this.dataManager.set(HEAL_COOLDOWN, cooldown);
	}

	@Override
	public void setContinuousSpell(Spell spell) {
		this.dataManager.set(CONTINUOUS_SPELL, spell.getRegistryName().toString());
	}

	@Nonnull
	@Override
	public Spell getContinuousSpell() {
		return Spell.get(this.dataManager.get(CONTINUOUS_SPELL));
	}

	@Override
	public void setSpellCounter(int count) {
		this.dataManager.set(SPELL_COUNTER, count);
	}

	@Override
	public int getSpellCounter() {
		return this.dataManager.get(SPELL_COUNTER);
	}

	@Nonnull
	@Override
	public List<Spell> getSpells() {
		return this.spells;
	}

	@Override
	public int getAimingError(EnumDifficulty difficulty){
		// Being more intelligent than skeletons, wizards are a little more accurate.
		switch(difficulty){
			case EASY: return 7;
			case NORMAL: return 4;
			case HARD: return 1;
			default: return 7; // Peaceful counts as easy
		}
	}

	@Override
	public void onLivingUpdate(){
		super.onLivingUpdate();
		int healCooldown = this.getHealCooldown();
		if (healCooldown == 0 && this.getHealth() < this.getMaxHealth() && this.getHealth() > 0 && !this.isPotionActive(WizardryPotions.arcane_jammer)) {
			this.heal(4);
			this.setHealCooldown(-1);
		} else if (healCooldown == -1 && this.deathTime == 0) {
			if (world.isRemote) {
				ParticleBuilder.spawnHealParticles(world, this);
			} else {
				if (this.getHealth() < 10) {
					this.setHealCooldown(150);
				} else {
					this.setHealCooldown(400);
				}
				this.playSound(Spells.heal.getSounds()[0], 0.7F, rand.nextFloat() * 0.4F + 1.0F);
			}
		}
		if(healCooldown > 0) {
			this.setHealCooldown(healCooldown - 1);
		}
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.spells.addAll(Arrays.asList(IFSPSpells.DRAGON_FIRE_BREATH, IFSPSpells.DRAGON_ICE_BREATH, IFSPSpells.DRAGON_LIGHTNING_BREATH, IFSPSpells.DRAGON_FIRE_CHARGE, IFSPSpells.DRAGON_ICE_CHARGE, IFSPSpells.DRAGON_LIGHTNING_CHARGE));
		this.setHealCooldown(50);
		return super.onInitialSpawn(difficulty, livingdata);
	}

}
