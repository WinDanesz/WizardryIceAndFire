package com.windanesz.ifspellpack.client.renderer.entity.living;

import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonBanner;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonRider;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.windanesz.ifspellpack.client.renderer.entity.layer.LayerSkeletalDragonArmor;
import com.windanesz.ifspellpack.client.renderer.entity.layer.LayerSkeletalDragonEyes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSkeletalDragon extends RenderLiving<EntityDragonBase> {

    private final int dragonType;

    public RenderSkeletalDragon(RenderManager renderManager, ModelBase model, int dragonType) {
        super(renderManager, model, 0.15F);
        this.addLayer(new LayerSkeletalDragonEyes(this));
        this.addLayer(new LayerDragonRider(this, false));
        this.addLayer(new LayerDragonBanner(this));
        this.addLayer(new LayerSkeletalDragonArmor(this));
        this.dragonType = dragonType;
    }

    @Override
    public boolean shouldRender(EntityDragonBase dragon, ICamera camera, double camX, double camY, double camZ) {
        return super.shouldRender(dragon, camera, camX, camY, camZ) || dragon.shouldRender(camera) || Minecraft.getMinecraft().player.isRidingOrBeingRiddenBy(dragon);
    }

    @Override
    protected void preRenderCallback(EntityDragonBase entity, float f) {
        this.shadowSize = entity.getRenderSize() / 3;
        float f7 = entity.prevDragonPitch + (entity.getDragonPitch() - entity.prevDragonPitch) * f;
        GL11.glRotatef(f7, 1, 0, 0);
        GL11.glScalef(shadowSize, shadowSize, shadowSize);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDragonBase entity) {
        switch (dragonType) {
            case 0:
                switch (entity.getDragonStage()) {
                    case 1:
                        return new ResourceLocation("iceandfire:textures/models/firedragon/fire_skeleton_1.png");
                    case 2:
                        return new ResourceLocation("iceandfire:textures/models/firedragon/fire_skeleton_2.png");
                    case 3:
                        return new ResourceLocation("iceandfire:textures/models/firedragon/fire_skeleton_3.png");
                    case 4:
                        return new ResourceLocation("iceandfire:textures/models/firedragon/fire_skeleton_4.png");
                    default:
                        return new ResourceLocation("iceandfire:textures/models/firedragon/fire_skeleton_5.png");
                }
            case 1:
                switch (entity.getDragonStage()) {
                    case 1:
                        return new ResourceLocation("iceandfire:textures/models/icedragon/ice_skeleton_1.png");
                    case 2:
                        return new ResourceLocation("iceandfire:textures/models/icedragon/ice_skeleton_2.png");
                    case 3:
                        return new ResourceLocation("iceandfire:textures/models/icedragon/ice_skeleton_3.png");
                    case 4:
                        return new ResourceLocation("iceandfire:textures/models/icedragon/ice_skeleton_4.png");
                    default:
                        return new ResourceLocation("iceandfire:textures/models/icedragon/ice_skeleton_5.png");
                }
            default:
                switch (entity.getDragonStage()) {
                    case 1:
                        return new ResourceLocation("iceandfire:textures/models/lightningdragon/lightning_skeleton_1.png");
                    case 2:
                        return new ResourceLocation("iceandfire:textures/models/lightningdragon/lightning_skeleton_2.png");
                    case 3:
                        return new ResourceLocation("iceandfire:textures/models/lightningdragon/lightning_skeleton_3.png");
                    case 4:
                        return new ResourceLocation("iceandfire:textures/models/lightningdragon/lightning_skeleton_4.png");
                    default:
                        return new ResourceLocation("iceandfire:textures/models/lightningdragon/lightning_skeleton_5.png");
                }
        }
    }
}