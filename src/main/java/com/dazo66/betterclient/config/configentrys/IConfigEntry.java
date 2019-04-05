package com.dazo66.betterclient.config.configentrys;

/**
 * @author Dazo66
 */
public interface IConfigEntry<T> {

    String getKey();

    T getDefaultValue();

    void setDefaultValue(T defaultValueIn);

    T getValue();

    void setValue(T valueIn);

    String[] getComment();

    void setComment(String[] commentIn);

    String getLangKey();

    void setLangKey(String s);

    boolean getIsShowInGui();

    void setIsShowInGui(boolean b);

    CategoryConfigEntry getPath();

    void setPath(CategoryConfigEntry pathIn);

}
