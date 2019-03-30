package com.dazo66.prompt.subfunction.fishingprompt;

import com.dazo66.betterclient.config.configentrys.AbstractConfigEntry;
import com.dazo66.betterclient.functionsbase.AbstractFunction;
import com.dazo66.prompt.subfunction.fishingprompt.eventhandler.FishingPromptEventHandler;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.List;

/**
 * @author Dazo66
 */
public class FishingPrompt extends AbstractFunction {
    @Override
    public String getID() {
        return "fishingprompt";
    }

    @Override
    public String getName() {
        return I18n.format("fishingprompt.name");
    }

    @Override
    public String getVersion() {
        return "1.3";
    }

    @Override
    public String getAuthor() {
        return "Dazo66";
    }

    @Override
    public Class eventHandlerClass() {
        return FishingPromptEventHandler.class;
    }

    @Override
    public List<AbstractConfigEntry> getConfigEntrys() {
        return null;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
