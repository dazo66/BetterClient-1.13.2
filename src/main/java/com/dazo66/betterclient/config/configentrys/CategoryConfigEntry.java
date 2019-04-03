package com.dazo66.betterclient.config.configentrys;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author Dazo66
 */
public class CategoryConfigEntry extends AbstractConfigEntry<List<AbstractConfigEntry>> {

    public CategoryConfigEntry(String keyIn, String langKeyIn, String[] commentIn) {
        super(keyIn, langKeyIn, Lists.newArrayList(), commentIn);
    }

    public void addValue(AbstractConfigEntry entry) {
        entry.setPath(this);
        value.add(entry);
    }

    public CategoryConfigEntry getSubCategory(String keyIn, String langKeyIn, String[] commentIn) {
        CategoryConfigEntry c = findSubCategory(keyIn);
        if (c == null) {
            CategoryConfigEntry c1 = new CategoryConfigEntry(keyIn, langKeyIn, commentIn);
            this.addValue(c1);
            return c1;
        }else {
            return c;
        }
    }

    @Override
    public List<AbstractConfigEntry> getDefaultValue(){
        return value;
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


