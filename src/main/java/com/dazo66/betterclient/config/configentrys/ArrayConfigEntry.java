package com.dazo66.betterclient.config.configentrys;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dazo66
 */
public class ArrayConfigEntry<T> extends AbstractConfigEntry<T[]> {

    public ArrayConfigEntry(String keyIn, String langKeyIn, T[] defaultValueIn, String[] commentIn) {
        super(keyIn, langKeyIn, defaultValueIn, commentIn);
    }

    public T addVaule(T e) {
        List<T> list = Arrays.asList(this.value);
        list.add(e);
        this.setValue((T[]) list.toArray());
        return e;
    }

}
