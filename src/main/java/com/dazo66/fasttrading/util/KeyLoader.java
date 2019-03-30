package com.dazo66.fasttrading.util;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

/**
 * @author Dazo66
 */
public class KeyLoader {
    public static KeyBinding key_F4;

    public KeyLoader() {
        KeyLoader.key_F4 = new KeyBinding("FastTrading ON-OFF", Keyboard.KEY_F4, "FastTrading");
        ClientRegistry.registerKeyBinding(KeyLoader.key_F4);
    }
}