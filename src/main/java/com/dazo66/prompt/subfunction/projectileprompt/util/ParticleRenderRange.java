package com.dazo66.prompt.subfunction.projectileprompt.util;

/**
 * @author Dazo66
 */
public class ParticleRenderRange {

    private static double range = 1024.0d;

    public static double getRange() {
        return range;
    }

    public static void setRange(double range) {
        ParticleRenderRange.range = range;
    }

    public static void resetRange(){
        ParticleRenderRange.setRange(1024.0d);
    }
}
