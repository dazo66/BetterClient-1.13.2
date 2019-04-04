package com.dazo66.test;

public interface Itest<T> {
    T getBoolean();
}

class test1 implements Itest<Boolean> {
    boolean b;
    test1(boolean bIn){
        b = bIn;
    }

    @Override
    public Boolean getBoolean() {
        return b;
    }
}