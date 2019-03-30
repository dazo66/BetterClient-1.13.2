package com.dazo66.betterclient.config.gui;

import com.dazo66.betterclient.BetterClient;
import com.dazo66.betterclient.FunctionsRegister;
import com.dazo66.betterclient.config.configentrys.AbstractConfigEntry;
import com.dazo66.betterclient.config.configentrys.BooleanConfigEntry;
import com.dazo66.betterclient.config.configentrys.IConfigEntry;
import com.dazo66.betterclient.functionsbase.IFunction;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dazo66
 */
public class BetterClientGuiConfig extends GuiConfig {

    public BetterClientGuiConfig(GuiScreen parentScreen, String modid, String title) {
        super(parentScreen, getConfigElements(), modid, false, false, title, "");
    }

    public static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();
        for (IFunction function : FunctionsRegister.primaryFunctions) {
            list.add(getConfigElements(function));
        }
        return list;
    }

    private static DummyConfigElement.DummyCategoryElement getConfigElements(IFunction function) {

        List<IConfigElement> list1 = new ArrayList<>();
        BooleanConfigEntry isEnable = new BooleanConfigEntry("enable", "enable", true, function, "This function is enable to load or not.");
        list1.add(new BetterClientConfigElement(isEnable));
        List<IFunction> functions = function.getSubFunctions();
        List<AbstractConfigEntry> entries = function.getConfigEntrys();
        if (functions != null) {
            for (IFunction function1 : functions) {
                list1.add(getConfigElements(function1));
            }
        }
        if (entries != null) {
            for (IConfigEntry configEntry : entries) {
                list1.add(new BetterClientConfigElement(configEntry));
            }
        }
        return new DummyConfigElement.DummyCategoryElement(function.getName(), function.getID(), list1);

    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        CustomizeCategoryEntry.clearMap();
        BetterClient.config.save();
    }
}
