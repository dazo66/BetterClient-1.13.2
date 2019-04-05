package com.dazo66.betterclient.config.configentrys;

import javax.annotation.Nonnull;

/**
 * @author Dazo66
 */
public class FloatConfigEntry extends AbstractConfigEntry<Float> {

    public FloatConfigEntry(@Nonnull String keyIn, String langKeyIn, @Nonnull float defultValueIn, String[] commentIn) {
        super(keyIn, langKeyIn, defultValueIn, commentIn);
    }

}
