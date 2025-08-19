package com.windanesz.ifspellpack.client.gui;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.inventory.ContainerDwarvenPocketForge;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiDwarvenPocketForge extends GuiContainer {

	public static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(IFSpellPack.MODID, "textures/gui/dwarven_pocket_forge.png");

	public GuiDwarvenPocketForge(IInventory inventory, EntityPlayer player) {
		super(new ContainerDwarvenPocketForge(player.inventory, inventory, player));
		this.xSize = 176;
		this.ySize = 223;
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GUI_BACKGROUND);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

}
