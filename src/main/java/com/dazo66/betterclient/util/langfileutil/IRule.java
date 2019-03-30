package com.dazo66.betterclient.util.langfileutil;

/**
 * @author Dazo66
 */
public interface IRule {

    String getKeyword();

    boolean isReverse();

    String getRawRule();

    boolean isAllow(String key);

    void compile(String rawRule);

}
