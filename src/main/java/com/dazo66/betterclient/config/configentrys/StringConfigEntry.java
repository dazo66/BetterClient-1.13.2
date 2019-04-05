package com.dazo66.betterclient.config.configentrys;

import javax.annotation.Nonnull;

/**
 * @author Dazo66
 */
public class StringConfigEntry extends AbstractConfigEntry<String> {

    public StringConfigEntry(@Nonnull String keyIn, String langKeyIn, @Nonnull String defaultValueIn, String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);
    }

    public StringConfigEntry(@Nonnull String keyIn,@Nonnull String defultValueIn) {
        this(keyIn, keyIn, defultValueIn, new String[]{});
    }

}
