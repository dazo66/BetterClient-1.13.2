package com.dazo66.betterclient.config.configentrys;

import javax.annotation.Nonnull;

/**
 * @author Dazo66
 */
public class IntArrayConfigEntry extends ArrayConfigEntry<Integer> {

    public IntArrayConfigEntry(@Nonnull String keyIn, String langKeyIn, @Nonnull Integer[] defaultValueIn, String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);
    }

    public IntArrayConfigEntry(@Nonnull String keyIn,@Nonnull Integer[] defultValueIn) {
        this(keyIn, keyIn, defultValueIn, new String[]{});
    }
}
