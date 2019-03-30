package com.dazo66.betterclient.config.configentrys;

import com.dazo66.betterclient.BetterClient;
import com.dazo66.betterclient.functionsbase.IFunction;
import com.google.common.primitives.Floats;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nullable;

/**
 * @author Dazo66
 */
public class FloatConfigEntry extends AbstractConfigEntry<Float> {

    protected Float min;
    protected Float max;

    public FloatConfigEntry(String keyIn, String langKeyIn, float defaultValueIn, IFunction ownerIn, @Nullable String commentIn, @Nullable Float minIn, @Nullable Float maxIn) {
        super(keyIn, langKeyIn, defaultValueIn, ownerIn, commentIn);
        min = minIn;
        max = maxIn;
    }

    public FloatConfigEntry(String keyIn, float defultValueIn, IFunction ownerIn) {
        this(keyIn, keyIn, defultValueIn, ownerIn, null, null, null);
    }

    public Float getMin() {
        return min;
    }

    public Float getMax() {
        return max;
    }


    @Override
    public Float getValue() {
        try {
            float parseFloat = Float.parseFloat(getProperty().getString());
            if (min != null && max != null) {
                return Floats.constrainToRange(parseFloat, min, max);
            }
            return parseFloat;
        } catch (Exception e) {
            BetterClient.logger.error("Failed to get float for {}/{}", key, owner.getID(), e);
        }
        return defaultValue;
    }

    @Override
    public void setValue(Float i) {
        getProperty().setValue(i);
        config.save();
    }

    @Override
    Property createProperty() {
        Property property;
        property = config.get(owner.getID(), key, Float.toString(defaultValue), key);
        property.setLanguageKey(langKey);
        return property;
    }

}
