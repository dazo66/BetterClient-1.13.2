package com.dazo66.fasttrading.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.Arrays;

/**
 * @author Dazo66
 */
public class GuiButtonPlus extends GuiButton {

    private static ResourceLocation resourceButtonDefault = new ResourceLocation("textures/gui/widgets.png");
    private int hoverTime = 0;
    private String[] tooltipText;

    public GuiButtonPlus(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, String[] tooltipText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.tooltipText = tooltipText;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (isMouseOver()) {
                hoverTime++;
            } else {
                hoverTime = 0;
            }
            int k = this.getHoverState(isMouseOver());
            mc.getTextureManager().bindTexture(resourceButtonDefault);
            this.drawTexturedModalRect(this.x, this.y, 1, 46 + k * 20 + 1, this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.x, this.y + this.height / 2, 1, 46 + k * 20 + 20 - this.height / 2 - 1, this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2 - 1, 46 + k * 20 + 1, this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y + this.height / 2, 200 - this.width / 2 - 1, 46 + k * 20 + 19 - this.height / 2, this.width / 2, this.height / 2);
        }
    }

    @Override
    public boolean isMouseOver() {
        return hovered && visible && enabled;
    }

    public void drawTooltip(Minecraft mc, int mouseX, int mouseY) {
        if (hoverTime > 15 && visible) {
            FontRenderer font = mc.fontRenderer;
            GuiUtils.drawHoveringText(ItemStack.EMPTY, Arrays.asList(tooltipText), mouseX, mouseY, 1000, 10000, 10000, font);
        }
    }

}
