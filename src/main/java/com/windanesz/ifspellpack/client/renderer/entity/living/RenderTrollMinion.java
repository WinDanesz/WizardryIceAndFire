package com.windanesz.ifspellpack.client.renderer.entity.living;

import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.client.render.entity.ICustomStoneLayer;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerStoneEntityCrack;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollStone;
import com.windanesz.ifspellpack.client.renderer.entity.layer.LayerTrollMinionEyes;
import com.windanesz.ifspellpack.client.renderer.entity.layer.LayerTrollMinionWeapon;
import com.windanesz.ifspellpack.entity.living.EntityTrollMinion;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTrollMinion extends RenderLiving<EntityTrollMinion> implements ICustomStoneLayer {

    public RenderTrollMinion(RenderManager renderManager) {
        super(renderManager, new ModelTroll(), 0.9F);
        this.layerRenderers.add(new LayerTrollMinionWeapon(this));
        this.layerRenderers.add(new LayerTrollMinionEyes(this));
    }

    @Override
    public void preRenderCallback(EntityTrollMinion entitylivingbaseIn, float partialTickTime) {
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTrollMinion troll) {
        return troll.getType().TEXTURE;
    }

    @Override
    public LayerRenderer<EntityLivingBase> getStoneLayer(RenderLivingBase<? extends EntityLivingBase> render) {
        return new LayerTrollStone(render);
    }

    @Override
    public LayerRenderer<EntityLivingBase> getCrackLayer(RenderLivingBase<? extends EntityLivingBase> render) {
        return new LayerStoneEntityCrack(render);
    }
}
