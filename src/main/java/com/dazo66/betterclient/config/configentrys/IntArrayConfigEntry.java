package com.dazo66.betterclient.config.configentrys;

/**
 * @author Dazo66
 */
public class IntArrayConfigEntry extends ArrayConfigEntry<Integer> {

    public IntArrayConfigEntry(String keyIn, String langKeyIn, Integer[] defaultValueIn, String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);
    }

    public IntArrayConfigEntry(String keyIn, Integer[] defultValueIn) {
        this(keyIn, keyIn, defultValueIn, new String[]{});
    }
}
