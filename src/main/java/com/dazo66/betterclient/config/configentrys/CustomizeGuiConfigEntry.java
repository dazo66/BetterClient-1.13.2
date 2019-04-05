package com.dazo66.betterclient.config.configentrys;

import net.minecraft.client.gui.GuiScreen;

import javax.annotation.Nonnull;

/**
 * @author Dazo66
 */
public class CustomizeGuiConfigEntry extends CategoryConfigEntry {

    private GuiScreen guiScreen;

    public CustomizeGuiConfigEntry(@Nonnull String keyIn, String langKeyIn,@Nonnull String dafaultIn, String[] commentIn,@Nonnull GuiScreen guiScreenIn) {
        super(keyIn, langKeyIn, commentIn);
        guiScreen = guiScreenIn;
    }

    public GuiScreen getGuiScreen() {
        return guiScreen;
    }
}
