package com.dazo66.betterclient.config.configentrys;

/**
 * @author Dazo66
 */
public class StringArrayConfigEntry extends ArrayConfigEntry<String> {

    protected String[] validValues;

    public StringArrayConfigEntry(String keyIn, String langKeyIn, String[] defaultValueIn, String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);
    }

    public StringArrayConfigEntry(String keyIn, String[] defultValueIn) {
        this(keyIn, keyIn, defultValueIn, new String[]{});
    }

}
