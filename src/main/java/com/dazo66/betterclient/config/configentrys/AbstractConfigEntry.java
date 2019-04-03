package com.dazo66.betterclient.config.configentrys;

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

    public AbstractConfigEntry(String keyIn, String langKeyIn, T defaultValueIn, String[] commentIn) {
        key = keyIn;
        langKey = langKeyIn;
        defaultValue = defaultValueIn;
        comment = commentIn;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public void setLangKey(String langKeyIn) {
        this.langKey = langKeyIn;
    }


    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(T defaultValueIn) {
        this.defaultValue = defaultValueIn;
    }

    @Override
    public String[] getComment() {
        return comment;
    }
    @Override
    public void setComment(String[] commentIn) {
        this.comment = commentIn;
    }

    @Override
    public T getValue() {
        return value == null ? defaultValue : value;
    }

    @Override
    public void setValue(T valueIn) {
        this.value = valueIn;
    }

    @Override
    public Boolean getIsShowInGui() {
        return isShowInGui;
    }

    @Override
    public void setIsShowInGui(Boolean b) {
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
                new String[]{String.format("default=%s", entry.getDefaultValue()),
                            String.format("langkey=%s", entry.langKey)},
                            entry.getComment());
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Key: %s, CurrentValue: %s", this.getClass().getName(), key, getValue());
    }
}
