package com.dazo66.betterclient.config.configentrys;

import javax.annotation.Nullable;

/**
 * @author Dazo66
 */
public class DoubleConfigEntry extends AbstractConfigEntry<Double> {

    public DoubleConfigEntry(String keyIn, String langKeyIn, double defaultValueIn, String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);

    }

    public DoubleConfigEntry(String keyIn, double defultValueIn) {
        this(keyIn, keyIn, defultValueIn, new String[]{});
    }

}
