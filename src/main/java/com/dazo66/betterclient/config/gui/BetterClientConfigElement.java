package com.dazo66.betterclient.config.gui;

import com.dazo66.betterclient.config.configentrys.CustomizeGuiConfigEntry;
import com.dazo66.betterclient.config.configentrys.IConfigEntry;
import net.minecraftforge.fml.client.config.GuiConfigEntries;

/**
 * @author Dazo66
 */
public class BetterClientConfigElement extends net.minecraftforge.common.config.ConfigElement {

    IConfigEntry configEntry;

    public BetterClientConfigElement(IConfigEntry configEntryIn) {
        super(configEntryIn.getProperty());
        configEntry = configEntryIn;
    }

    @Override
    public Class<? extends GuiConfigEntries.IConfigEntry> getConfigEntryClass() {
        if (configEntry instanceof CustomizeGuiConfigEntry) {
            CustomizeGuiConfigEntry guiEntry = (CustomizeGuiConfigEntry) configEntry;
            CustomizeCategoryEntry.putScreen(guiEntry.getKey(), guiEntry.getGuiScreen());
            return CustomizeCategoryEntry.class;
        } else {
            return super.getConfigEntryClass();
        }
    }

}
