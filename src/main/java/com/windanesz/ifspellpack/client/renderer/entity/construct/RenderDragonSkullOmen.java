package com.windanesz.ifspellpack.client.renderer.entity.construct;

import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.construct.EntityDragonSkullOmen;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDragonSkullOmen extends RenderLiving<EntityDragonSkullOmen> {

    public static final float[] growth_stage_1 = new float[]{1F, 3F};
    public static final float[] growth_stage_2 = new float[]{3F, 7F};
    public static final float[] growth_stage_3 = new float[]{7F, 12.5F};
    public static final float[] growth_stage_4 = new float[]{12.5F, 20F};
    public static final float[] growth_stage_5 = new float[]{20F, 30F};
    public float[][] growth_stages;
    private final IceAndFireTabulaModel<? extends EntityDragonBase> fireDragonModel;
    private final IceAndFireTabulaModel<? extends EntityDragonBase> iceDragonModel;
    private final IceAndFireTabulaModel<? extends EntityDragonBase> lightningDragonModel;

    @SuppressWarnings("unchecked")
    public RenderDragonSkullOmen(RenderManager renderManager, ModelBase fireDragonModel, ModelBase iceDragonModel, ModelBase lightningDragonModel) {
        super(renderManager, null, 0);
        growth_stages = new float[][]{growth_stage_1, growth_stage_2, growth_stage_3, growth_stage_4, growth_stage_5};
        this.fireDragonModel = (IceAndFireTabulaModel<? extends EntityDragonBase>) fireDragonModel;
        this.iceDragonModel = (IceAndFireTabulaModel<? extends EntityDragonBase>) iceDragonModel;
        this.lightningDragonModel = (IceAndFireTabulaModel<? extends EntityDragonBase>) lightningDragonModel;
    }

    private static void setRotationAngles(ModelRenderer cube, float rotX, float rotY, float rotZ) {
        cube.rotateAngleX = rotX;
        cube.rotateAngleY = rotY;
        cube.rotateAngleZ = rotZ;
    }

    @Override
    public void doRender(EntityDragonSkullOmen skull, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(skull.getYaw(), 0, -1, 0);
        // float f = 0.0625F;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(skull);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(skull));
        }
        float size = getRenderSize(skull) / 3;
        GlStateManager.scale(size, size, size);
        GlStateManager.translate(0, skull.isOnWall() ? -0.24F : -0.12F, 0.5F);
        float rotx = skull.isOnWall() ? (float)Math.toRadians(50f) : 0f;
        if (skull.getType() == 0) {
            fireDragonModel.resetToDefaultPose();
            setRotationAngles(fireDragonModel.getCube("Head"), rotx, 0, 0);
            fireDragonModel.getCube("Head").render(0.0625F);
        }
        if (skull.getType() == 1) {
            iceDragonModel.resetToDefaultPose();
            setRotationAngles(iceDragonModel.getCube("Head"), rotx, 0, 0);
            iceDragonModel.getCube("Head").render(0.0625F);
        }
        if (skull.getType() == 2) {
            lightningDragonModel.resetToDefaultPose();
            setRotationAngles(lightningDragonModel.getCube("Head"), rotx, 0, 0);
            lightningDragonModel.getCube("Head").render(0.0625F);
        }
        //Start of eye layer
        boolean flag = this.setBrightness(skull, partialTicks, true);
        this.bindTexture(this.getEyeTexture(skull));
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(!skull.isInvisible());
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (skull.getType() == 0) {
            fireDragonModel.resetToDefaultPose();
            setRotationAngles(fireDragonModel.getCube("Head"), rotx, 0, 0);
            fireDragonModel.getCube("Head").render(0.0625F);
        }
        if (skull.getType() == 1) {
            iceDragonModel.resetToDefaultPose();
            setRotationAngles(iceDragonModel.getCube("Head"), rotx, 0, 0);
            iceDragonModel.getCube("Head").render(0.0625F);
        }
        if (skull.getType() == 2) {
            lightningDragonModel.resetToDefaultPose();
            setRotationAngles(lightningDragonModel.getCube("Head"), rotx, 0, 0);
            lightningDragonModel.getCube("Head").render(0.0625F);
        }
        this.setLightmap(skull);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        if (flag) {
            this.unsetBrightness();
        }
        //End of eye layer
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();
        if (!this.renderOutlines) {
            this.renderName(skull, x, y, z);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDragonSkullOmen entity) {
        if (entity.getType() == 0) {
            return EnumDragonTextures.getFireDragonSkullTextures(entity);
        } if (entity.getType() == 1) {
            return EnumDragonTextures.getIceDragonSkullTextures(entity);
        }
        return EnumDragonTextures.getLightningDragonSkullTextures(entity);
    }

    public ResourceLocation getEyeTexture(EntityDragonSkullOmen entity) {
        if (entity.getType() == 0) {
            return new ResourceLocation(IFSpellPack.MODID, "textures/entities/layers/fire_dragon_skull_omen_eyes.png");
        } if (entity.getType() == 1) {
            return new ResourceLocation(IFSpellPack.MODID, "textures/entities/layers/ice_dragon_skull_omen_eyes.png");
        }
        return new ResourceLocation(IFSpellPack.MODID, "textures/entities/layers/lightning_dragon_skull_omen_eyes.png");
    }

    public float getRenderSize(EntityDragonSkull skull) {
        float step = (growth_stages[skull.getDragonStage() - 1][1] - growth_stages[skull.getDragonStage() - 1][0]) / 25;
        if (skull.getDragonAge() > 125) {
            return growth_stages[skull.getDragonStage() - 1][0] + ((step * 25));
        }
        return growth_stages[skull.getDragonStage() - 1][0] + ((step * this.getAgeFactor(skull)));
    }

    private int getAgeFactor(EntityDragonSkull skull) {
        return (skull.getDragonStage() > 1 ? skull.getDragonAge() - (25 * (skull.getDragonStage() - 1)) : skull.getDragonAge());
    }

}
