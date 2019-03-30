package com.dazo66.betterclient.config.configentrys;

import com.dazo66.betterclient.functionsbase.IFunction;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nullable;

/**
 * @author Dazo66
 */
public class DoubleConfigEntry extends AbstractConfigEntry<Double> {

    public DoubleConfigEntry(String keyIn, String langKeyIn, double defaultValueIn, IFunction ownerIn, @Nullable String commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, ownerIn, commentIn);

    }

    public DoubleConfigEntry(String keyIn, double defultValueIn, IFunction ownerIn) {
        this(keyIn, keyIn, defultValueIn, ownerIn, null);
    }

    @Override
    public Double getValue() {
        return property.getDouble(defaultValue);
    }

    @Override
    Property createProperty() {
        return config.get(owner.getID(), key, defaultValue, comment).setLanguageKey(langKey);
    }
}
