package com.dazo66.betterclient.config.configentrys;

import com.dazo66.betterclient.functionsbase.IFunction;
import net.minecraftforge.common.config.Property;

/**
 * @author Dazo66
 */
public interface IConfigEntry<T> {

    IFunction getOwner();

    String getKey();

    String getLangKey();

    T getDefaultValue();

    String getComment();

    T getValue();

    void setValue(T valueIn);

    Property getProperty();

}
