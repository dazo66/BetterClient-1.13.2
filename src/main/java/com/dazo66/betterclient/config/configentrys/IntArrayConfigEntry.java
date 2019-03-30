package com.dazo66.betterclient.config.configentrys;

import com.dazo66.betterclient.functionsbase.IFunction;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nullable;

/**
 * @author Dazo66
 */
public class IntArrayConfigEntry extends AbstractConfigEntry<int[]> {

    public IntArrayConfigEntry(String keyIn, String langKeyIn, int[] defaultValueIn, IFunction ownerIn, @Nullable String commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, ownerIn, commentIn);
    }

    public IntArrayConfigEntry(String keyIn, int[] defultValueIn, IFunction ownerIn) {
        this(keyIn, keyIn, defultValueIn, ownerIn, null);
    }

    @Override
    public int[] getValue() {
        return property.getIntList();
    }

    @Override
    Property createProperty() {
        return config.get(owner.getID(), key, defaultValue, comment).setLanguageKey(langKey);
    }
}
