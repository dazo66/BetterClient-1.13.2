package com.dazo66.betterclient.config.configentrys;

import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Dazo66
 */
public class CategoryConfigEntry extends AbstractConfigEntry<List<AbstractConfigEntry>> {

    public CategoryConfigEntry(@Nonnull String keyIn, String langKeyIn, String[] commentIn) {
        super(keyIn, langKeyIn, Lists.newArrayList(), commentIn);
    }

    public boolean containEntry(AbstractConfigEntry entry){
        return this.getValue().contains(entry);
    }

    public void addValue(AbstractConfigEntry entry) {
        getValue().add(entry);
        if (entry != this) {
            entry.setPath(this);
        }
    }

    public CategoryConfigEntry getSubCategory(@Nonnull String keyIn, String langKeyIn, String[] commentIn) {
        CategoryConfigEntry c = findSubCategory(keyIn);
        if (c == null) {
            CategoryConfigEntry c1 = new CategoryConfigEntry(keyIn, langKeyIn, commentIn);
            this.addValue(c1);
            return c1;
        } else {
            return c;
        }
    }

    @Override
    public List<AbstractConfigEntry> getDefaultValue() {
        return getValue();
    }

    private CategoryConfigEntry findSubCategory(@Nonnull String name) {
        for (AbstractConfigEntry entry : getValue()) {
            if (entry instanceof CategoryConfigEntry && entry.key.equals(name)) {
                return (CategoryConfigEntry) entry;
            }
        }
        return null;
    }
}


