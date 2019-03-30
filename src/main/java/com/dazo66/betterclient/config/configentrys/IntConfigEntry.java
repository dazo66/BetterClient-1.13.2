package com.dazo66.betterclient.config.configentrys;

import com.dazo66.betterclient.functionsbase.IFunction;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nullable;

/**
 * @author Dazo66
 */
public class IntConfigEntry extends AbstractConfigEntry<Integer> {

    private Integer min;
    private Integer max;

    public IntConfigEntry(String keyIn, String langKeyIn, int defaultValueIn, IFunction ownerIn, @Nullable String commentIn, @Nullable Integer minIn, @Nullable Integer maxIn) {
        super(keyIn, langKeyIn, defaultValueIn, ownerIn, commentIn);
        max = maxIn;
        min = minIn;
    }

    public IntConfigEntry(String keyIn, int defultValueIn, IFunction ownerIn) {
        this(keyIn, keyIn, defultValueIn, ownerIn, null, null, null);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }


    @Override
    public Integer getValue() {
        if (comment == null || (min == null || max == null)) {
            return property.getInt(defaultValue);
        }
        int i = property.getInt(defaultValue);
        return i < min ? min : (i > max ? max : i);
    }

    @Override
    Property createProperty() {
        Property property;
        property = config.get(owner.getID(), key, defaultValue).setLanguageKey(langKey);
        if (min != null && max != null) {
            property.setComment(comment + " [range: " + min + " ~ " + max + ", default: " + defaultValue + "]");
            property.setMinValue(min);
            property.setMaxValue(max);
        }
        if (comment != null) {
            property.setComment(comment);
        }
        return property;
    }


}
