package com.dazo66.prompt.subfunction.healthprompt;

import com.dazo66.betterclient.config.configentrys.AbstractConfigEntry;
import com.dazo66.betterclient.config.configentrys.FloatConfigEntry;
import com.dazo66.betterclient.config.configentrys.IConfigEntry;
import com.dazo66.betterclient.functionsbase.AbstractFunction;
import com.dazo66.prompt.subfunction.healthprompt.eventhandler.HealthPromptEventHandler;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dazo66
 */
public class HealthPrompt extends AbstractFunction {

    public static AbstractConfigEntry<Float> warnHealth;

    @Override
    public String getID() {
        return "healthprompt";
    }

    @Override
    public String getName() {
        return I18n.format("healthprompt.name");
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getAuthor() {
        return "Dazo66";
    }

    @Override
    public Class eventHandlerClass() {
        return HealthPromptEventHandler.class;
    }

    @Override
    public List<AbstractConfigEntry> getConfigEntrys() {
        ArrayList<AbstractConfigEntry> list = new ArrayList<>();
        list.add(warnHealth);
        return list;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        warnHealth = new FloatConfigEntry("warnHealth", "prompt.warnhealth",  0.45f, this, "", 0f, 1f);
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
