package com.windanesz.ifspellpack.client.renderer.entity.living;

import com.windanesz.ifspellpack.entity.living.EntityAbstractSlayerMerchant;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.client.model.ModelWizard;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSlayerMerchant extends RenderBiped<EntityAbstractSlayerMerchant> {

	static final ResourceLocation[] TEXTURES = new ResourceLocation[6];

	public RenderSlayerMerchant(RenderManager renderManager){
		super(renderManager, new ModelWizard(), 0.5F);
		for(int i = 0; i < 6; i++){
			TEXTURES[i] = new ResourceLocation(Wizardry.MODID, "textures/entity/wizard/wizard_" + i + ".png");
		}
		// Just using the default without overriding models, since the armour sets its own model anyway.
		this.addLayer(new LayerBipedArmor(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAbstractSlayerMerchant merchant){
		return TEXTURES[0];
	}

}
