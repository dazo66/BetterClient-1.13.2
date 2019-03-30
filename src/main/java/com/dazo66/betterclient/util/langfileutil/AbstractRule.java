package com.dazo66.betterclient.util.langfileutil;

/**
 * @author Dazo66
 */
public abstract class AbstractRule implements IRule {


    @Override
    public abstract String getKeyword();

    @Override
    public abstract boolean isReverse();

    @Override
    public abstract String getRawRule();

    @Override
    public abstract boolean isAllow(String key);

    @Override
    public abstract void compile(String rawRule);

    @Override
    public boolean equals(Object o) {
        if (o instanceof AbstractRule || o instanceof String) {
            return getRawRule().equals(o.toString());
        }
        return false;
    }

    public String saveToString() {
        return toString();
    }

    @Override
    public String toString() {
        return getRawRule();
    }
}
