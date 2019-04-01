package com.dazo66.fasttrading.util;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraft.client.util.InputMappings;

/**
 * @author Dazo66
 */
public class KeyLoader {
    public static KeyBinding key_F4;

    public KeyLoader() {
        //TODO 修改了Keyboard.KEY_F4
        KeyLoader.key_F4 = new KeyBinding("FastTrading ON-OFF", InputMappings.getInputByName("key.keyboard.f4").getKeyCode(), "FastTrading");
        ClientRegistry.registerKeyBinding(KeyLoader.key_F4);
    }
}