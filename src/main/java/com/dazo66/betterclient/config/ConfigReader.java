package com.dazo66.betterclient.config;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dazo66
 */
public class ConfigReader {

    private final FileReader reader;
    private LineIterator lineIterator;
    private List<String> commentBuffer = new ArrayList<>();

    public ConfigReader(FileReader readerIn) {
        reader = readerIn;
        lineIterator = IOUtils.lineIterator(reader);

    }

    public ConfigReader(File file) throws FileNotFoundException {
        this(new FileReader(file));
    }










}
