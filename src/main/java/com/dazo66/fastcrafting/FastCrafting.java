package com.dazo66.fastcrafting;

import com.dazo66.betterclient.config.configentrys.AbstractConfigEntry;
import com.dazo66.betterclient.coremod.IRegisterTransformer;
import com.dazo66.betterclient.functionsbase.AbstractFunction;
import com.dazo66.fastcrafting.eventhandler.FastCraftingEventHandler;
import com.dazo66.fastcrafting.transformer.MineRecipe;
import com.dazo66.fastcrafting.transformer.MouseClickTransformerClass;
import com.dazo66.fastcrafting.transformer.SendRecipePacketHook0;
import com.dazo66.fastcrafting.transformer.SendRecipePacketHook1;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dazo66
 */
public class FastCrafting extends AbstractFunction {

    public static final String MODID = "fastcrafting";
    public static final String NAME = "TCFastCrafting";
    public static final String MCVERSION = "[1.12,1.12.2]";
    public static final String VERSION = "2.6";

    @Override
    public String getID() {
        return MODID;
    }

    @Override
    public String getName() {
        return I18n.format("fastcrafting.name");
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
        return FastCraftingEventHandler.class;
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
        return Arrays.asList(MineRecipe.class, SendRecipePacketHook0.class, SendRecipePacketHook1.class, MouseClickTransformerClass.class);
    }
}
