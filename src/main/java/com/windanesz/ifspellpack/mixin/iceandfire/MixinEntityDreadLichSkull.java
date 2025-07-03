package com.windanesz.ifspellpack.mixin.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityDreadLichSkull;
import electroblob.wizardry.entity.living.ISummonedCreature;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(EntityDreadLichSkull.class)
public class MixinEntityDreadLichSkull {

	//Ignore Intellij errors
	@ModifyVariable(method = "onUpdate()V", at = @At("STORE"))
	private List<EntityLivingBase> modifyList(List<EntityLivingBase> list) {
		EntityDreadLichSkull skull = (EntityDreadLichSkull)(Object)this;
		list.removeIf(e -> e instanceof ISummonedCreature && ((ISummonedCreature)e).getCaster() == skull.shootingEntity);
		return list;
	}

}
