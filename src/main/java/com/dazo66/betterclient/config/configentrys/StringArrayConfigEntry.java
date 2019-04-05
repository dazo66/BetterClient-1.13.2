package com.dazo66.betterclient.config.configentrys;

import javax.annotation.Nonnull;

/**
 * @author Dazo66
 */
public class StringArrayConfigEntry extends ArrayConfigEntry<String> {

    protected String[] validValues;

    public StringArrayConfigEntry(@Nonnull String keyIn, String langKeyIn, @Nonnull String[] defaultValueIn, String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);
    }

    public StringArrayConfigEntry(@Nonnull String keyIn,@Nonnull String[] defultValueIn) {
        this(keyIn, keyIn, defultValueIn, new String[]{});
    }

}
