package com.dazo66.betterclient.config.configentrys;

import javax.annotation.Nullable;

/**
 * @author Dazo66
 */
public class DoubleArrayConfigEntry extends AbstractConfigEntry<double[]> {

    public DoubleArrayConfigEntry(String keyIn, String langKeyIn, double[] defaultValueIn, @Nullable String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);
    }

    public DoubleArrayConfigEntry(String keyIn, double[] defultValueIn) {
        this(keyIn, keyIn, defultValueIn, new String[]{});
    }
}
