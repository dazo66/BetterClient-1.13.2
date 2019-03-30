package com.dazo66.betterclient;

import com.electronwill.nightconfig.core.file.FileConfig;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.TypesafeMap;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.File;
import java.util.Optional;

/**
 * @author Dazo66
 */
@Mod(BetterClient.MODID)
public class BetterClient {
    public static final String MODID = "betterclient";
    public static final String NAME = "BetterClient";
    public static final String VERSION = "@version@";
    //instence
    public static BetterClient betterClient = new BetterClient();
    public static Logger logger;
    public static boolean DEBUG = isDEBUG();

    //TODO 需要检查 不知道是否可用
    public static FileConfig config = FileConfig.of(new File("config\\" + MODID + ".cfg"));


    static {
        config.load();
        logger = (org.apache.logging.log4j.core.Logger) LogManager.getLogger(MODID);
        if (DEBUG) {
            logger.setLevel(Level.ALL);
        } else {
            logger.setLevel(Level.WARN);
        }
    }

    public BetterClient() {
    }

    /**
     * feature register at here
     */
    public static void registerFunctions() {

    }


    //TODO 待调试
    private static boolean isDEBUG() {
        //launchArgs
        TypesafeMap map = Launcher.INSTANCE.blackboard();
        Optional<String> args = map.get(TypesafeMap.Key.getOrCreate(map, "--betterclient_debug", String.class));
        String s = args.get();
        return Boolean.valueOf(s);
    }

    //TODO 这个可能是preinit 有待考证
    @SubscribeEvent
    public void preInit(FMLCommonSetupEvent event) {

    }

    //TODO 可能是原来的init 有待考证
    @SubscribeEvent
    public void init(FMLClientSetupEvent event) {

    }


}
