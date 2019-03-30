package com.dazo66.shulkerboxshower;

import com.dazo66.betterclient.config.configentrys.AbstractConfigEntry;
import com.dazo66.betterclient.config.configentrys.BooleanConfigEntry;
import com.dazo66.betterclient.config.configentrys.IConfigEntry;
import com.dazo66.betterclient.config.configentrys.IntConfigEntry;
import com.dazo66.betterclient.functionsbase.AbstractFunction;
import com.dazo66.shulkerboxshower.eventhandler.ShulkerBoxViewerEventHandler;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dazo66
 */
public class ShulkerBoxViewer extends AbstractFunction {
    public static final String VERSION = "1.5";
    private static final String ID = "shulkerboxviewer";
    public static AbstractConfigEntry<Boolean> isOrganizing;
    public static AbstractConfigEntry<Integer> offsetX;
    public static AbstractConfigEntry<Integer> offsetY;

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getName() {
        return I18n.format("shulkerboxviewer.name");
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
        return ShulkerBoxViewerEventHandler.class;
    }

    @Override
    public List<AbstractConfigEntry> getConfigEntrys() {
        ArrayList<AbstractConfigEntry> list = new ArrayList<>();
        list.add(isOrganizing);
        list.add(offsetX);
        list.add(offsetY);
        return list;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        offsetX = new IntConfigEntry("offsetx", "shulkerboxviewer.offsetx", 0, this, "offset X in game gui", null, null);
        offsetY = new IntConfigEntry("offsety", "shulkerboxviewer.offsety", 0, this, "offset Y in game gui", null, null);
        isOrganizing = new BooleanConfigEntry("isorganizing", "shulkerboxviewer.isorganizing", false, this, "Organizing the items or not");
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

}
