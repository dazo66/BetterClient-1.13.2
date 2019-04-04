package com.dazo66.betterclient.config.configentrys;

/**
 * @author Dazo66
 */
public class DoubleArrayConfigEntry extends ArrayConfigEntry<Double> {

    public DoubleArrayConfigEntry(String keyIn, String langKeyIn, Double[] defaultValueIn, String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);
    }

    public DoubleArrayConfigEntry(String keyIn, Double[] defultValueIn) {
        this(keyIn, keyIn, defultValueIn, new String[]{});
    }
}
