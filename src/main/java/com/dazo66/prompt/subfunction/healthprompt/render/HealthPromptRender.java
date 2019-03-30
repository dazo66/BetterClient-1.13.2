package com.dazo66.prompt.subfunction.healthprompt.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

/**
 * @author Dazo66
 */
public class HealthPromptRender {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static void renderVignette() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        GlStateManager.disableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
        GlStateManager.color(0.0F, 1f, 1f, 1.0F);
        mc.getTextureManager().bindTexture(new ResourceLocation("textures/misc/vignette.png"));
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0D, (double) scaledResolution.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos((double) scaledResolution.getScaledWidth(), (double) scaledResolution.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos((double) scaledResolution.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }
}
