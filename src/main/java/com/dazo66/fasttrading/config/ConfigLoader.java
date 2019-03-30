package com.dazo66.fasttrading.config;

import com.dazo66.betterclient.config.configentrys.StringArrayConfigEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * @author Dazo66
 */
public class ConfigLoader {

    public SimpleRecipeList recipeList = new SimpleRecipeList();
    public ArrayList<ConfigJson.SimpleRecipe> oldList;
    private StringArrayConfigEntry configEntry;
    private Gson gson = new GsonBuilder().create();

    public ConfigLoader(StringArrayConfigEntry configEntryIn) {
        configEntry = configEntryIn;
        load();
    }

    @SuppressWarnings("unchecked")
    public void load() {
        String[] rawRecipes = configEntry.getValue();
        recipeList.clear();
        for (String rawRecipe : rawRecipes) {
            ConfigJson.SimpleRecipe simpleRecipe = gson.fromJson(rawRecipe, ConfigJson.SimpleRecipe.class);
            recipeList.add(simpleRecipe);
        }
        oldList = (ArrayList<ConfigJson.SimpleRecipe>) recipeList.clone();
    }

    public void onSave() {
        if (oldList.equals(recipeList)) {
            return;
        }
        int size = recipeList.size();
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = gson.toJson(recipeList.get(i));
        }
        configEntry.setValue(strings);
    }
    //
    //    public void addRecipe(boolean isLock, MerchantRecipe recipe){
    //        ConfigJson.SimpleRecipe simpleRecipe = new ConfigJson.SimpleRecipe(isLock, recipe);
    //        recipeList.add(simpleRecipe);
    //        int size = recipeList.size();
    //        String[] strings = new String[size];
    //        for (int i = 0; i < size; i++) {
    //            strings[i] = gson.toJson(recipeList.get(i));
    //        }
    //        configEntry.setValue(strings);
    //
    //    }
    //
    //    public void removeRecipe(MerchantRecipe recipe) {
    //
    //    }
}