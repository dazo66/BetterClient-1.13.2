package com.dazo66.betterclient.config.configentrys;

import com.dazo66.betterclient.functionsbase.IFunction;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nullable;

/**
 * @author Dazo66
 */
public class StringConfigEntry extends AbstractConfigEntry<String> {

    public StringConfigEntry(String keyIn, String langKeyIn, String defaultValueIn, IFunction ownerIn, @Nullable String commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, ownerIn, commentIn);
    }

    public StringConfigEntry(String keyIn, String defultValueIn, IFunction ownerIn) {
        this(keyIn, keyIn, defultValueIn, ownerIn, null);
    }

    @Override
    public String getValue() {
        return property.getString();
    }

    @Override
    Property createProperty() {
        return config.get(owner.getID(), key, defaultValue, comment).setLanguageKey(langKey);
    }

}
