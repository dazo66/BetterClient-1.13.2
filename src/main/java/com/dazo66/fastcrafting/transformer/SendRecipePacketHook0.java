package com.dazo66.fastcrafting.transformer;

import com.dazo66.betterclient.coremod.IRegisterTransformer;
import com.dazo66.betterclient.coremod.MainTransformer;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dazo66
 */
public class SendRecipePacketHook0 implements IRegisterTransformer {

    private final List<String> methodInfo = Arrays.asList("func_194338_a", "a", "(ILakt;ZLaed;)V", "(ILnet/minecraft/item/crafting/IRecipe;ZLnet/minecraft/entity/player/EntityPlayer;)V");

    @Override
    public String getMcVersion() {
        return "[1.12.1,1.12.2]";
    }

    @Override
    public List<String> getClassName() {
        return Arrays.asList("net.minecraft.client.multiplayer.PlayerControllerMP");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return MainTransformer.clearMethod(name, transformedName, basicClass, methodInfo);
    }
}
