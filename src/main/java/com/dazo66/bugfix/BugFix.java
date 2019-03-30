package com.dazo66.bugfix;

import com.dazo66.betterclient.config.configentrys.AbstractConfigEntry;
import com.dazo66.betterclient.functionsbase.AbstractFunction;
import com.dazo66.betterclient.functionsbase.IFunction;
import com.dazo66.bugfix.event.BugFixEventHandler;
import com.dazo66.bugfix.subfunctions.ghostblockfix.GhostBlockFix;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dazo66
 */
public class BugFix extends AbstractFunction {
    @Override
    public String getID() {
        return "bugfix";
    }

    @Override
    public String getName() {
        return I18n.format("bugfix.name");
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
        return BugFixEventHandler.class;
    }

    @Override
    public List<AbstractConfigEntry> getConfigEntrys() {
        return null;
    }

    @Override
    public List<IFunction> getSubFunctions(){
        return Arrays.asList(new GhostBlockFix());
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
