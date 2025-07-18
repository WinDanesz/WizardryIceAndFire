package com.windanesz.ifspellpack.spell;

import com.github.alexthe666.iceandfire.entity.ChainEntityProperties;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.network.IFSPPacketHandler;
import com.windanesz.ifspellpack.network.CPacketLiveWire;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.SpellModifiers;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class LiveWire extends Spell {

	public LiveWire() {
		super(IFSpellPack.MODID, "live_wire", SpellActions.POINT_UP, true);
		this.addProperties(DAMAGE);
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return true;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		float damage = this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		boolean result = false;
		List<Entity> entityList = EntityUtils.getEntitiesWithinRadius(10, caster.posX, caster.posY, caster.posZ, world, Entity.class);
		entityList.remove(caster);
		for (Entity entity : entityList) {
			ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(entity, ChainEntityProperties.class);
			if (chainProperties != null && chainProperties.isConnectedToEntity(entity, caster)) {
				result = true;
				if (ticksInUse % 10 == 0) {
					EntityUtils.attackEntityWithoutKnockback(entity, MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.SHOCK), damage);
				}
				//These Vec3d align with the chain render position. IAF ClientEvents says eye height should be multiplied by 0.75, but that is too high in the actual render.
				Vec3d center = new Vec3d(caster.posX, caster.posY + caster.getEyeHeight() * 0.5, caster.posZ);
				Vec3d targetVec = new Vec3d(entity.posX, entity.posY + entity.getEyeHeight() * 0.5, entity.posZ);
				Vec3d distance = targetVec.subtract(center);
				for (double d = 0; d < 5; d += world.rand.nextDouble() + 0.5) {
					Vec3d chainSpark = center.add(distance.scale(d / 5));
					IFSPPacketHandler.net.sendToAll(new CPacketLiveWire.Message(chainSpark));
				}
			}
		}
		return result;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		float damage = this.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		boolean result = false;
		List<Entity> entityList = EntityUtils.getEntitiesWithinRadius(10, caster.posX, caster.posY, caster.posZ, world, Entity.class);
		entityList.remove(caster);
		for (Entity entity : entityList) {
			ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(entity, ChainEntityProperties.class);
			if (chainProperties != null && chainProperties.isConnectedToEntity(entity, caster)) {
				result = true;
				if (ticksInUse % 10 == 0) {
					EntityUtils.attackEntityWithoutKnockback(entity, MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.SHOCK), damage);
				}
				//These Vec3d align with the chain render position. IAF ClientEvents says eye height should be multiplied by 0.75, but that is too high in the actual render.
				Vec3d center = new Vec3d(caster.posX, caster.posY + caster.getEyeHeight() * 0.5, caster.posZ);
				Vec3d targetVec = new Vec3d(entity.posX, entity.posY + entity.getEyeHeight() * 0.5, entity.posZ);
				Vec3d distance = targetVec.subtract(center);
				for (double d = 0; d < 5; d += world.rand.nextDouble() + 0.5) {
					Vec3d chainSpark = center.add(distance.scale(d / 5));
					IFSPPacketHandler.net.sendToAll(new CPacketLiveWire.Message(chainSpark));
				}
			}
		}
		return result;
	}
}
