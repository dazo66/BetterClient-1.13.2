package com.dazo66.betterclient;

import com.dazo66.betterclient.coremod.IRegisterTransformer;
import com.dazo66.betterclient.coremod.MainTransformer;
import com.dazo66.betterclient.event.BetterClientEventHandler;
import com.dazo66.betterclient.functionsbase.IFunction;
import com.dazo66.betterclient.util.reflection.ReflectionHelper;
import com.dazo66.bugfix.BugFix;
import com.dazo66.elytrafix.ElytraFix;
import com.dazo66.fastcrafting.FastCrafting;
import com.dazo66.fasttrading.FastTrading;
import com.dazo66.prompt.Prompt;
import com.dazo66.shulkerboxshower.ShulkerBoxViewer;
import com.electronwill.nightconfig.core.file.FileConfig;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.TypesafeMap;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Dazo66
 */
@Mod(BetterClient.MODID)
public class BetterClient {
    public static final String MODID = "betterclient";
    public static final String NAME = "BetterClient";
    public static final String VERSION = "@version@";
    public static final String MCVersion = MainTransformer.getMCVERSION();
    //instence
    public static BetterClient betterClient = new BetterClient();
    public static Logger logger;
    public static boolean DEBUG = isDEBUG();

    //TODO 需要检查 不知道是否可用
    public static FileConfig config = FileConfig.of(new File("config\\" + MODID + ".cfg"));
    public static Set<IFunction> enableFeatures = FunctionsRegister.enableFunctions;
    public static Set<IFunction> disableFeatures = FunctionsRegister.disableFunctions;

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
        FunctionsRegister.register(new ShulkerBoxViewer());
        FunctionsRegister.register(new FastCrafting());
        FunctionsRegister.register(new FastTrading());
        FunctionsRegister.register(new Prompt());
        FunctionsRegister.register(new ElytraFix());
        FunctionsRegister.register(new BugFix());
    }

    public static void registerTransformerClass(MainTransformer mainTransformer) {
        for (IFunction feature : enableFeatures) {
            List<Class<? extends IRegisterTransformer>> transformers = feature.transformerClass();
            if (transformers == null) {
                continue;
            }
            for (Class<? extends IRegisterTransformer> transClass : transformers) {
                IRegisterTransformer iRegisterTransformer;
                try {
                    iRegisterTransformer = transClass.newInstance();
                    mainTransformer.register(iRegisterTransformer);
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("Cant register transformer class : " + transClass.getName());
                }
            }
        }
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
        ReflectionHelper.getInstance().addDefineFile("betterclient_rh.cfg");
        for (IFunction feature : enableFeatures) {
            feature.preInit(event);
            FunctionsRegister.configEntryInit(feature);
        }
    }

    //TODO 可能是原来的init 有待考证
    @SubscribeEvent
    public void init(FMLClientSetupEvent event) {
        FunctionsRegister.registerHandleClass(BetterClientEventHandler.class);
        for (IFunction feature : enableFeatures) {
            boolean regResult = FunctionsRegister.registerHandleClass(feature);
            if (!regResult) {
                logger.error("Registor %s handle class was failed", feature.eventHandlerClass().getSimpleName());
            }
            feature.init(event);
        }
    }

    @SubscribeEvent
    public void loadComplete(FMLLoadCompleteEvent event) {
        for (IFunction feature : enableFeatures) {
            feature.loadComplete(event);
        }
    }

    //TODO postInitEvent跑哪里去了
    @SubscribeEvent
    public void postInit(InterModProcessEvent event) {
        for (IFunction feature : enableFeatures) {
            feature.postInit(event);
        }
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent event) {
        for (IFunction feature : enableFeatures) {
            feature.serverStarting(event);
        }
    }

    @SubscribeEvent
    public void serverStarted(FMLServerStartedEvent event) {
        for (IFunction feature : enableFeatures) {
            feature.serverStarted(event);
        }
    }

    @SubscribeEvent
    public void serverAbortToStart(FMLServerAboutToStartEvent event) {
        for (IFunction feature : enableFeatures) {
            feature.serverAboutToStart(event);
        }
    }

    @SubscribeEvent
    public void serverStoped(FMLServerStoppedEvent event) {
        for (IFunction feature : enableFeatures) {
            feature.serverStoped(event);
        }
    }

    @SubscribeEvent
    public void serverStoping(FMLServerStoppingEvent event) {
        for (IFunction feature : enableFeatures) {
            feature.serverStoping(event);
        }
    }
}
