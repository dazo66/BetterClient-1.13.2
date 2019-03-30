package com.dazo66.betterclient.config.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.HashMap;

/**
 * @author Dazo66
 */
public class CustomizeCategoryEntry extends GuiConfigEntries.CategoryEntry {

    public static HashMap<String, GuiScreen> screenMap = new HashMap<>(10);

    public CustomizeCategoryEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
        super(owningScreen, owningEntryList, configElement);
    }

    public static void putScreen(String configKey, GuiScreen gui) {
        screenMap.put(configKey, gui);
    }

    public static void clearMap() {
        screenMap.clear();
    }

    @Override
    protected GuiScreen buildChildScreen() {
        return screenMap.get(configElement.getName());
    }
}
