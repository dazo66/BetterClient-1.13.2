package com.dazo66.betterclient.config.configentrys;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Property;

/**
 * @author Dazo66
 */
public class CustomizeGuiConfigEntry<T> extends AbstractConfigEntry<T> {

    private IConfigEntry<T> entry;
    private GuiScreen guiScreen;

    public CustomizeGuiConfigEntry(IConfigEntry<T> entryIn, GuiScreen guiScreenIn) {
        super(entryIn.getKey(), entryIn.getLangKey(), entryIn.getDefaultValue(), entryIn.getOwner(), entryIn.getComment());
        entry = entryIn;
        property = entryIn.getProperty();
        guiScreen = guiScreenIn;
    }

    @Override
    public T getValue() {
        return entry.getValue();
    }

    @Override
    public Property getProperty() {
        return entry.getProperty();
    }

    @Override
    Property createProperty() {
        return null;
    }

    public IConfigEntry<T> getEntry() {
        return entry;
    }

    public GuiScreen getGuiScreen() {
        return guiScreen;
    }
}
