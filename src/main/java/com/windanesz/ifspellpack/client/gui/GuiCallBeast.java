/*
package com.windanesz.ifspellpack.client.gui;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.client.DrawingUtils;
import electroblob.wizardry.client.gui.GuiButtonTurnPage;
import electroblob.wizardry.client.gui.GuiButtonTurnPage.Type;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.spell.Spell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiCallBeast extends GuiScreen  {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Wizardry.MODID, "textures/gui/container/lectern.png");
	*/
/** The distance of the page buttons from the bottom outside corners of the GUI. *//*

	private static final int PAGE_BUTTON_INSET_X = 22, PAGE_BUTTON_INSET_Y = 13;
	*/
/** The distance between adjacent page turn buttons. *//*

	private static final int PAGE_BUTTON_SPACING = 20;
	*/
/** The distance of the sort buttons from the top left corner of the GUI. *//*

	private static final int SORT_BUTTON_INSET_X = 96, SORT_BUTTON_INSET_Y = 20;
	*/
/** The distance between adjacent sort buttons. *//*

	private static final int SORT_BUTTON_SPACING = 13;
	*/
/** The distance of the spell buttons from the top outside corners of the GUI. *//*

	private static final int SPELL_BUTTON_INSET_X = 23, SPELL_BUTTON_INSET_Y = 44;
	*/
/** The distance between adjacent spell buttons (in both x and y). *//*

	private static final int SPELL_BUTTON_SPACING = 38;

	private static final int SPELL_ROWS = 3, SPELL_COLUMNS = 3;
	public static final int SPELL_BUTTON_COUNT = SPELL_ROWS * SPELL_COLUMNS * 2; // x2 because there are 2 pages

	public GuiCallBeast(){
		super();
	}

	public ResourceLocation getTexture(){
		return TEXTURE;
	}

	@Override
	public void initGui(){
		super.initGui();
		final int left = this.width / 2 - this.xSize / 2;
		final int top = this.height / 2 - this.ySize / 2;
		int buttonID = 0;
		// Page buttons
		this.buttonList.add(nextPageButton = new GuiButtonTurnPage(buttonID++, left + xSize - PAGE_BUTTON_INSET_X - GuiButtonTurnPage.WIDTH, top + ySize - PAGE_BUTTON_INSET_Y - GuiButtonTurnPage.HEIGHT, Type.NEXT_PAGE, TEXTURE, textureWidth, textureHeight));

		this.buttonList.add(prevPageButton = new GuiButtonTurnPage(buttonID++, left + PAGE_BUTTON_INSET_X, top + ySize - PAGE_BUTTON_INSET_Y - GuiButtonTurnPage.HEIGHT, Type.PREVIOUS_PAGE, TEXTURE, textureWidth, textureHeight));

		this.buttonList.add(lastPageButton = new GuiButtonTurnPage(buttonID++, left + xSize - PAGE_BUTTON_INSET_X - GuiButtonTurnPage.WIDTH - PAGE_BUTTON_SPACING, top + ySize - PAGE_BUTTON_INSET_Y - GuiButtonTurnPage.HEIGHT, Type.NEXT_SECTION, TEXTURE, textureWidth, textureHeight));

		this.buttonList.add(firstPageButton = new GuiButtonTurnPage(buttonID++, left + PAGE_BUTTON_INSET_X + PAGE_BUTTON_SPACING, top + ySize - PAGE_BUTTON_INSET_Y - GuiButtonTurnPage.HEIGHT, Type.PREVIOUS_SECTION, TEXTURE, textureWidth, textureHeight));

		this.buttonList.add(indexButton = new GuiButtonTurnPage(buttonID++, left + xSize/2 - 23, top + ySize - PAGE_BUTTON_INSET_Y - GuiButtonTurnPage.HEIGHT, Type.CONTENTS, TEXTURE, textureWidth, textureHeight));

	}

	@Override
	public void updateScreen(){
		super.updateScreen();
	}

	@Override
	public void onGuiClosed(){
		super.onGuiClosed();
	}

	private void drawIndexPage(int left, int top){

		for(int i = 0; i < SPELL_BUTTON_COUNT; i++){

			int index = currentPage * SPELL_BUTTON_COUNT + i;
			Spell spell = index < matchingSpells.size() ? matchingSpells.get(index) : Spells.none;
			boolean discovered = Wizardry.proxy.shouldDisplayDiscovered(spell, null);

			Minecraft.getMinecraft().renderEngine.bindTexture(discovered ? spell.getIcon() : Spells.none.getIcon());

			DrawingUtils.drawTexturedRect(left + x + 1, top + y + 1, 0, 0, 32, 32, 32, 32);
		}

		mc.renderEngine.bindTexture(getTexture());
		DrawingUtils.drawTexturedRect(left, top, 0, 256, xSize, ySize, textureWidth, textureHeight);

		GlStateManager.color(1, 1, 1, 1);
		mc.renderEngine.bindTexture(getTexture());

	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	   super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void actionPerformed(GuiButton button){

	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
	}

}
*/
