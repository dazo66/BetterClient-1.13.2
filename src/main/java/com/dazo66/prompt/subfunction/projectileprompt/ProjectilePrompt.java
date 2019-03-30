package com.dazo66.prompt.subfunction.projectileprompt;

import com.dazo66.betterclient.config.configentrys.AbstractConfigEntry;
import com.dazo66.betterclient.coremod.IRegisterTransformer;
import com.dazo66.betterclient.functionsbase.AbstractFunction;
import com.dazo66.prompt.subfunction.projectileprompt.eventhandler.ProjectilePromptEventHandler;
import com.dazo66.prompt.subfunction.projectileprompt.transformer.ParticleRenferRangeInject;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dazo66
 */
public class ProjectilePrompt extends AbstractFunction {

    @Override
    public String getID() {
        return "projectileprompt";
    }

    @Override
    public String getName() {
        return I18n.format("projectileprompt.name");
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
        return ProjectilePromptEventHandler.class;
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

    @Override
    public List<Class<? extends IRegisterTransformer>> transformerClass() {
        return Arrays.asList(ParticleRenferRangeInject.class);
    }

}
