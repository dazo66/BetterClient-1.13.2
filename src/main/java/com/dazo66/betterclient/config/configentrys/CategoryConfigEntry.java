package com.dazo66.betterclient.config.configentrys;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.List;

/**
 * @author Dazo66
 */
public class CategoryConfigEntry extends AbstractConfigEntry<List<AbstractConfigEntry>> {

    CategoryConfigEntry parent = null;

    public CategoryConfigEntry(CategoryConfigEntry parentIn, String keyIn) {
        super(keyIn, null, null, null);
        parent = parentIn;
    }

    public void addValue(AbstractConfigEntry entry) {
        value.add(entry);
    }

    public CategoryConfigEntry getParent() {
        return parent;
    }

    public CategoryConfigEntry getSubCategory(String name) {
        CategoryConfigEntry c = findSubCategory(name);
        if (c == null) {
            CategoryConfigEntry c1 = new CategoryConfigEntry(this, name);
            this.addValue(c1);
            return c1;
        }else {
            return c;
        }
    }

    private CategoryConfigEntry findSubCategory(String name) {
        for (AbstractConfigEntry entry : value) {
            if (entry instanceof CategoryConfigEntry && entry.key.equals(name)) {
                return (CategoryConfigEntry) entry;
            }
        }
        return null;
    }
}


