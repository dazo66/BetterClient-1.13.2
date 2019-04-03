package com.dazo66.betterclient.config.configentrys;

import net.minecraft.client.gui.GuiScreen;

/**
 * @author Dazo66
 */
public class CustomizeGuiConfigEntry extends CategoryConfigEntry {

    private GuiScreen guiScreen;

    public CustomizeGuiConfigEntry(String keyIn, String langKeyIn, String dafaultIn, String[] commentIn, GuiScreen guiScreenIn) {
        super(keyIn, langKeyIn, commentIn);
        guiScreen = guiScreenIn;
    }

    public GuiScreen getGuiScreen() {
        return guiScreen;
    }
}
