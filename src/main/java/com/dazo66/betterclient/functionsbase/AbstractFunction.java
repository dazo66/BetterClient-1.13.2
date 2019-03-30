package com.dazo66.betterclient.functionsbase;

import com.dazo66.betterclient.coremod.IRegisterTransformer;
import com.google.common.base.Strings;
import net.minecraftforge.fml.common.event.*;

import java.util.List;

/**
 * @author Dazo66
 */
public abstract class AbstractFunction implements IFunction {

    @Override
    public List<Class<? extends IRegisterTransformer>> transformerClass() {
        return null;
    }

    @Override
    public List<IFunction> getSubFunctions() {
        return null;
    }

    @Override
    public void loadComplete(FMLLoadCompleteEvent event) {
    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
    }

    @Override
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
    }

    @Override
    public void serverStoping(FMLServerStoppingEvent event) {
    }

    @Override
    public void serverStoped(FMLServerStoppedEvent event) {
    }

    public boolean hasSubFunction() {
        List<IFunction> list = getSubFunctions();
        return list != null && !list.isEmpty();
    }

    @Override
    public int compareTo(IFunction function) {
        return this.getID().compareTo(function.getID());
    }

    @Override
    public String toString() {
        String nullString = "null";
        String id = getID();
        String name = getName();
        String version = getVersion();
        String author = getAuthor();
        id = Strings.isNullOrEmpty(id) ? nullString : id;
        name = Strings.isNullOrEmpty(name) ? nullString : name;
        version = Strings.isNullOrEmpty(version) ? nullString : version;
        author = Strings.isNullOrEmpty(author) ? nullString : author;
        return String.format("Function: {ID: %s, Name: %s, Version: %s, Author: %s}", id, name, version, author);
    }

}
