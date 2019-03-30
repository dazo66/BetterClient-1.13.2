package com.dazo66.fastcrafting.transformer;

import com.dazo66.betterclient.coremod.IRegisterTransformer;
import com.dazo66.betterclient.coremod.MainTransformer;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dazo66
 */
public class SendRecipePacketHook1 implements IRegisterTransformer {

    private List<String> methodInfo = Arrays.asList("func_193945_a", "(Lnet/minecraft/item/crafting/IRecipe;Lnet/minecraft/client/gui/recipebook/RecipeList;)V");

    @Override
    public String getMcVersion() {
        return "[1.12,1.12.1)";
    }

    @Override
    public List<String> getClassName() {
        return Arrays.asList("net/minecraft/client/gui/recipebook/GuiRecipeBook", "bnm");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return MainTransformer.clearMethod(name, transformedName, basicClass, methodInfo);
    }
}