package com.dazo66.betterclient.util.langfileutil;

import java.io.File;

/**
 * @author Dazo66
 */
public class LangFileUpdater {

    private LangEntryList[] list;

    public LangFileUpdater(File... files) {
        int length = files.length;
        list = new LangEntryList[length];
        for (int i = 0; i < length; i++) {
            list[i] = new LangEntryList(files[i]);
        }
        addShutDownHook();
    }

    public void put(String key, String value) {
        if (null == value) {
            value = key;
        }
        for (LangEntryList entryList : list) {
            entryList.put(key, value);
        }
    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (LangEntryList entryList : list) {
                entryList.save();
            }
        }));
    }


}