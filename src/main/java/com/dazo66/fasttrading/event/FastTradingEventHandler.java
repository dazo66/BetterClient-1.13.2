package com.dazo66.fasttrading.event;

import com.dazo66.betterclient.event.GuiCloseEvent;
import com.dazo66.fasttrading.FastTrading;
import com.dazo66.fasttrading.client.gui.GuiMerchantModifier;
import com.dazo66.fasttrading.util.KeyLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

/**
 * @author Dazo66
 */
public class FastTradingEventHandler {

    private Minecraft mc = Minecraft.getMinecraft();
    private GuiMerchant gui;
    private GuiMerchantModifier modifier;

    public FastTradingEventHandler() {

    }

    @SubscribeEvent
    public void onActionPerformedEvent(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getGui() instanceof GuiMerchant) {
            if (modifier != null) {
                modifier.actionPerformed(event.getButton());
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc.currentScreen instanceof GuiMerchant) {
            if (modifier != null) {
                modifier.updateScreen();
            }
        }
    }

    @SubscribeEvent
    public void onGuiClose(GuiCloseEvent event) {
        if (event.getGui() instanceof GuiMerchant) {
            if (modifier != null) {
                modifier.onGuiClosed();
            }
        }
    }

    @SubscribeEvent
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.getGui() instanceof GuiMerchant) {
            if (modifier != null) {
                MerchantRecipeList list = gui.getMerchant().getRecipes(mc.player);
                if (modifier.getMerchantRecipeList() != list && list != null) {
                    modifier.setMerchantRecipeList(list);
                }
                modifier.drawScreen(event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks());
            }
        }
    }

    @SubscribeEvent
    public void onMouseInput(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (event.getGui() instanceof GuiMerchant) {
            if (modifier != null) {
                int i = Mouse.getEventX() * gui.width / this.mc.displayWidth;
                int j = gui.height - Mouse.getEventY() * gui.height / this.mc.displayHeight - 1;
                int k = Mouse.getEventButton();
                modifier.mouseClicked(i, j, k);
            }
        }
    }

    @SubscribeEvent
    public void initGui(GuiScreenEvent.InitGuiEvent event) {
        if (event.getGui() instanceof GuiMerchant) {
            if (modifier != null) {
                modifier.initGui();
            }
        }
    }

//    @SubscribeEvent
//    public void onSetMerchantList(SetMerchantListEvent event) {
//        if (mc.currentScreen instanceof GuiMerchant) {
//            if (modifier == null) {
//                modifier = new GuiMerchantModifier((GuiMerchant) mc.currentScreen);
//            }
//            modifier.setMerchantRecipeList(event.list);
//        }
//    }

    @SubscribeEvent
    public void guiOpenEvent(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMerchant) {
            gui = (GuiMerchant) event.getGui();
            modifier = new GuiMerchantModifier((GuiMerchant) event.getGui());
        }

    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (KeyLoader.key_F4.isPressed()) {
            FastTrading.setAuto(!FastTrading.isAuto.getValue());
        }
    }

}
