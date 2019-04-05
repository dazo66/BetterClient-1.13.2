package com.dazo66.betterclient.config.configentrys;

import javax.annotation.Nonnull;

/**
 * @author Dazo66
 */
public class DoubleArrayConfigEntry extends ArrayConfigEntry<Double> {

    public DoubleArrayConfigEntry(@Nonnull String keyIn, String langKeyIn, @Nonnull Double[] defaultValueIn, String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);
    }

    public DoubleArrayConfigEntry(@Nonnull String keyIn,@Nonnull Double[] defultValueIn) {
        this(keyIn, keyIn, defultValueIn, new String[]{});
    }
}
