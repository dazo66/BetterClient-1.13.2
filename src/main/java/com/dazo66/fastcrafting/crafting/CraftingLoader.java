package com.dazo66.fastcrafting.crafting;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author Dazo66
 */
public class CraftingLoader {

    private static List<IRecipe> myRecipe = new ArrayList<>();
    private static HashMap<String, IRecipe> allRecipemap = new HashMap<>();

    public static void unlockRecipe() {
        Minecraft mc = Minecraft.getMinecraft();
        RecipeBook recipebook = mc.player.getRecipeBook();
        boolean flag = false;
        for (IRecipe iRecipe : CraftingManager.REGISTRY) {
            if (!recipebook.isUnlocked(iRecipe) && !iRecipe.isDynamic()) {
                recipebook.unlock(iRecipe);
                recipebook.markSeen(iRecipe);
                flag = true;
            }
        }
        if (flag) {
            for (RecipeList list : RecipeBookClient.ALL_RECIPES) {
                list.updateKnownRecipes(recipebook);
            }
        }
    }


    public static void myRecipe() {
        Minecraft mc = Minecraft.getMinecraft();

        if (!mc.isSingleplayer()) {
            RecipeBook recipebook = mc.player.getRecipeBook();
            boolean flag = false;
            for (IRecipe iRecipe : myRecipe) {
                if (!recipebook.isUnlocked(iRecipe)) {
                    recipebook.unlock(iRecipe);
                    flag = true;
                }
            }
            if (flag) {
                for (RecipeList list : RecipeBookClient.ALL_RECIPES) {
                    list.updateKnownRecipes(recipebook);
                }
            }
        } else {
            IntegratedServer server = Minecraft.getMinecraft().getIntegratedServer();
            server.getPlayerList().getPlayerByUsername(mc.player.getName()).unlockRecipes(myRecipe);
        }
    }

    public static boolean canCraft(IRecipe iRecipe, int i) {
        return new RecipeItemHelper().canCraft(iRecipe, null) && iRecipe.canFit(i, i);
    }

    public static IRecipe getIRecipeForName(String s) {
        if (allRecipemap.isEmpty()) {
            initRecipeMap();
        }
        return allRecipemap.get(s);
    }

    private static void initRecipeMap() {
        for (ResourceLocation r : CraftingManager.REGISTRY.getKeys()) {
            allRecipemap.put(r.toString(), CraftingManager.getRecipe(r));
        }
    }

    private static int getRecipeId(@Nullable IRecipe recipe) {
        int ret = CraftingManager.REGISTRY.getIDForObject(recipe);
        if (ret == -1) {
            ret = ((net.minecraftforge.registries.ForgeRegistry<IRecipe>) net.minecraftforge.fml.common.registry.ForgeRegistries.RECIPES).getID(recipe.getRegistryName());
            if (ret == -1) {
                throw new IllegalArgumentException(String.format("Attempted to get the ID for a unknown recipe: %s Name: %s", recipe, recipe.getRegistryName()));
            }
        }
        return ret;
    }

    private static RecipeList getRecipeList(IRecipe iRecipe) {
        for (RecipeList list : RecipeBookClient.ALL_RECIPES) {
            list.getRecipes().contains(iRecipe);
            return list;
        }
        return null;
    }


    public static void init() {
        CraftingHelper.register(new ResourceLocation("fastcrafting:crafting_shaped"), (IRecipeFactory) (context, json) -> {
            String group = JsonUtils.getString(json, "group", "");
            //if (!group.isEmpty() && group.indexOf(':') == -1)
            //    group = context.getModId() + ":" + group;

            Map<Character, Ingredient> ingMap = Maps.newHashMap();
            for (Map.Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "key").entrySet()) {
                if (entry.getKey().length() != 1) {
                    throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
                }
                if (" ".equals(entry.getKey())) {
                    throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
                }
                ingMap.put(entry.getKey().toCharArray()[0], CraftingHelper.getIngredient(entry.getValue(), context));
            }
            ingMap.put(' ', Ingredient.EMPTY);

            JsonArray patternJ = JsonUtils.getJsonArray(json, "pattern");

            if (patternJ.size() == 0) {
                throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
            }
            if (patternJ.size() > 3) {
                throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
            }
            String[] pattern = new String[patternJ.size()];
            for (int x = 0; x < pattern.length; ++x) {
                String line = JsonUtils.getString(patternJ.get(x), "pattern[" + x + "]");
                if (line.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                }
                if (x > 0 && pattern[0].length() != line.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }
                pattern[x] = line;
            }

            NonNullList<Ingredient> input = NonNullList.withSize(pattern[0].length() * pattern.length, Ingredient.EMPTY);
            Set<Character> keys = Sets.newHashSet(ingMap.keySet());
            keys.remove(' ');

            int x = 0;
            for (String line : pattern) {
                for (char chr : line.toCharArray()) {
                    Ingredient ing = ingMap.get(chr);
                    if (ing == null) {
                        throw new JsonSyntaxException("Pattern references symbol '" + chr + "' but it's not defined in the key");
                    }
                    input.set(x++, ing);
                    keys.remove(chr);
                }
            }

            if (!keys.isEmpty()) {
                throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keys);
            }
            ItemStack result = DeserializeItem.deserializeItem(JsonUtils.getJsonObject(json, "result"), context);
            ShapedRecipes s = new ShapedRecipes(group, pattern[0].length(), pattern.length, input, result);
            myRecipe.add(s);
            return s;
        });

        CraftingHelper.register(new ResourceLocation("fastcrafting:crafting_shapeless"), (IRecipeFactory) (context, json) -> {
            String group = JsonUtils.getString(json, "group", "");

            NonNullList<Ingredient> ings = NonNullList.create();
            for (JsonElement ele : JsonUtils.getJsonArray(json, "ingredients")) {
                ings.add(CraftingHelper.getIngredient(ele, context));
            }
            if (ings.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            }
            if (ings.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            }

            ItemStack itemstack = DeserializeItem.deserializeItem(JsonUtils.getJsonObject(json, "result"), context);
            ShapelessRecipes s = new ShapelessRecipes(group, itemstack, ings);
            myRecipe.add(s);
            return s;
        });
    }


}
