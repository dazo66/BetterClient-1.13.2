package com.dazo66.betterclient.config.configentrys;

import com.dazo66.betterclient.functionsbase.IFunction;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nullable;

/**
 * @author Dazo66
 */
public class StringArrayConfigEntry extends AbstractConfigEntry<String[]> {

    protected String[] validValues;

    public StringArrayConfigEntry(String keyIn, String langKeyIn, String[] defaultValueIn, IFunction ownerIn, @Nullable String commentIn, String[] validValuesIn) {
        super(keyIn, langKeyIn, defaultValueIn, ownerIn, commentIn);
        validValues = validValuesIn;
    }

    public StringArrayConfigEntry(String keyIn, String[] defultValueIn, IFunction ownerIn) {
        this(keyIn, keyIn, defultValueIn, ownerIn, null, null);
    }

    @Override
    public String[] getValue() {
        return property.getStringList();
    }

    @Override
    Property createProperty() {
        Property property = config.get(owner.getID(), key, defaultValue, comment).setLanguageKey(langKey);
        if (validValues != null) {
            property.setValidValues(validValues);
        }
        property.setComment(comment + " [default: " + property.getDefault() + "]");
        return property;
    }

}
