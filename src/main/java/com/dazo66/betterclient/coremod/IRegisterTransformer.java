package com.dazo66.betterclient.coremod;

import cpw.mods.modlauncher.ClassTransformer;

import java.util.List;

/**
 * @author Dazo66
 */
public interface IRegisterTransformer extends ClassTransformer {

    /**
     * the mc version of this class transformer can use
     * version format {@link net.minecraftforge.fml.common.versioning.VersionRange})
     *
     * @return a String of version
     **/
    String getMcVersion();

    /**
     * what class you want to transformer
     *
     * @return a class name list
     */
    List<String> getClassName();
}
