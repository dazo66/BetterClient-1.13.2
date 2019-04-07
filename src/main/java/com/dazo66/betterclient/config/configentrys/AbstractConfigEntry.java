package com.dazo66.betterclient.config.configentrys;

import javax.annotation.Nonnull;

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


    public AbstractConfigEntry(@Nonnull String keyIn, String langKeyIn, @Nonnull T defaultValueIn, String[] commentIn) {
        key = keyIn;
        langKey = langKeyIn;
        defaultValue = defaultValueIn;
        comment = commentIn;
        if (langKey == null) {
            langKey = key;
        }
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(@Nonnull T defaultValueIn) {
        this.defaultValue = defaultValueIn;
    }

    @Override
    public T getValue() {
        return value == null ? defaultValue : value;
    }

    @Override
    public void setValue(@Nonnull T valueIn) {
        this.value = valueIn;
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
    public String getLangKey() {
        return langKey;
    }

    @Override
    public void setLangKey(String langKeyIn) {
        this.langKey = langKeyIn;
    }

    @Override
    public boolean getIsShowInGui() {
        return isShowInGui;
    }

    @Override
    public void setIsShowInGui(boolean b) {
        isShowInGui = b;
    }

    @Override
    public CategoryConfigEntry getPath() {
        return path;
    }

    @Override
    public void setPath(CategoryConfigEntry pathIn) {
        path = pathIn;
        if (!pathIn.containEntry(this)) {
            pathIn.addValue(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Key: %s, CurrentValue: %s", this.getClass().getName(), key, getValue());
    }



}
