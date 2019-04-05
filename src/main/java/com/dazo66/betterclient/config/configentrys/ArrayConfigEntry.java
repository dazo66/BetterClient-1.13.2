package com.dazo66.betterclient.config.configentrys;

import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;

/**
 * @author Dazo66
 */
public class ArrayConfigEntry<T> extends AbstractConfigEntry<T[]> {

    public ArrayConfigEntry(@Nonnull String keyIn, String langKeyIn, @Nonnull T[] defaultValueIn, String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);
    }

    public T addVaule(@Nonnull T e) {
        this.setValue(ArrayUtils.add(this.value, e));
        return e;
    }

}
