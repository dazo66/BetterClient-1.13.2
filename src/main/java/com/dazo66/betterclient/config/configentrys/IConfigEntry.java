package com.dazo66.betterclient.config.configentrys;

/**
 * @author Dazo66
 */
public interface IConfigEntry<T> {

    String getKey();

    T getDefaultValue();

    T getValue();

    void setValue(T valueIn);

    String[] getComment();

    String getLangKey();

    void setIsShowInGui(boolean b);

    boolean getIsShowInGui();

    CategoryConfigEntry getPath();

    void setPath(CategoryConfigEntry pathIn);

}
