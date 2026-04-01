package com.windanesz.ifspellpack.entity.ai;

import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.packet.PacketNPCCastSpell;
import electroblob.wizardry.packet.WizardryPacketHandler;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;

public class EntityAIAttackHybrid<T extends EntityLiving & ISpellCaster> extends EntityAIBase {

	private final T attacker;
	private EntityLivingBase target;
	private int spellCooldown;
	private final int baseCooldown;
	private int continuousSpellTimer;
	private final int continuousSpellDuration;
	private final double speed;
	private int seeTime;
	private final float maxAttackDistance;
	private int meleeCooldown;

	public EntityAIAttackHybrid(T attacker, double speed, float maxDistance, int baseCooldown, int continuousSpellDuration) {
		this.spellCooldown = -1;
		this.attacker = attacker;
		this.baseCooldown = baseCooldown;
		this.continuousSpellDuration = continuousSpellDuration;
		this.speed = speed;
		this.maxAttackDistance = maxDistance * maxDistance;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
		if (entitylivingbase == null) {
			return false;
		} else {
			this.target = entitylivingbase;
			return true;
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute() || !this.attacker.getNavigator().noPath();
	}

	@Override
	public void resetTask() {
		this.target = null;
		this.seeTime = 0;
		this.spellCooldown = -1;
		this.setContinuousSpellAndNotify(Spells.none, new SpellModifiers());
		this.continuousSpellTimer = 0;
	}

	private void setContinuousSpellAndNotify(Spell spell, SpellModifiers modifiers) {
		attacker.setContinuousSpell(spell);
		WizardryPacketHandler.net.sendToAllAround(new PacketNPCCastSpell.Message(attacker.getEntityId(), target == null ? -1 : target.getEntityId(), EnumHand.MAIN_HAND, spell, modifiers), new NetworkRegistry.TargetPoint(attacker.dimension, attacker.posX, attacker.posY, attacker.posZ, 128));
	}

	@Override
	public void updateTask() {
		double distanceSq = this.attacker.getDistanceSq(this.target.posX, this.target.posY, this.target.posZ);
		boolean targetIsVisible = this.attacker.getEntitySenses().canSee(this.target);
		if (targetIsVisible) {
			++this.seeTime;
		} else {
			this.seeTime = 0;
		}
		if (distanceSq <= (double)this.maxAttackDistance && this.seeTime >= 20) {
			this.attacker.getNavigator().clearPath();
		} else {
			this.attacker.getNavigator().tryMoveToEntityLiving(this.target, this.speed);
		}
		this.attacker.getLookHelper().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
		if (this.continuousSpellTimer > 0){
			this.continuousSpellTimer--;
			if (distanceSq > (double)this.maxAttackDistance || !targetIsVisible || MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Tick(SpellCastEvent.Source.NPC, attacker.getContinuousSpell(), attacker, attacker.getModifiers(), this.continuousSpellDuration - this.continuousSpellTimer)) || !attacker.getContinuousSpell().cast(attacker.world, attacker, EnumHand.MAIN_HAND, this.continuousSpellDuration - this.continuousSpellTimer, target, attacker.getModifiers())	|| this.continuousSpellTimer == 0){
				this.continuousSpellTimer = 0;
				this.spellCooldown = attacker.getContinuousSpell().getCooldown() + this.baseCooldown;
				setContinuousSpellAndNotify(Spells.none, new SpellModifiers());
			} else if (this.continuousSpellDuration - this.continuousSpellTimer == 1) {
				MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(SpellCastEvent.Source.NPC, attacker.getContinuousSpell(),
						attacker, attacker.getModifiers()));
			}
		} else if (--this.spellCooldown == 0) {
			if (distanceSq > (double)this.maxAttackDistance || !targetIsVisible){
				return;
			}
			double dx = target.posX - attacker.posX;
			double dz = target.posZ - attacker.posZ;
			List<Spell> spells = new ArrayList<>(attacker.getSpells());
			if (!spells.isEmpty()) {
				if (!attacker.world.isRemote) {
					Spell spell;
					while (!spells.isEmpty()) {
						spell = spells.get(attacker.world.rand.nextInt(spells.size()));
						SpellModifiers modifiers = attacker.getModifiers();
						if (spell != null && attemptCastSpell(spell, modifiers)) {
							attacker.rotationYaw = (float)(Math.atan2(dz, dx) * 180.0D / Math.PI) - 90.0F;
							return;
						} else {
							spells.remove(spell);
						}
					}
				}
			}
		} else if (this.spellCooldown < 0) {
			this.spellCooldown = this.baseCooldown;
		}
	}

	private boolean attemptCastSpell(Spell spell, SpellModifiers modifiers) {
		// If anything stops the spell working at this point, nothing else happens.
		if (MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Pre(SpellCastEvent.Source.NPC, spell, attacker, modifiers))) {
			return false;
		}
		if (spell.cast(attacker.world, attacker, EnumHand.MAIN_HAND, 0, target, modifiers)) {
			if (spell.isContinuous) {
				this.continuousSpellTimer = this.continuousSpellDuration - 1;
				setContinuousSpellAndNotify(spell, modifiers);
			} else {
				MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(SpellCastEvent.Source.NPC, spell, attacker, modifiers));
				this.spellCooldown = this.baseCooldown + spell.getCooldown();
				if (spell.requiresPacket()) {
					IMessage msg = new PacketNPCCastSpell.Message(attacker.getEntityId(), target.getEntityId(),	EnumHand.MAIN_HAND, spell, modifiers);
					WizardryPacketHandler.net.sendToDimension(msg, attacker.world.provider.getDimension());
				}
			}
			return true;
		}
		return false;
	}

}
