package com.windanesz.ifspellpack.client.gui;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.network.C2SPacketSummonBeast;
import com.windanesz.ifspellpack.network.IFSPPacketHandler;
import com.windanesz.ifspellpack.spell.CallBeast;
import electroblob.wizardry.client.DrawingUtils;
import electroblob.wizardry.client.gui.GuiButtonInvisible;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiCallBeast extends GuiScreen  {

	private static final ResourceLocation TEXTURE = new ResourceLocation(IFSpellPack.MODID, "textures/gui/call_beast.png");
	protected int xSize = 96;
	protected int ySize = 32;
	protected int textureWidth = 128;
	protected int textureHeight = 64;
	protected int iconWidth = 32;
	protected int iconHeight = 32;
	boolean[] enabledIcons;
	GuiButton amphithereButton;
	GuiButton hippogryphButton;
	GuiButton hippocampusButton;

	public GuiCallBeast(boolean[] enabledIcons){
		super();
		if (enabledIcons.length != 3) {
			throw new IllegalArgumentException("Enabled Icons must be 3 elements long");
		}
		this.enabledIcons = enabledIcons;
	}

	public ResourceLocation getTexture(){
		return TEXTURE;
	}

	@Override
	public void initGui(){
		super.initGui();
		final int left = this.width / 2 - this.xSize / 2;
		final int top = this.height / 2 - this.ySize / 2;
		int i = 0;
		if (this.enabledIcons[i]) {
			this.buttonList.add(amphithereButton = new GuiOutline(i, left + i * this.iconWidth, top));
		}
		i++;
		if (this.enabledIcons[i]) {
			this.buttonList.add(hippogryphButton = new GuiOutline(i, left + i * this.iconWidth, top));
		}
		i++;
		if (this.enabledIcons[i]) {
			this.buttonList.add(hippocampusButton = new GuiOutline(i, left + i * this.iconWidth, top));
		}
	}

	@Override
	protected void actionPerformed(GuiButton button)  {
		int type = -1;
		if (button == amphithereButton) {
			type = CallBeast.AMPHITHERE;
		}
		if (button == hippogryphButton) {
			type = CallBeast.HIPPOGRYPH;
		}
		if (button == hippocampusButton) {
			type = CallBeast.HIPPOCAMPUS;
		}
		if (type != -1) {
			System.out.println(type);
			IFSPPacketHandler.net.sendToServer(new C2SPacketSummonBeast.Message(type));
		}
		Minecraft.getMinecraft().displayGuiScreen(null);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int left = this.width / 2 - this.xSize / 2;
		int top = this.height / 2 - this.ySize / 2;
		mc.renderEngine.bindTexture(getTexture());
		for (int i = 0; i < this.enabledIcons.length; i++) {
			int u = i * this.iconWidth;
			int v = 0;
			if (!this.enabledIcons[i]) {
				v += this.iconHeight;
			}
			DrawingUtils.drawTexturedRect(left + u, top, u, v, this.iconWidth, this.iconHeight, this.textureWidth, this.textureHeight);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	private class GuiOutline extends GuiButtonInvisible {

		public GuiOutline(int id, int x, int y){
			super(id, x, y, GuiCallBeast.this.iconWidth, GuiCallBeast.this.iconHeight);
		}

		@Override
		public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks){
			super.drawButton(minecraft, mouseX, mouseY, partialTicks);
			if(hovered){
				if (GuiCallBeast.this.enabledIcons[this.id]) {
					GuiCallBeast.this.mc.renderEngine.bindTexture(getTexture());
					DrawingUtils.drawTexturedRect(this.x, this.y, 96, 0, GuiCallBeast.this.iconWidth, GuiCallBeast.this.iconHeight, GuiCallBeast.this.textureWidth, GuiCallBeast.this.textureHeight);
				}
			}
		}

	}

}

