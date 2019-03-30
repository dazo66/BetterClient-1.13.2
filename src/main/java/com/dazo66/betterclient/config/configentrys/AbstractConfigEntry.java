package com.dazo66.betterclient.config.configentrys;

import com.dazo66.betterclient.BetterClient;
import com.dazo66.betterclient.functionsbase.IFunction;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * @author Dazo66
 */
public abstract class AbstractConfigEntry<T> implements IConfigEntry<T> {

    protected Configuration config = BetterClient.config;
    protected String key;
    protected String langKey;
    protected T defaultValue;
    protected IFunction owner;
    protected String comment;
    protected Property property;

    public AbstractConfigEntry(String keyIn, String langKeyIn, T defultValueIn, IFunction ownerIn, String commentIn) {
        key = keyIn;
        langKey = langKeyIn;
        defaultValue = defultValueIn;
        owner = ownerIn;
        comment = commentIn;
        property = createProperty();
    }

    @Override
    public final String getKey() {
        return key;
    }

    @Override
    public final String getLangKey() {
        return langKey;
    }

    @Override
    public final T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public final IFunction getOwner() {
        return owner;
    }

    @Override
    public final String getComment() {
        return comment;
    }

    @Override
    public abstract T getValue();

    @Override
    public void setValue(T valueIn) {
        if (valueIn instanceof Integer) {
            property.set((Integer) valueIn);
        } else if (valueIn instanceof int[]) {
            property.set((int[]) valueIn);
        } else if (valueIn instanceof String) {
            property.set((String) valueIn);
        } else if (valueIn instanceof String[]) {
            property.set((String[]) valueIn);
        } else if (valueIn instanceof Double) {
            property.set((Double) valueIn);
        } else if (valueIn instanceof double[]) {
            property.set((double[]) valueIn);
        } else if (valueIn instanceof Boolean) {
            property.set((Boolean) valueIn);
        }
        config.save();
    }

    @Override
    public Property getProperty() {
        return property;
    }

    abstract Property createProperty();

    public final void setShowInGui(boolean b) {
        if (property == null) {
            property = getProperty();
            property.setShowInGui(b);
        } else {
            property.setShowInGui(b);
        }
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Key: %s, CurrentValue: %s", this.getClass().getName(), key, getValue());
    }
}
