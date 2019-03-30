package com.dazo66.fastcrafting.eventhandler;

import com.dazo66.fastcrafting.crafting.CraftingLoader;
import com.dazo66.fastcrafting.gui.GuiInventoryModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Dazo66
 */
public class FastCraftingEventHandler {

    @SubscribeEvent
    public void guiOpenEvent(GuiOpenEvent event) {
        if (event.getGui() instanceof IRecipeShownListener) {
            CraftingLoader.unlockRecipe();
        }
    }
}
