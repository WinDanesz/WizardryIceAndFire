package com.windanesz.ifspellpack.client.gui;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.entity.ISlayerMerchant;
import com.windanesz.ifspellpack.entity.SlayerMerchantTrade;
import com.windanesz.ifspellpack.entity.SlayerMerchantTradeList;
import com.windanesz.ifspellpack.inventory.ContainerSlayerMerchant;
import com.windanesz.ifspellpack.network.C2SPacketGuiSlayerMerchantReset;
import com.windanesz.ifspellpack.network.C2SPacketGuiSlayerMerchantSetIndex;
import com.windanesz.ifspellpack.registry.IFSPPackets;
import com.windanesz.ifspellpack.world.SlayerTracker;
import electroblob.wizardry.data.WizardData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSlayerMerchant extends GuiContainer {

    private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation(IFSpellPack.MODID, "textures/gui/merchant_trade.png");
    private final ISlayerMerchant merchant;
    private GuiSlayerMerchant.MerchantButton nextButton;
    private GuiSlayerMerchant.MerchantButton previousButton;
    private int selectedTrade;
    private final ITextComponent chatComponent;

    public GuiSlayerMerchant(InventoryPlayer playerInventoryIn, ISlayerMerchant merchantIn) {
        super(new ContainerSlayerMerchant(playerInventoryIn, merchantIn));
        this.merchant = merchantIn;
        this.chatComponent = merchantIn.getDisplayName();
    }

    public void initGui() {
        super.initGui();
        this.nextButton = this.addButton(new MerchantButton(1, this.getGuiLeft() + 120 + 27, this.getGuiTop() + 24 - 1, true));
        this.previousButton = this.addButton(new MerchantButton(2, this.getGuiLeft() + 36 - 19, this.getGuiTop() + 24 - 1, false));
        this.nextButton.enabled = false;
        this.previousButton.enabled = false;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        String s = this.chatComponent.getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    public void updateScreen() {
        super.updateScreen();
        SlayerMerchantTradeList trades = this.merchant.getTrades();
        if (trades != null) {
            this.nextButton.enabled = this.selectedTrade < trades.size() - 1;
            this.previousButton.enabled = this.selectedTrade > 0;
        }
        ((ContainerSlayerMerchant)this.inventorySlots).resetTrade();
        IFSPPackets.net.sendToServer(new C2SPacketGuiSlayerMerchantReset.Message());
    }

    protected void actionPerformed(GuiButton button) {
        boolean flag = false;
        if (button == this.nextButton) {
            ++this.selectedTrade;
            SlayerMerchantTradeList trades = this.merchant.getTrades();
            if (trades != null && this.selectedTrade >= trades.size()) {
                this.selectedTrade = trades.size() - 1;
            }
            flag = true;
        }
        else if (button == this.previousButton) {
            --this.selectedTrade;
            if (this.selectedTrade < 0) {
                this.selectedTrade = 0;
            }
            flag = true;
        }
        if (flag) {
            ((ContainerSlayerMerchant)this.inventorySlots).setCurrentRecipeIndex(this.selectedTrade);
            ((ContainerSlayerMerchant)this.inventorySlots).resetTrade();
            IFSPPackets.net.sendToServer(new C2SPacketGuiSlayerMerchantSetIndex.Message(this.selectedTrade));
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
        this.drawTexturedModalRect(this.getGuiLeft(), this.getGuiTop(), 0, 0, this.xSize, this.ySize);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        SlayerMerchantTradeList trades = this.merchant.getTrades();
        if (trades != null && !trades.isEmpty()) {
            int k = this.selectedTrade;
            SlayerMerchantTrade trade = trades.get(k);
            ItemStack itemstack = trade.getStack();
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            this.itemRender.zLevel = 100.0F;
            this.itemRender.renderItemAndEffectIntoGUI(itemstack, this.getGuiLeft() + 120, this.getGuiTop() + 24);
            this.itemRender.renderItemOverlays(this.fontRenderer, itemstack, this.getGuiLeft() + 120, this.getGuiTop() + 24);
            this.itemRender.zLevel = 0.0F;
            if (this.isPointInRegion(120, 24, 16, 16, mouseX, mouseY) && !itemstack.isEmpty()) {
                this.renderToolTip(itemstack, mouseX, mouseY);
            }
            //Render trade cost
            this.fontRenderer.drawString("Cost: " + trade.getCost(), this.getGuiLeft() + 35, this.getGuiTop() + 51, 0);
            WizardData data = WizardData.get(mc.player);
            if (data != null) {
                Integer points = data.getVariable(SlayerTracker.POINT_TRACKER);
                if (points != null) {
                    //Render total slayer points
                    this.fontRenderer.drawString("Points: " + points, this.getGuiLeft() + 35, this.getGuiTop() + 24, 0);
                }
            }
            GlStateManager.disableLighting();
            if (trade.isTradeDisabled()) {
                this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.drawTexturedModalRect(this.getGuiLeft() + 116, this.getGuiTop() + 50, 212, 0, 24, 24);
            }
            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
        }
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    public ISlayerMerchant getMerchant()
    {
        return this.merchant;
    }

    @SideOnly(Side.CLIENT)
    static class MerchantButton extends GuiButton {

        private final boolean forward;

        public MerchantButton(int buttonID, int x, int y, boolean p_i1095_4_) {
            super(buttonID, x, y, 12, 19, "");
            this.forward = p_i1095_4_;
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(GuiSlayerMerchant.MERCHANT_GUI_TEXTURE);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                int i = 0;
                int j = 176;
                if (!this.enabled) {
                    j += this.width * 2;
                }
                else if (flag) {
                    j += this.width;
                }
                if (!this.forward) {
                    i += this.height;
                }
                this.drawTexturedModalRect(this.x, this.y, j, i, this.width, this.height);
            }
        }
    }

}