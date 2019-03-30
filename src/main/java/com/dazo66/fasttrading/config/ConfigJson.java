package com.dazo66.fasttrading.config;

import com.dazo66.fasttrading.util.ItemStackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;

import java.util.Collection;
import java.util.List;

/**
 * @author Dazo66
 */
public class ConfigJson {

    public static boolean isContain(SimpleRecipe recipe, List<SimpleRecipe> simpleRecipes) {
        for (SimpleRecipe simpleRecipe : simpleRecipes) {
            if (isRecipeEqualIgnoreCount(recipe, simpleRecipe)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isContain(MerchantRecipe recipe, List<SimpleRecipe> simpleRecipes) {
        for (SimpleRecipe simpleRecipe : simpleRecipes) {
            if (isRecipeEqualIgnoreCount(recipe, simpleRecipe)) {
                return true;
            }
        }
        return false;
    }

    public static SimpleRecipe getRecipe(MerchantRecipe recipe, List<SimpleRecipe> simpleRecipes) {
        for (SimpleRecipe simpleRecipe : simpleRecipes) {
            if (isRecipeEqualIgnoreCount(recipe, simpleRecipe)) {
                return simpleRecipe;
            }
        }
        return null;
    }

    public static boolean isRecipeEqualIgnoreCount(MerchantRecipe recipe, SimpleRecipe simpleRecipe) {
        ItemStack buy11 = simpleRecipe.buy1.getItemStack();
        ItemStack buy22 = simpleRecipe.buy2.getItemStack();
        ItemStack sell1 = simpleRecipe.sell.getItemStack();
        if (!ItemStackUtils.areItemEqualIgnoreCount(buy11, recipe.getItemToBuy())) {
            return false;
        } else if (!ItemStackUtils.areItemEqualIgnoreCount(sell1, recipe.getItemToSell())) {
            return false;
        }
        if (recipe.hasSecondItemToBuy()) {
            if (!ItemStackUtils.areItemEqualIgnoreCount(buy22, recipe.getSecondItemToBuy())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isRecipeEqual(SimpleRecipe recipe1, SimpleRecipe recipe2) {
        ItemStack buy11 = recipe2.buy1.getItemStack();
        ItemStack buy22 = recipe2.buy2.getItemStack();
        ItemStack sell1 = recipe2.sell.getItemStack();
        if (recipe2.lockPrice) {
            ItemStack recipeBuy1 = recipe1.buy1.getItemStack();
            ItemStack recipeSell = recipe1.sell.getItemStack();
            if (!ItemStackUtils.areItemEqualIgnoreCount(buy11, recipeBuy1) || buy11.getCount() != recipeBuy1.getCount()) {
                return false;
            } else if (!ItemStackUtils.areItemEqualIgnoreCount(sell1, recipeSell) || sell1.getCount() != recipeSell.getCount()) {
                return false;
            }
        }
        if (recipe1.hasSecondItemToBuy()) {
            ItemStack recipeBuy2 = recipe1.buy2.getItemStack();
            if (!ItemStackUtils.areItemEqualIgnoreCount(buy22, recipeBuy2) || !(buy22.getCount() == recipeBuy2.getCount())) {
                return false;
            }
        }
        return true;
    }

    public static SimpleRecipe getRecipe(SimpleRecipe recipe, List<SimpleRecipe> simpleRecipes) {
        for (SimpleRecipe simpleRecipe : simpleRecipes) {
            if (isRecipeEqualIgnoreCount(recipe, simpleRecipe)) {
                return simpleRecipe;
            }
        }
        return null;
    }

    public static boolean isRecipeEqualIgnoreCount(SimpleRecipe recipe, SimpleRecipe simpleRecipe) {
        try {
            ItemStack buy11 = simpleRecipe.buy1.getItemStack();
            ItemStack buy22 = simpleRecipe.buy2.getItemStack();
            ItemStack sell1 = simpleRecipe.sell.getItemStack();
            if (!ItemStackUtils.areItemEqualIgnoreCount(buy11, recipe.buy1.getItemStack())) {
                return false;
            } else if (!ItemStackUtils.areItemEqualIgnoreCount(sell1, recipe.sell.getItemStack())) {
                return false;
            }
            if (recipe.hasSecondItemToBuy()) {
                if (!ItemStackUtils.areItemEqualIgnoreCount(buy22, recipe.buy2.getItemStack())) {
                    return false;
                }
            }
            return true;
        }catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean isRecipeEqual(MerchantRecipe recipe, SimpleRecipe simpleRecipe) {
        ItemStack buy11 = simpleRecipe.buy1.getItemStack();
        ItemStack buy22 = simpleRecipe.buy2.getItemStack();
        ItemStack sell1 = simpleRecipe.sell.getItemStack();
        if (simpleRecipe.lockPrice) {
            if (!ItemStackUtils.areItemEqualIgnoreCount(buy11, recipe.getItemToBuy()) || buy11.getCount() != recipe.getItemToBuy().getCount()) {
                return false;
            } else if (!ItemStackUtils.areItemEqualIgnoreCount(sell1, recipe.getItemToSell()) || sell1.getCount() != recipe.getItemToSell().getCount()) {
                return false;
            }
        }
        if (recipe.hasSecondItemToBuy()) {
            if (!ItemStackUtils.areItemEqualIgnoreCount(buy22, recipe.getSecondItemToBuy()) || !(buy22.getCount() == recipe.getSecondItemToBuy().getCount())) {
                return false;
            }
        }
        return true;
    }

    public static class SimpleRecipe {

        public boolean lockPrice;
        SimpleItem buy1;
        SimpleItem buy2;
        SimpleItem sell;

        public SimpleRecipe(boolean lockPrice, MerchantRecipe recipe) {
            this.lockPrice = lockPrice;
            buy1 = new SimpleItem(recipe.getItemToBuy());
            buy2 = new SimpleItem(recipe.getSecondItemToBuy());
            sell = new SimpleItem(recipe.getItemToSell());
        }

        public boolean hasSecondItemToBuy() {
            return !"minecraft:air".equals(buy2.itemID);
        }

        public SimpleRecipe setLockPrice(boolean lockPrice) {
            this.lockPrice = lockPrice;
            return this;
        }

        private class SimpleItem {
            private String itemID;
            private String nbt;

            private SimpleItem(ItemStack itemStack) {
                itemID = itemStack.getItem().getRegistryName().toString();
                nbt = nbtToJson(itemStack.serializeNBT());
            }

            private String nbtToJson(NBTTagCompound nbt) {
                StringBuilder stringBuilder = new StringBuilder("{");
                Collection<String> collection = nbt.getKeySet();

                for (String s : collection) {
                    if (stringBuilder.length() != 1) {
                        stringBuilder.append(',');
                    }

                    stringBuilder.append(s).append(':').append(nbt.getTag(s));
                }

                return stringBuilder.append('}').toString();
            }

            public ItemStack getItemStack() {
                try {
                    return new ItemStack(JsonToNBT.getTagFromJson(nbt));
                } catch (NBTException e) {
                    e.printStackTrace();
                    return ItemStack.EMPTY;
                }
            }
        }
    }
}
