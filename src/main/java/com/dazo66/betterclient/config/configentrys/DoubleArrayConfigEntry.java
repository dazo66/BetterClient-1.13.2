package com.dazo66.betterclient.config.configentrys;

import com.dazo66.betterclient.functionsbase.IFunction;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nullable;

/**
 * @author Dazo66
 */
public class DoubleArrayConfigEntry extends AbstractConfigEntry<double[]> {

    public DoubleArrayConfigEntry(String keyIn, String langKeyIn, double[] defaultValueIn, IFunction ownerIn, @Nullable String commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, ownerIn, commentIn);
    }

    public DoubleArrayConfigEntry(String keyIn, double[] defultValueIn, IFunction ownerIn) {
        this(keyIn, keyIn, defultValueIn, ownerIn, null);
    }

    @Override
    public double[] getValue() {
        return property.getDoubleList();
    }

    @Override
    Property createProperty() {
        return config.get(owner.getID(), key, defaultValue, comment).setLanguageKey(langKey);
    }

}
