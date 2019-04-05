package com.dazo66.betterclient.config.configentrys;

import javax.annotation.Nonnull;

/**
 * @author Dazo66
 */
public class DoubleConfigEntry extends AbstractConfigEntry<Double> {

    public DoubleConfigEntry(@Nonnull String keyIn, String langKeyIn, @Nonnull double defaultValueIn, String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);

    }

    public DoubleConfigEntry(@Nonnull String keyIn,@Nonnull double defultValueIn) {
        this(keyIn, keyIn, defultValueIn, new String[]{});
    }

}
