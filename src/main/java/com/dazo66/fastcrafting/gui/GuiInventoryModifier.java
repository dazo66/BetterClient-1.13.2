package com.dazo66.fastcrafting.gui;

import com.dazo66.fastcrafting.crafting.CraftingHelper;
import com.dazo66.fastcrafting.crafting.GuiInventoryEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.RecipeBookPage;
import net.minecraft.inventory.Container;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;

/**
 * @author Dazo66
 */
public class GuiInventoryModifier {

    private GuiInventoryEnum guiType = null;
    private final RecipeBookPage bookPage;
    private Minecraft mc = Minecraft.getMinecraft();
    private GuiContainer gui ;
    private GuiRecipeBook guiRecipeBook;
    private CraftingHelper helper;
    private Container inventorySlots;

    public GuiInventoryModifier(GuiContainer guiIn) {
        gui = guiIn;
        if (gui instanceof GuiCrafting) {
            guiType = GuiInventoryEnum.CRAFTING_TABLE;
        } else if (gui instanceof GuiInventory) {
            guiType = GuiInventoryEnum.INVENTORY;
        }
        if (guiType == GuiInventoryEnum.CRAFTING_TABLE) {
            guiRecipeBook = ((GuiCrafting) gui).recipeBookGui;
        } else if (guiType == GuiInventoryEnum.INVENTORY) {
            guiRecipeBook = ((GuiInventory) gui).recipeBookGui;
        }
        inventorySlots = gui.inventorySlots;
        bookPage = guiRecipeBook.recipeBookPage;
        helper = new CraftingHelper(gui, this);
    }

    public void mouseClicked() {
        IRecipe iRecipe = bookPage.getLastClickedRecipe();
        helper.setIRecipe(iRecipe);
        NonNullList<Ingredient> list;
        if (iRecipe == null) {
            return;
        }
        if (iRecipe instanceof ShapelessRecipes || iRecipe instanceof ShapedRecipes) {
            list = iRecipe.getIngredients();
        } else {
            return;
        }
        helper.setList(list);
        helper.setType(guiType);
        if (!bookPage.getLastClickedRecipeList().isCraftable(iRecipe)) {
            guiRecipeBook.setupGhostRecipe(iRecipe, inventorySlots.inventorySlots);
            return;
        }
        helper.craftMode(GuiContainer.isShiftKeyDown(), GuiContainer.isCtrlKeyDown());
//        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public Container getInventorySlots() {
        return inventorySlots;
    }

    public GuiInventoryEnum getGuiType() {
        return guiType;
    }
}
