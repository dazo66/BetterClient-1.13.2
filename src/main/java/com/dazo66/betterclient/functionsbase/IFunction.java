package com.dazo66.betterclient.functionsbase;

import com.dazo66.betterclient.config.configentrys.AbstractConfigEntry;
import com.dazo66.betterclient.config.configentrys.IConfigEntry;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.*;

import java.util.List;

/**
 * @author Dazo66
 */
public interface IFunction extends Comparable<IFunction> {

    /**
     * the function id
     *
     * @return id
     */
    String getID();

    /**
     * the function name return the local name{@link I18n} was best
     *
     * @return name
     */
    String getName();

    /**
     * this function version
     *
     * @return version
     */
    String getVersion();

    /**
     * the author of this function
     *
     * @return author
     */
    String getAuthor();


    /**
     * you can put all event {@link SubscribeEvent} to the handler class don't need to register
     * please don't make method static in handles class
     *
     * @return handler class
     */
    Class eventHandlerClass();

    /**
     * the config entryts
     * all of this will display in config gui
     *
     * @return a list of {@link IConfigEntry}
     */
    List<AbstractConfigEntry> getConfigEntrys();

    /**
     * if this function has subfunction
     * please return at here
     *
     * @return a list of {@link IFunction}
     */

    List<IFunction> getSubFunctions();

    /**
     * on FMLPreInitializationEvent tryInvoke
     *
     * @param event FMLCommonSetupEvent
     */
    void preInit(FMLCommonSetupEvent event);

    /**
     * on FMLInitializationEvent tryInvoke
     *
     * @param event FMLClientSetupEvent
     */
    void init(FMLClientSetupEvent event);

    /**
     * on FMLPostInitializationEvent tryInvoke
     *
     * @param event FMLPostInitializationEvent
     */
    void postInit(InterModProcessEvent event);

    /**
     * on FMLLoadCompleteEvent tryInvoke
     *
     * @param event FMLLoadCompleteEvent
     */
    void loadComplete(FMLLoadCompleteEvent event);

    /**
     * on FMLServerStartedEvent tryInvoke
     *
     * @param event FMLServerStartedEvent
     */
    void serverStarted(FMLServerStartedEvent event);

    /**
     * on FMLServerStartingEvent tryInvoke
     *
     * @param event FMLServerStartingEvent
     */
    void serverStarting(FMLServerStartingEvent event);

    /**
     * on FMLServerAboutToStartEvent tryInvoke
     *
     * @param event FMLServerAboutToStartEvent
     */
    void serverAboutToStart(FMLServerAboutToStartEvent event);

    /**
     * on FMLServerStoppingEvent tryInvoke
     *
     * @param event FMLServerStoppingEvent
     */
    void serverStoping(FMLServerStoppingEvent event);

    /**
     * on FMLServerStoppedEvent tryInvoke
     *
     * @param event FMLServerStoppedEvent
     */
    void serverStoped(FMLServerStoppedEvent event);

}
