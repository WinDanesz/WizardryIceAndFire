package com.windanesz.ifspellpack.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModeFireDragonRobes extends ModelBase {

    public ModelRenderer HornL;
    public ModelRenderer HornR;
    public ModelRenderer HornL3;
    public ModelRenderer HornR3;
    public ModelRenderer HeadFront;
    public ModelRenderer Jaw;
    public ModelRenderer HornL2;
    public ModelRenderer HornR2;
    public ModelRenderer Teeth1;
    public ModelRenderer Teeth2;
    public ModelRenderer RightShoulderSpike1;
    public ModelRenderer RightShoulderSpike2;
    public ModelRenderer LeftLegSpike;
    public ModelRenderer LeftLegSpike2;
    public ModelRenderer LeftLegSpike3;
    public ModelRenderer BackSpike1;
    public ModelRenderer BackSpike2;
    public ModelRenderer BackSpike3;
    public ModelRenderer LeftShoulderSpike1;
    public ModelRenderer LeftShoulderSpike2;
    public ModelRenderer RightLegSpike;
    public ModelRenderer RightLegSpike2;
    public ModelRenderer RightLegSpike3;
    public ModelRenderer bipedHead;
    public ModelRenderer bipedHeadwear;
    public ModelRenderer bipedBody;
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public ModeFireDragonRobes.ArmPose leftArmPose;
    public ModeFireDragonRobes.ArmPose rightArmPose;
    public boolean isSneak;

    public ModeFireDragonRobes(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
        this.leftArmPose = ModeFireDragonRobes.ArmPose.EMPTY;
        this.rightArmPose = ModeFireDragonRobes.ArmPose.EMPTY;
        this.textureWidth = textureWidthIn;
        this.textureHeight = textureHeightIn;
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
        this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);this.textureWidth = 64;
        this.textureHeight = 64;
        this.RightLegSpike3 = new ModelRenderer(this, 0, 34);
        this.RightLegSpike3.setRotationPoint(-0.8F, 0.0F, -0.8F);
        this.RightLegSpike3.addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.setRotateAngle(RightLegSpike3, -1.2217304763960306F, 1.2217304763960306F, -0.17453292519943295F);
        this.LeftShoulderSpike2 = new ModelRenderer(this, 0, 34);
        this.LeftShoulderSpike2.setRotationPoint(1.8F, -0.1F, 0.0F);
        this.LeftShoulderSpike2.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(LeftShoulderSpike2, -3.141592653589793F, 0.0F, 0.2617993877991494F);
        this.HornL2 = new ModelRenderer(this, 46, 36);
        this.HornL2.mirror = true;
        this.HornL2.setRotationPoint(0.0F, 0.3F, 4.5F);
        this.HornL2.addBox(-0.5F, -0.8F, -0.0F, 1, 2, 5, 0.0F);
        this.setRotateAngle(HornL2, -0.07504915783575616F, 0.0F, 0.0F);
        this.RightLegSpike = new ModelRenderer(this, 0, 34);
        this.RightLegSpike.setRotationPoint(0.0F, 5.0F, 0.4F);
        this.RightLegSpike.addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.setRotateAngle(RightLegSpike, -1.4114477660878142F, 0.0F, 0.0F);
        this.HeadFront = new ModelRenderer(this, 6, 44);
        this.HeadFront.setRotationPoint(0.0F, -5.6F, 0.0F);
        this.HeadFront.addBox(-3.5F, -2.8F, -8.8F, 7, 2, 5, 0.0F);
        this.setRotateAngle(HeadFront, 0.045553093477052F, -0.0F, 0.0F);
        this.HornL3 = new ModelRenderer(this, 46, 36);
        this.HornL3.mirror = true;
        this.HornL3.setRotationPoint(4.0F, -4.0F, 0.7F);
        this.HornL3.addBox(-0.5F, -0.8F, -0.0F, 1, 2, 5, 0.0F);
        this.setRotateAngle(HornL3, -0.06981317007977318F, 0.4886921905584123F, 0.08726646259971647F);
        this.LeftLegSpike = new ModelRenderer(this, 0, 34);
        this.LeftLegSpike.setRotationPoint(0.0F, 5.0F, 0.4F);
        this.LeftLegSpike.addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.setRotateAngle(LeftLegSpike, -1.4114477660878142F, 0.0F, 0.0F);
        this.RightShoulderSpike1 = new ModelRenderer(this, 0, 34);
        this.RightShoulderSpike1.setRotationPoint(-0.5F, -1.2F, 0.0F);
        this.RightShoulderSpike1.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(RightShoulderSpike1, -3.141592653589793F, 0.0F, -0.17453292519943295F);
        this.HornL = new ModelRenderer(this, 48, 44);
        this.HornL.mirror = true;
        this.HornL.setRotationPoint(3.6F, -8.0F, 1.0F);
        this.HornL.addBox(-1.0F, -0.5F, 0.0F, 2, 3, 5, 0.0F);
        this.setRotateAngle(HornL, 0.3141592653589793F, 0.33161255787892263F, 0.19198621771937624F);
        this.HornR = new ModelRenderer(this, 48, 44);
        this.HornR.setRotationPoint(-3.6F, -8.0F, 1.0F);
        this.HornR.addBox(-1.0F, -0.5F, 0.0F, 2, 3, 5, 0.0F);
        this.setRotateAngle(HornR, 0.3141592653589793F, -0.33161255787892263F, -0.19198621771937624F);
        this.RightShoulderSpike2 = new ModelRenderer(this, 0, 34);
        this.RightShoulderSpike2.setRotationPoint(-1.8F, -0.1F, 0.0F);
        this.RightShoulderSpike2.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(RightShoulderSpike2, -3.141592653589793F, 0.0F, -0.2617993877991494F);
        this.Teeth2 = new ModelRenderer(this, 6, 34);
        this.Teeth2.mirror = true;
        this.Teeth2.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.Teeth2.addBox(-0.4F, 0.1F, -8.9F, 4, 1, 5, 0.0F);
        this.HornR3 = new ModelRenderer(this, 46, 36);
        this.HornR3.mirror = true;
        this.HornR3.setRotationPoint(-4.0F, -4.0F, 0.7F);
        this.HornR3.addBox(-0.5F, -0.8F, -0.0F, 1, 2, 5, 0.0F);
        this.setRotateAngle(HornR3, -0.06981317007977318F, -0.4886921905584123F, -0.08726646259971647F);
        this.BackSpike2 = new ModelRenderer(this, 0, 34);
        this.BackSpike2.setRotationPoint(0.0F, 3.5F, 0.6F);
        this.BackSpike2.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(BackSpike2, 1.1838568316277536F, 0.0F, 0.0F);
        this.LeftLegSpike2 = new ModelRenderer(this, 0, 34);
        this.LeftLegSpike2.setRotationPoint(0.7F, 3.6F, -0.4F);
        this.LeftLegSpike2.addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.setRotateAngle(LeftLegSpike2, -1.4114477660878142F, 0.0F, 0.0F);
        this.BackSpike1 = new ModelRenderer(this, 0, 34);
        this.BackSpike1.setRotationPoint(0.0F, 0.9F, 0.2F);
        this.BackSpike1.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(BackSpike1, 1.1838568316277536F, 0.0F, 0.0F);
        this.RightLegSpike2 = new ModelRenderer(this, 0, 34);
        this.RightLegSpike2.setRotationPoint(-0.7F, 3.6F, -0.4F);
        this.RightLegSpike2.addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.setRotateAngle(RightLegSpike2, -1.4114477660878142F, 0.0F, 0.0F);
        this.BackSpike3 = new ModelRenderer(this, 0, 34);
        this.BackSpike3.setRotationPoint(0.0F, 6.4F, 0.0F);
        this.BackSpike3.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(BackSpike3, 1.1838568316277536F, 0.0F, 0.0F);
        this.LeftShoulderSpike1 = new ModelRenderer(this, 0, 34);
        this.LeftShoulderSpike1.setRotationPoint(0.5F, -1.2F, 0.0F);
        this.LeftShoulderSpike1.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(LeftShoulderSpike1, -3.141592653589793F, 0.0F, 0.17453292519943295F);
        this.Jaw = new ModelRenderer(this, 6, 51);
        this.Jaw.setRotationPoint(0.0F, -5.4F, 0.0F);
        this.Jaw.addBox(-3.5F, 4.0F, -7.4F, 7, 2, 5, 0.0F);
        this.setRotateAngle(Jaw, -0.091106186954104F, -0.0F, 0.0F);
        this.LeftLegSpike3 = new ModelRenderer(this, 0, 34);
        this.LeftLegSpike3.setRotationPoint(0.8F, -0.0F, -0.8F);
        this.LeftLegSpike3.addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.setRotateAngle(LeftLegSpike3, -1.2217304763960306F, -1.2217304763960306F, 0.17453292519943295F);
        this.HornR2 = new ModelRenderer(this, 46, 36);
        this.HornR2.mirror = true;
        this.HornR2.setRotationPoint(0.0F, 0.3F, 4.5F);
        this.HornR2.addBox(-0.5F, -0.8F, -0.0F, 1, 2, 5, 0.0F);
        this.setRotateAngle(HornR2, -0.07504915783575616F, 0.0F, 0.0F);
        this.Teeth1 = new ModelRenderer(this, 6, 34);
        this.Teeth1.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.Teeth1.addBox(-3.6F, 0.1F, -8.9F, 4, 1, 5, 0.0F);
        //Legs
        this.bipedLeftLeg.addChild(this.LeftLegSpike3);
        this.bipedLeftLeg.addChild(this.LeftLegSpike2);
        this.bipedLeftLeg.addChild(this.LeftLegSpike);
        this.bipedRightLeg.addChild(this.RightLegSpike3);
        this.bipedRightLeg.addChild(this.RightLegSpike2);
        this.bipedRightLeg.addChild(this.RightLegSpike);
        //Body
        this.bipedLeftArm.addChild(this.LeftShoulderSpike2);
        this.HornL.addChild(this.HornL2);
        this.bipedHead.addChild(this.HeadFront);
        this.bipedHead.addChild(this.HornL3);
        this.bipedRightArm.addChild(this.RightShoulderSpike1);
        this.bipedHead.addChild(this.HornL);
        this.bipedHead.addChild(this.HornR);
        this.bipedRightArm.addChild(this.RightShoulderSpike2);
        this.HeadFront.addChild(this.Teeth2);
        this.bipedHead.addChild(this.HornR3);
        this.bipedBody.addChild(this.BackSpike2);
        this.bipedBody.addChild(this.BackSpike1);
        this.bipedBody.addChild(this.BackSpike3);
        this.bipedLeftArm.addChild(this.LeftShoulderSpike1);
        this.bipedHead.addChild(this.Jaw);
        this.HornR.addChild(this.HornR2);
        this.HeadFront.addChild(this.Teeth1);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        if (entityIn instanceof EntityArmorStand) {
            EntityArmorStand entityarmorstand = (EntityArmorStand) entityIn;
            this.bipedHead.rotateAngleX = 0.017453292F * entityarmorstand.getHeadRotation().getX();
            this.bipedHead.rotateAngleY = 0.017453292F * entityarmorstand.getHeadRotation().getY();
            this.bipedHead.rotateAngleZ = 0.017453292F * entityarmorstand.getHeadRotation().getZ();
            this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
            this.bipedBody.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
            this.bipedBody.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
            this.bipedBody.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
            this.bipedLeftArm.rotateAngleX = 0.017453292F * entityarmorstand.getLeftArmRotation().getX();
            this.bipedLeftArm.rotateAngleY = 0.017453292F * entityarmorstand.getLeftArmRotation().getY();
            this.bipedLeftArm.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftArmRotation().getZ();
            this.bipedRightArm.rotateAngleX = 0.017453292F * entityarmorstand.getRightArmRotation().getX();
            this.bipedRightArm.rotateAngleY = 0.017453292F * entityarmorstand.getRightArmRotation().getY();
            this.bipedRightArm.rotateAngleZ = 0.017453292F * entityarmorstand.getRightArmRotation().getZ();
            this.bipedLeftLeg.rotateAngleX = 0.017453292F * entityarmorstand.getLeftLegRotation().getX();
            this.bipedLeftLeg.rotateAngleY = 0.017453292F * entityarmorstand.getLeftLegRotation().getY();
            this.bipedLeftLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftLegRotation().getZ();
            this.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
            this.bipedRightLeg.rotateAngleX = 0.017453292F * entityarmorstand.getRightLegRotation().getX();
            this.bipedRightLeg.rotateAngleY = 0.017453292F * entityarmorstand.getRightLegRotation().getY();
            this.bipedRightLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getRightLegRotation().getZ();
            this.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
            copyModelAngles(this.bipedHead, this.bipedHeadwear);
        } else {
            boolean flag = entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getTicksElytraFlying() > 4;
            this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;
            if (flag) {
                this.bipedHead.rotateAngleX = -((float)Math.PI / 4F);
            }
            else {
                this.bipedHead.rotateAngleX = headPitch * 0.017453292F;
            }
            this.bipedBody.rotateAngleY = 0.0F;
            this.bipedRightArm.rotationPointZ = 0.0F;
            this.bipedRightArm.rotationPointX = -5.0F;
            this.bipedLeftArm.rotationPointZ = 0.0F;
            this.bipedLeftArm.rotationPointX = 5.0F;
            float f = 1.0F;
            if (flag) {
                f = (float)(entityIn.motionX * entityIn.motionX + entityIn.motionY * entityIn.motionY + entityIn.motionZ * entityIn.motionZ);
                f = f / 0.2F;
                f = f * f * f;
            }
            if (f < 1.0F) {
                f = 1.0F;
            }
            this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
            this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
            this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount / f;
            this.bipedRightLeg.rotateAngleY = 0.0F;
            this.bipedLeftLeg.rotateAngleY = 0.0F;
            this.bipedRightLeg.rotateAngleZ = 0.0F;
            this.bipedLeftLeg.rotateAngleZ = 0.0F;
            if (this.isRiding)
            {
                this.bipedRightArm.rotateAngleX += -((float)Math.PI / 5F);
                this.bipedLeftArm.rotateAngleX += -((float)Math.PI / 5F);
                this.bipedRightLeg.rotateAngleX = -1.4137167F;
                this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
                this.bipedRightLeg.rotateAngleZ = 0.07853982F;
                this.bipedLeftLeg.rotateAngleX = -1.4137167F;
                this.bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10F);
                this.bipedLeftLeg.rotateAngleZ = -0.07853982F;
            }
            this.bipedRightArm.rotateAngleY = 0.0F;
            this.bipedRightArm.rotateAngleZ = 0.0F;
            switch (this.leftArmPose) {
                case EMPTY:
                    this.bipedLeftArm.rotateAngleY = 0.0F;
                    break;
                case BLOCK:
                    this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - 0.9424779F;
                    this.bipedLeftArm.rotateAngleY = 0.5235988F;
                    break;
                case ITEM:
                    this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F);
                    this.bipedLeftArm.rotateAngleY = 0.0F;
            }
            switch (this.rightArmPose) {
                case EMPTY:
                    this.bipedRightArm.rotateAngleY = 0.0F;
                    break;
                case BLOCK:
                    this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.9424779F;
                    this.bipedRightArm.rotateAngleY = -0.5235988F;
                    break;
                case ITEM:
                    this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F);
                    this.bipedRightArm.rotateAngleY = 0.0F;
            }
            if (this.swingProgress > 0.0F) {
                EnumHandSide enumhandside = this.getMainHand(entityIn);
                ModelRenderer modelrenderer = this.getArmForSide(enumhandside);
                float f1 = this.swingProgress;
                this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f1) * ((float)Math.PI * 2F)) * 0.2F;
                if (enumhandside == EnumHandSide.LEFT) {
                    this.bipedBody.rotateAngleY *= -1.0F;
                }
                this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
                this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
                this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
                this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
                f1 = 1.0F - this.swingProgress;
                f1 = f1 * f1;
                f1 = f1 * f1;
                f1 = 1.0F - f1;
                float f2 = MathHelper.sin(f1 * (float)Math.PI);
                float f3 = MathHelper.sin(this.swingProgress * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
                modelrenderer.rotateAngleX = (float)((double)modelrenderer.rotateAngleX - ((double)f2 * 1.2D + (double)f3));
                modelrenderer.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
                modelrenderer.rotateAngleZ += MathHelper.sin(this.swingProgress * (float)Math.PI) * -0.4F;
            }
            if (this.isSneak) {
                this.bipedBody.rotateAngleX = 0.5F;
                this.bipedRightArm.rotateAngleX += 0.4F;
                this.bipedLeftArm.rotateAngleX += 0.4F;
                this.bipedRightLeg.rotationPointZ = 4.0F;
                this.bipedLeftLeg.rotationPointZ = 4.0F;
                this.bipedRightLeg.rotationPointY = 9.0F;
                this.bipedLeftLeg.rotationPointY = 9.0F;
                this.bipedHead.rotationPointY = 1.0F;
            } else {
                this.bipedBody.rotateAngleX = 0.0F;
                this.bipedRightLeg.rotationPointZ = 0.1F;
                this.bipedLeftLeg.rotationPointZ = 0.1F;
                this.bipedRightLeg.rotationPointY = 12.0F;
                this.bipedLeftLeg.rotationPointY = 12.0F;
                this.bipedHead.rotationPointY = 0.0F;
            }
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
            if (this.rightArmPose == ModeFireDragonRobes.ArmPose.BOW_AND_ARROW) {
                this.bipedRightArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY;
                this.bipedLeftArm.rotateAngleY = 0.1F + this.bipedHead.rotateAngleY + 0.4F;
                this.bipedRightArm.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
                this.bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
            }
            else if (this.leftArmPose == ModeFireDragonRobes.ArmPose.BOW_AND_ARROW) {
                this.bipedRightArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY - 0.4F;
                this.bipedLeftArm.rotateAngleY = 0.1F + this.bipedHead.rotateAngleY;
                this.bipedRightArm.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
                this.bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
            }
            copyModelAngles(this.bipedHead, this.bipedHeadwear);
        }
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.pushMatrix();
        if (this.isChild) {
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            this.bipedHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
		}
        else {
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            this.bipedHead.render(scale);
		}
		this.bipedBody.render(scale);
		this.bipedRightArm.render(scale);
		this.bipedLeftArm.render(scale);
		this.bipedRightLeg.render(scale);
		this.bipedLeftLeg.render(scale);
		this.bipedHeadwear.render(scale);
		GlStateManager.popMatrix();
    }


    public void setModelAttributes(ModelBase model) {
        super.setModelAttributes(model);
        if (model instanceof ModeFireDragonRobes) {
            ModeFireDragonRobes modelbiped = (ModeFireDragonRobes)model;
            this.leftArmPose = modelbiped.leftArmPose;
            this.rightArmPose = modelbiped.rightArmPose;
            this.isSneak = modelbiped.isSneak;
        }
    }

    public void setVisible(boolean visible) {
        this.bipedHead.showModel = visible;
        this.bipedHeadwear.showModel = visible;
        this.bipedBody.showModel = visible;
        this.bipedRightArm.showModel = visible;
        this.bipedLeftArm.showModel = visible;
        this.bipedRightLeg.showModel = visible;
        this.bipedLeftLeg.showModel = visible;
    }

    public void postRenderArm(float scale, EnumHandSide side) {
        this.getArmForSide(side).postRender(scale);
    }

    protected ModelRenderer getArmForSide(EnumHandSide side) {
        return side == EnumHandSide.LEFT ? this.bipedLeftArm : this.bipedRightArm;
    }

    protected EnumHandSide getMainHand(Entity entityIn) {
        if (entityIn instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
            EnumHandSide enumhandside = entitylivingbase.getPrimaryHand();
            return entitylivingbase.swingingHand == EnumHand.MAIN_HAND ? enumhandside : enumhandside.opposite();
        }
        else {
            return EnumHandSide.RIGHT;
        }
    }

    @SideOnly(Side.CLIENT)
    public static enum ArmPose {
        EMPTY,
        ITEM,
        BLOCK,
        BOW_AND_ARROW;
    }

}