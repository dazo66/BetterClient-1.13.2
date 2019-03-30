package com.dazo66.prompt.subfunction.healthprompt.eventhandler;

import com.dazo66.prompt.subfunction.healthprompt.render.HealthPromptRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.dazo66.prompt.subfunction.healthprompt.HealthPrompt.warnHealth;

/**
 * @author Dazo66
 */
public class HealthPromptEventHandler {

    private Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (mc.world != null) {
                EntityPlayer play = mc.player;
                if (play != null && warnHealth != null) {
                    if (play.getHealth() / play.getMaxHealth() < warnHealth.getValue()) {
                        if (!mc.gameSettings.hideGUI &&
                                !(mc.currentScreen instanceof GuiChat) &&
                                !(mc.currentScreen instanceof GuiErrorScreen) &&
                                !(mc.currentScreen instanceof GuiIngameMenu)) {
                            HealthPromptRender.renderVignette();
                        }
                    }
                }
            }
        }
    }
}
