package com.dazo66.betterclient.config.configentrys;

import com.dazo66.betterclient.functionsbase.IFunction;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nullable;

/**
 * @author Dazo66
 */
public class BooleanConfigEntry extends AbstractConfigEntry<Boolean> {

    public BooleanConfigEntry(String keyIn, String langKeyIn, boolean defaultValueIn, IFunction ownerIn, @Nullable String commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, ownerIn, commentIn);
    }

    public BooleanConfigEntry(String keyIn, boolean defultValueIn, IFunction ownerIn) {
        this(keyIn, keyIn, defultValueIn, ownerIn, null);
    }

    @Override
    public Boolean getValue() {
        return property.getBoolean();
    }

    @Override
    Property createProperty() {
        return config.get(owner.getID(), key, defaultValue, comment).setLanguageKey(langKey);
    }

}
