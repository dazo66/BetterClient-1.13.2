package com.dazo66.fasttrading.client.gui;

import com.dazo66.betterclient.client.audio.FakeSubtitleSound;
import com.dazo66.fasttrading.config.ConfigJson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.List;

/**
 * @author Dazo66
 */
public class GuiRecipeButton extends GuiButton {

    private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/villager.png");
    public boolean hasBeenMove = false;
    private Minecraft mc = Minecraft.getMinecraft();
    private GuiMerchantModifier modifier;
    private GuiMerchant gui;
    private MerchantRecipe recipe;

    public GuiRecipeButton(int buttonId, int x, int y, GuiMerchantModifier modifier, MerchantRecipe recipe) {
        super(buttonId, x, y, 89, 25, "");
        this.recipe = recipe;
        this.modifier = modifier;
        gui = modifier.getGui();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GlStateManager.popMatrix();
            renderBackground(mc);
            renderItem(mc);
            GlStateManager.pushMatrix();
        }
    }

    private void renderBackground(Minecraft mc) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.enableAlpha();
        ConfigJson.SimpleRecipe simpleRecipe = modifier.helper.map.get(recipe);
        if (null != simpleRecipe) {
            if (simpleRecipe.lockPrice) {
                if (ConfigJson.isRecipeEqual(recipe, simpleRecipe)) {
                    GlStateManager.color(1.0F, 0.8F, 0.8F, 1.0F);
                }
            } else {
                GlStateManager.color(1.0F, 0.9F, 0.9F, 1.0F);
            }
        } else {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        }
        mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
        this.drawTexturedModalRect(this.x, this.y, 0, 0, this.width - 5, this.height - 5);
        this.drawTexturedModalRect(this.x, this.y + height - 5, 0, 166 - 5, this.width / 2 + 1, 5);
        this.drawTexturedModalRect(this.x + width - 5, y, 176 - 5, 0, 5, this.height - 5);
        this.drawTexturedModalRect(this.x + width / 2 + 1, y + height - 5, 176 - width / 2, 166 - 5, width / 2, 5);
        if (recipe.isRecipeDisabled()) {
            this.drawTexturedModalRect(this.x + 18 * 2 + 3 * 5 - 11, this.y + 4, 212, 3, 28, 15);
        } else {
            this.drawTexturedModalRect(this.x + 18 * 2 + 3 * 5 - 11, this.y + 4, 83, 24, 28, 15);
        }
    }

    private void renderItem(Minecraft mc) {
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.disableLighting();
        String s0 = recipe.getItemToBuy().getCount() == 1 ? "" : String.valueOf(recipe.getItemToBuy().getCount());
        String s1 = recipe.getSecondItemToBuy().getCount() == 1 ? "" : String.valueOf(recipe.getSecondItemToBuy().getCount());
        String s2 = recipe.getItemToSell().getCount() == 1 ? "" : String.valueOf(recipe.getItemToSell().getCount());
        RenderItem renderItem = mc.getRenderItem();
        renderItem.renderItemAndEffectIntoGUI(recipe.getItemToBuy(), x + 5, y + 4);
        renderItem.renderItemAndEffectIntoGUI(recipe.getSecondItemToBuy(), x + 18 + 3 * 2 + 1, y + 4);
        renderItem.renderItemAndEffectIntoGUI(recipe.getItemToSell(), x + 18 * 2 + 3 * 4 + 28 + 2 - 10, y + 4);
        renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, recipe.getItemToBuy(), x + 5, y + 4, s0);
        renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, recipe.getSecondItemToBuy(), x + 18 + 3 * 2 + 1, y + 4, s1);
        renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, recipe.getItemToSell(), x + 18 * 2 + 3 * 4 + 28 + 2 - 10, y + 4, s2);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
    }

    @Override
    public boolean isMouseOver() {
        return hovered && visible && enabled;
    }

    private void renderItemTooltip(int mouseX, int mouseY, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return;
        }
        GuiUtils.drawHoveringText(getItemToolTip(itemStack), mouseX, mouseY, gui.width, gui.height, 250, mc.fontRenderer);
    }

    public void tryRenderItemTooltip(int mouseX, int mouseY) {
        if (visible) {
            GlStateManager.disableDepth();
            if (mouseX >= x + 5 && mouseX <= x + 23 && mouseY >= y + 4 && mouseY <= y + 22) {
                renderItemTooltip(mouseX, mouseY, recipe.getItemToBuy());
            } else if (mouseX >= x + 18 + 3 * 2 + 1 && mouseX <= x + 18 + 3 * 2 + 1 + 18 && mouseY >= y + 4 && mouseY <= y + 22) {
                renderItemTooltip(mouseX, mouseY, recipe.getSecondItemToBuy());
            } else if (mouseX >= x + 18 * 2 + 3 * 4 + 28 + 2 - 10 && mouseX <= x + 18 * 2 + 3 * 4 + 28 + 2 - 10 + 18 && mouseY >= y + 4 && mouseY <= y + 22) {
                renderItemTooltip(mouseX, mouseY, recipe.getItemToSell());
            }
        }
    }

    public void tryProminent(Minecraft mc, int mouseX, int mouseY, float p, boolean shouldDraw) {
        if (visible) {
            if (isMouseOver() && !hasBeenMove && shouldDraw) {
                x -= 1;
                y -= 1;
                height += 1;
                hasBeenMove = true;
                mc.getSoundHandler().playSound(FakeSubtitleSound.getRecord(SoundEvents.ENTITY_ITEM_PICKUP, 0.5f, 0.05F, "fasttrading.subtitles.buttonswitching"));
            } else if (!shouldDraw && hasBeenMove) {
                x += 1;
                y += 1;
                height -= 1;
                hasBeenMove = false;
            }
            if (shouldDraw) {
                mc.getRenderItem().zLevel = 120;
                drawButton(mc, mouseX, mouseY, p);
                mc.getRenderItem().zLevel = 100;
            }
        }
    }

    public List<String> getItemToolTip(ItemStack stack)
    {
        List<String> list = stack.getTooltip(this.mc.player, this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);

        for (int i = 0; i < list.size(); ++i)
        {
            if (i == 0)
            {
                list.set(i, stack.getRarity().rarityColor + (String)list.get(i));
            }
            else
            {
                list.set(i, TextFormatting.GRAY + (String)list.get(i));
            }
        }

        return list;
    }

}
