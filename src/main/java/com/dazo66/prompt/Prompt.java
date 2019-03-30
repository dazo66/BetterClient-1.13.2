package com.dazo66.prompt;

import com.dazo66.betterclient.config.configentrys.AbstractConfigEntry;
import com.dazo66.betterclient.functionsbase.AbstractFunction;
import com.dazo66.betterclient.functionsbase.IFunction;
import com.dazo66.prompt.event.eventhandler.PromptEventhandler;
import com.dazo66.prompt.subfunction.fishingprompt.FishingPrompt;
import com.dazo66.prompt.subfunction.furnaceprompt.FurnacePrompt;
import com.dazo66.prompt.subfunction.healthprompt.HealthPrompt;
import com.dazo66.prompt.subfunction.projectileprompt.ProjectilePrompt;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dazo66
 */
public class Prompt extends AbstractFunction {

    public static final String MODID = "prompt";
    public static final String VERSION = "2.0";

    @Override
    public String getID() {
        return MODID;
    }

    @Override
    public String getName() {
        return I18n.format("prompt.name");
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public String getAuthor() {
        return "Dazo66";
    }

    @Override
    public Class eventHandlerClass() {
        return PromptEventhandler.class;
    }

    @Override
    public List<IFunction> getSubFunctions(){
        return Arrays.asList(new FishingPrompt(), new HealthPrompt(), new FurnacePrompt(), new ProjectilePrompt());
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
