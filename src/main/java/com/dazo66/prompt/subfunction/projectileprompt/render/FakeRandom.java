package com.dazo66.prompt.subfunction.projectileprompt.render;

import java.util.Random;

/**
 * @author Dazo66
 */
public class FakeRandom extends Random {

    FakeRandom(){
        super();
    }

    @Override
    synchronized public double nextGaussian() {
        return 0.5d;
    }

    @Override
    public float nextFloat() {
        return 0.5f;
    }

    @Override
    public double nextDouble() {
        return 0.5d;
    }

    @Override
    public int nextInt(int bound) {
        return bound / 2;
    }



}
