package com.dazo66.prompt.subfunction.projectileprompt.util;


import com.dazo66.betterclient.util.reflection.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;

import java.lang.reflect.Field;


/**
 * @author Dazo66
 */
public final class RenderUtils {

    private static Field bufferField;
    private static Field shaderField;

    static {
        bufferField = ReflectionHelper.getInstance().getField(RenderGlobal.class, "entityOutlineFramebuffer");
        shaderField = ReflectionHelper.getInstance().getField(RenderGlobal.class, "entityOutlineShader");
    }


    public static void renderOutLine(Entity entity) {
        Minecraft mc = Minecraft.getMinecraft();
        float partialTicks = mc.getRenderPartialTicks();
        Entity viewEntity = mc.getRenderViewEntity();
        double d0 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * (double) partialTicks;
        double d1 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * (double) partialTicks;
        double d2 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * (double) partialTicks;
        GlStateManager.enableColorMaterial();
        GlStateManager.enableOutlineMode(16777215);
        Object entityOutlineFramebuffer;
        Object entityOutlineShader;
        try {
            entityOutlineFramebuffer = bufferField.get(mc.renderGlobal);
            entityOutlineShader = shaderField.get(mc.renderGlobal);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        ((Framebuffer)entityOutlineFramebuffer).framebufferClear();
        GlStateManager.depthFunc(519);
        GlStateManager.disableFog();
        ((Framebuffer)entityOutlineFramebuffer).bindFramebuffer(false);
        RenderHelper.disableStandardItemLighting();
        mc.getRenderManager().setRenderOutlines(true);
        mc.getRenderManager().renderEntity(entity, entity.posX - d0, entity.posY - d1, entity.posZ - d2, entity.rotationYaw, partialTicks, false);
        mc.getRenderManager().setRenderOutlines(false);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.depthMask(false);
        ((ShaderGroup) entityOutlineShader).render(partialTicks);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.enableFog();
        GlStateManager.enableBlend();
        GlStateManager.enableColorMaterial();
        GlStateManager.depthFunc(515);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        mc.getFramebuffer().bindFramebuffer(false);
        GlStateManager.disableOutlineMode();
        GlStateManager.disableColorMaterial();
    }

}
