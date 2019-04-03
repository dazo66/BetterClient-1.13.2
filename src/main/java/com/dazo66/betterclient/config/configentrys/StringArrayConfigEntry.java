package com.dazo66.betterclient.config.configentrys;

import javax.annotation.Nullable;

/**
 * @author Dazo66
 */
public class StringArrayConfigEntry extends AbstractConfigEntry<String[]> {

    protected String[] validValues;

    public StringArrayConfigEntry(String keyIn, String langKeyIn, String[] defaultValueIn, @Nullable String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);
    }

    public StringArrayConfigEntry(String keyIn, String[] defultValueIn) {
        this(keyIn, keyIn, defultValueIn, new String[]{});
    }

}
