package com.dazo66.betterclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Dazo66
 */
public class Configuration {

    Gson gson = new GsonBuilder().create();


    public Configuration(String configFileName){
        this(new File(configFileName));
    }

    public Configuration(File file){
        if (!file.exists() || !file.isFile()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            gson.fromJson(new FileReader(file), this.getClass());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



}
