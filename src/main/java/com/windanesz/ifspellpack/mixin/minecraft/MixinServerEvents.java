package com.windanesz.ifspellpack.mixin.minecraft;

import com.github.alexthe666.iceandfire.entity.ChainEntityProperties;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.windanesz.ifspellpack.accessor.AccessorChainEntityProperties;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.List;

@Mixin(ServerEvents.class)
public abstract class MixinServerEvents {

	@Redirect(method = "onEntityDie(Lnet/minecraftforge/event/entity/living/LivingDeathEvent;)V", at = @At(value = "FIELD", target = "Lcom/github/alexthe666/iceandfire/entity/ChainEntityProperties;connectedEntities:Ljava/util/List;", opcode = Opcodes.GETFIELD))
	private List<Entity> redirectIsRemote(ChainEntityProperties instance) {
		if (!((AccessorChainEntityProperties)instance).ifspellpack$getDropsChain()) {
			instance.connectedEntities.clear();
		}
		return instance.connectedEntities;
	}

	@Redirect(method = "onEntityInteract(Lnet/minecraftforge/event/entity/player/PlayerInteractEvent$EntityInteractSpecific;)V", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/ChainEntityProperties;isChained()Z"))
	private boolean redirectIsChained(ChainEntityProperties instance) {
		if (!((AccessorChainEntityProperties)instance).ifspellpack$getDropsChain()) {
			return false;
		}
		return instance.isChained();
	}

}
