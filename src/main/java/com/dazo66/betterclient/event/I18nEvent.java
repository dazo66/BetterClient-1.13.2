package com.dazo66.betterclient.event;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * this event only tryInvoke at development environment.
 *
 * @author Dazo66
 */
public class I18nEvent extends Event {

    private String langKey;
    private Object[] args;

    public I18nEvent(String langKeyIn, Object[] argsIn) {
        langKey = langKeyIn;
        args = argsIn;
    }

    public static I18nEvent post(String langKeyIn, Object[] args) {
        I18nEvent event = new I18nEvent(langKeyIn, args);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static String test(String langKeyIn, Object[] args) {
        I18nEvent event = post(langKeyIn, args);
        langKeyIn = event.getLangKey();
        args = event.getArgs();
        return langKeyIn;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
