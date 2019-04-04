package com.dazo66.betterclient.config.configentrys;

import javax.annotation.Nullable;

/**
 * @author Dazo66
 */
public class StringConfigEntry extends AbstractConfigEntry<String> {

    public StringConfigEntry(String keyIn, String langKeyIn, String defaultValueIn, String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);
    }

    public StringConfigEntry(String keyIn, String defultValueIn) {
        this(keyIn, keyIn, defultValueIn, new String[]{});
    }

}
