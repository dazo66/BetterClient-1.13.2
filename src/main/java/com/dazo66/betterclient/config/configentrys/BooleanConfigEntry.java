package com.dazo66.betterclient.config.configentrys;

import javax.annotation.Nonnull;

/**
 * @author Dazo66
 */
public class BooleanConfigEntry extends AbstractConfigEntry<Boolean> {

    public BooleanConfigEntry(@Nonnull String keyIn, String langKeyIn, @Nonnull Boolean defaultValueIn, String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);
    }
}
