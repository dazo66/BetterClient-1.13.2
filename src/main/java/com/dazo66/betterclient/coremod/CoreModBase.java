package com.dazo66.betterclient.coremod;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Map;

/**
 * @author Dazo66
 */


@IFMLLoadingPlugin.MCVersion("")
@IFMLLoadingPlugin.SortingIndex(10000)
public class CoreModBase implements IFMLLoadingPlugin {

    public static boolean RUNTIME_DEOBF;
    public static File COREMOD_LOCATION;

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{MainTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    /**
     * data.put("mcLocation", mcDir);
     * data.put("coremodList", loadPlugins);
     * data.put("runtimeDeobfuscationEnabled", !deobfuscatedEnvironment);
     * data.put("coremodLocation", location);
     *
     * @param data has "mcLocation", "coremodList", "runtimeDeofuscationEnable", "coremodLocation"
     */

    @Override
    public void injectData(Map<String, Object> data) {
        COREMOD_LOCATION = (File) data.get("coremodLocation");
        RUNTIME_DEOBF = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
        MainTransformer.mainTransformer.transformerDebug();
    }

    @Override
    public String getAccessTransformerClass() {
        return AccessTransformer.class.getName();
    }
}
