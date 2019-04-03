package com.dazo66.betterclient.config.configentrys;

import com.dazo66.betterclient.functionsbase.IFunction;

import javax.annotation.Nullable;

/**
 * @author Dazo66
 */
public class IntConfigEntry extends AbstractConfigEntry<Integer> {


    public IntConfigEntry(String keyIn, String langKeyIn, int defaultValueIn, @Nullable String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);
    }

    public IntConfigEntry(String keyIn, int defultValueIn) {
        this(keyIn, keyIn, defultValueIn, new String[]{});
    }

}
