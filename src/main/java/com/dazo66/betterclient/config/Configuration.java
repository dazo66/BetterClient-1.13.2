package com.dazo66.betterclient.config;

import com.dazo66.betterclient.config.configentrys.AbstractConfigEntry;
import com.dazo66.betterclient.config.configentrys.CategoryConfigEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dazo66
 */
public class Configuration {

    public Logger logger = LogManager.getLogger("Config");
    ConfigWriter writer;
    ConfigReader reader;
    private List<AbstractConfigEntry> loadedConfig = new ArrayList<>();
    private List<AbstractConfigEntry> registedConfig = new ArrayList<>();

    public Configuration(String configFileName){
        this(new File(configFileName));
    }

    public Configuration(File file){
        if (!file.exists() || !file.isFile()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                reader = new ConfigReader(file);
                writer = new ConfigWriter(file);
                load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void load() {
        try {
            loadedConfig = reader.load();
            reRegisterEntries();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void save() {
        try {
            writer.save(registedConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reRegisterEntries(){
        List<AbstractConfigEntry> list = new ArrayList<>(registedConfig);
        registedConfig.clear();
        for (AbstractConfigEntry entry : list) {
            register(entry);
        }
    }

    public void register(AbstractConfigEntry entry) {
        if (!(entry instanceof CategoryConfigEntry)) {
            AbstractConfigEntry entry1 = findEntryByKeyFromList(entry.getKey(), entry.getClass(), loadedConfig);
            if (entry1 != null) {
                entry.setValue(entry1);
            }
        }
        if (entry.getPath() == null) {
            registedConfig.add(entry);
        } else {
            if (!registedConfig.contains(entry.getPath())) {
                register(entry.getPath());
            }
        }
    }

    public <T extends AbstractConfigEntry> T findEntryByKey(String key, Class<T> clazz){
        return findEntryByKeyFromList(key, clazz, registedConfig);
    }

    private  <T extends AbstractConfigEntry> T findEntryByKeyFromList(String key, Class<T> clazz, List<AbstractConfigEntry> list){
        for (AbstractConfigEntry entry : list) {
            if (key.equals(entry.getKey()) && entry.getClass() == clazz) {
                return (T) entry;
            }
            if (entry instanceof CategoryConfigEntry) {
                return findEntryByKeyFromList(key, clazz, ((CategoryConfigEntry) entry).getValue());
            }
        }
        return null;
    }

}
