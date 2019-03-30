package com.dazo66.fasttrading.config;

import java.util.ArrayList;

/**
 * @author Dazo66
 */
public class SimpleRecipeList extends ArrayList<ConfigJson.SimpleRecipe> {

    @Override
    public boolean add(ConfigJson.SimpleRecipe recipe) {
        if (!ConfigJson.isContain(recipe, this)) {
            return super.add(recipe);
        }
        return false;
    }

    @Override
    public boolean remove(Object recipe) {
        if (recipe instanceof ConfigJson.SimpleRecipe) {
            ConfigJson.SimpleRecipe simpleRecipe = ConfigJson.getRecipe((ConfigJson.SimpleRecipe) recipe, this);
            return simpleRecipe != null && super.remove(simpleRecipe);
        }else {
            return false;
        }

    }

}
