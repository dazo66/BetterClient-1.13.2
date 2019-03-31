package com.dazo66.betterclient.config.configentrys;

import com.google.gson.annotations.JsonAdapter;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Dazo66
 */
public abstract class AbstractConfigEntry<T> implements IConfigEntry<T> {

    protected String key;
    protected T value;
    protected String langKey;
    protected T defaultValue;
    protected String[] comment;
    protected boolean isShowInGui = true;
    protected CategoryConfigEntry path;

    public AbstractConfigEntry(String keyIn, String langKeyIn, T defultValueIn, String[] commentIn) {
        key = keyIn;
        langKey = langKeyIn;
        defaultValue = defultValueIn;
        comment = commentIn;
    }

    @Override
    public final String getKey() {
        return key;
    }

    @Override
    public final String getLangKey() {
        return langKey;
    }

    @Override
    public final T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public final String[] getComment() {
        return comment;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T valueIn) {
        this.value = valueIn;
    }

    @Override
    public boolean getIsShowInGui() {
        return isShowInGui;
    }

    @Override
    public final void setIsShowInGui(boolean b) {
        isShowInGui = b;
    }


    @Override
    public CategoryConfigEntry getPath(){
        return path;
    }

    @Override
    public void setPath(CategoryConfigEntry pathIn){
        path = pathIn;
    }

    public static String[] commentToSave(AbstractConfigEntry entry) {
        return ArrayUtils.addAll(
                new String[]{String.format("default:[%s]", entry.getDefaultValue()),
                            String.format("langkey:[%s]", entry.langKey)},
                entry.getComment());
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Key: %s, CurrentValue: %s", this.getClass().getName(), key, getValue());
    }
}
