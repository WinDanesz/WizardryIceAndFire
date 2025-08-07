package com.windanesz.ifspellpack.item;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.windanesz.ifspellpack.spell.SpellCone;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemCharmVolatileResin extends ItemChargedArtefact {

	public static final double RADIUS = 3;
	public static final int DURATION = 200;

	public ItemCharmVolatileResin() {
		super(EnumRarity.EPIC, ItemArtefact.Type.CHARM, 10000, Arrays.asList(IafItemRegistry.myrmex_desert_resin, IafItemRegistry.myrmex_jungle_resin), 100, 100);
	}

	public static <T extends EntityMyrmexBase & ISummonedCreature> void explode(T myrmex) {
		World world = myrmex.world;
		boolean jungle = myrmex.isJungle();
		double radius = myrmex.width * RADIUS;
		List<EntityLivingBase> targets = EntityUtils.getLivingWithinRadius(radius, myrmex.posX, myrmex.posY, myrmex.posZ, myrmex.world);
		targets.removeIf(e -> e == myrmex || !myrmex.isValidTarget(e) || !SpellCone.crossesAABB(world, myrmex.getPositionVector(), e.getEntityBoundingBox()));
		for (EntityLivingBase target : targets) {
			if (jungle) {
				target.addPotionEffect(new PotionEffect(MobEffects.WITHER, DURATION, 1));
			} else {
				target.setFire(DURATION / 20);
			}
		}
		if (world.isRemote) {
			ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(myrmex.getPositionVector()).scale((float)radius).clr(0, 0, 0).spawn(world);
			world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, myrmex.posX, myrmex.posY, myrmex.posZ, 0, 0, 0);
			int color = jungle ? 0x352a27 : 0xFF6D00;
			for(int i = 0; i < radius * 20; i++){
				ParticleBuilder.create(ParticleBuilder.Type.CLOUD, world.rand, myrmex.posX, myrmex.posY, myrmex.posZ, radius / 2, false).clr(color).time(20 + world.rand.nextInt(10))/*.shaded(true)*/.spawn(world);
			}
		}
		myrmex.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 2f, 1f);
	}

}
