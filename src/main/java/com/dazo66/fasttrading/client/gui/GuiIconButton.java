package com.dazo66.fasttrading.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * @author Dazo66
 */
public class GuiIconButton extends GuiButtonPlus {

    private ResourceLocation icon;
    private int iconX = 0;
    private int iconY = 0;
    private int textureWidth = -1;
    private int textureHeight = -1;

    public GuiIconButton(int buttonId, int x, int y, int widthIn, int heightIn, String[] tooltipText, ResourceLocation icon) {
        super(buttonId, x, y, widthIn, heightIn, "", tooltipText);
        this.icon = icon;
    }

/*    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            super.drawButton(mc, mouseX, mouseY, partialTicks);
            GlStateManager.disableLighting();
            GlStateManager.disableDepthTest();
            mc.getTextureManager().bindTexture(icon);
            if (textureHeight == -1 || textureWidth == -1) {
                loadWidthAndHeight(mc.getResourceManager(), icon);
            }
            Gui.drawScaledCustomSizeModalRect(x, y, 0.0f, 0.0f, textureWidth, textureHeight, width, height, (float) textureWidth, (float) textureHeight);
            GlStateManager.enableDepthTest();
        }
    }*/

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            Minecraft mc = Minecraft.getInstance();
            super.render(mouseX, mouseY, partialTicks);
            GlStateManager.disableLighting();
            GlStateManager.disableDepthTest();
            mc.getTextureManager().bindTexture(icon);
            if (textureHeight == -1 || textureWidth == -1) {
                loadWidthAndHeight(mc.getResourceManager(), icon);
            }
            Gui.drawScaledCustomSizeModalRect(x, y, 0.0f, 0.0f, textureWidth, textureHeight, width, height, (float) textureWidth, (float) textureHeight);
            GlStateManager.enableDepthTest();
        }
    }

    @Override
    public boolean isMouseOver() {
        return hovered && visible && enabled;
    }

    public void drawCustomSizedTexture(int x, int y, float u, float v, int width, int height, ResourceLocation texture) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(texture);
        mc.getTextureManager().getTexture(texture).getGlTextureId();
        if (textureHeight == -1 || textureWidth == -1) {
            loadWidthAndHeight(mc.getResourceManager(), texture);
        }
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double) x, (double) (y + height), 0.0D).tex((double) (u * f), (double) ((v + (float) height) * f1)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + height), 0.0D).tex((double) ((u + (float) width) * f), (double) ((v + (float) height) * f1)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) y, 0.0D).tex((double) ((u + (float) width) * f), (double) (v * f1)).endVertex();
        bufferbuilder.pos((double) x, (double) y, 0.0D).tex((double) (u * f), (double) (v * f1)).endVertex();
        tessellator.draw();
    }

    public void loadWidthAndHeight(IResourceManager resourceManager, ResourceLocation resourceLocation) {
        IResource iresource = null;
        try {
            iresource = resourceManager.getResource(resourceLocation);
            //TODO 这里直接调用的图片读取流
            BufferedImage bufferedimage = ImageIO.read(iresource.getInputStream());//readBufferedImage(iresource.getInputStream());

            textureWidth = bufferedimage.getWidth();
            textureHeight = bufferedimage.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(iresource);
        }
    }

    public void setIconX(int iconX) {
        this.iconX = iconX;
    }

    public void setIconY(int iconY) {
        this.iconY = iconY;
    }

    public void setIcon(ResourceLocation icon) {
        this.icon = icon;
        textureWidth = -1;
        textureHeight = -1;
    }
}
