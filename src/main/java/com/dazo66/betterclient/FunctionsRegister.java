package com.dazo66.betterclient;

import com.dazo66.betterclient.config.configentrys.BooleanConfigEntry;
import com.dazo66.betterclient.config.configentrys.IConfigEntry;
import com.dazo66.betterclient.functionsbase.AbstractFunction;
import com.dazo66.betterclient.functionsbase.IFunction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Dazo66
 */
public class FunctionsRegister {

    public static Set<IFunction> allFunctions = new TreeSet<>();
    public static Set<IFunction> primaryFunctions = new TreeSet<>();
    public static Set<IFunction> enableFunctions = new TreeSet<>();
    public static Set<IFunction> disableFunctions = new TreeSet<>();

    public static void register(IFunction functionIn) {
        primaryFunctions.add(functionIn);
        registerPrivate(functionIn);
    }

    private static void registerPrivate(IFunction functionIn) {
        allFunctions.add(functionIn);
        if (canLoad(functionIn)) {
            enableFunctions.add(functionIn);
            if (((AbstractFunction) functionIn).hasSubFunction()) {
                for (IFunction function : functionIn.getSubFunctions()) {
                    registerPrivate(function);
                }
            }
        } else {
            disableFunctions.add(functionIn);
        }
    }

    public static boolean registerHandleClass(IFunction function) {
        if (function.eventHandlerClass() == null) {
            return false;
        }
        Class handler = function.eventHandlerClass();
        for (Method method : handler.getMethods()) {
            if (method.isAnnotationPresent(SubscribeEvent.class)) {
                if (Modifier.isStatic(method.getModifiers())) {
                    BetterClient.logger.error("Please don't use static in handler class. Method : %s will not register in event bus", method.getName());
                }
            }
        }
        try {
            MinecraftForge.EVENT_BUS.register(function.eventHandlerClass().newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            return false;
        }
        return true;
    }

    public static boolean registerHandleClass(Class clazz) {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(SubscribeEvent.class)) {
                if (Modifier.isStatic(method.getModifiers())) {
                    BetterClient.logger.error("Please don't use static in handler class. Method : %s will not register in event bus", method.getName());
                }
            }
        }
        try {
            MinecraftForge.EVENT_BUS.register(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            return false;
        }
        return true;
    }

    public static void configEntryInit(IFunction function) {
        if (function.getConfigEntrys() == null) {
            return;
        }
        for (IConfigEntry configEntry : function.getConfigEntrys()) {
            configEntry.getValue();
        }

    }

    private static boolean canLoad(IFunction function) {
        BooleanConfigEntry isEnable = new BooleanConfigEntry("enable", "enable", true, function, "This function is enable to load or not.");
        return isEnable.getValue();
    }

}
