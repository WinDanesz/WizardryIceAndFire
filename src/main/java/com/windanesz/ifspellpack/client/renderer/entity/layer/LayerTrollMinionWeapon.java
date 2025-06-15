package com.windanesz.ifspellpack.client.renderer.entity.layer;

import com.github.alexthe666.iceandfire.client.render.entity.RenderTroll;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.windanesz.ifspellpack.client.renderer.entity.living.RenderTrollMinion;
import com.windanesz.ifspellpack.entity.living.EntityTrollMinion;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerTrollMinionWeapon implements LayerRenderer<EntityTrollMinion> {
    private final RenderTrollMinion renderer;

    public LayerTrollMinionWeapon(RenderTrollMinion renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityTrollMinion entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
        if (entity.getWeaponType() != null && !EntityGorgon.isStoneMob(entity)) {
            this.renderer.bindTexture(entity.getWeaponType().TEXTURE);
            this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}