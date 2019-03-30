package com.dazo66.betterclient.event;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Dazo66
 */
public class GuiCloseEvent extends Event {

    private GuiScreen gui;

    public GuiCloseEvent(GuiScreen gui) {
        this.gui = gui;
    }

    public static void post(GuiScreen gui) {
        if (null == gui) {
            return;
        }
        MinecraftForge.EVENT_BUS.post(new GuiCloseEvent(gui));
    }

    public GuiScreen getGui() {
        return gui;
    }
}
