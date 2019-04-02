package com.dazo66.betterclient.config;

import com.dazo66.betterclient.config.configentrys.AbstractConfigEntry;
import com.dazo66.betterclient.config.configentrys.BooleanConfigEntry;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;

/**
 * @author Dazo66
 */
public class Assembler {

    private final static String SEPARATOR = "=";


    public static AbstractConfigEntry assenbleEntry(Collection<String> comment, Collection<String> body) {

    }

    private static <V> Pair<String, V> assenbleToOne(String s, Class<? extends V> clazz) {
        String raw = s.trim();
        int index = raw.lastIndexOf(SEPARATOR);
        String left = raw.substring(0, index);
        String right = raw.substring(index + 1);
        return new ImmutablePair<>(left, stringToObj(right, clazz));
    }

    @SuppressWarnings("unchecked")
    private static <T> T stringToObj(String s, Class<? extends T> clazz) {
        if (clazz == Boolean.class) {
            try {
                return (T) Boolean.valueOf(s);
            } catch (Exception e) {
                throw new IllegalValueException(String.format("Can't Conversion String:%s to [Boolean]", s));
            }
        } else if (clazz == Integer.class) {
            try {
                return (T) Integer.valueOf(s);
            } catch (Exception e) {
                throw new IllegalValueException(String.format("Can't Conversion String:%s to [Integer]", s));
            }
        } else if (clazz == Double.class) {
            try {
                return (T) Double.valueOf(s);
            } catch (Exception e) {
                throw new IllegalValueException(String.format("Can't Conversion String:%s to [Double]", s));
            }
        } else if (clazz == Float.class) {
            try {
                return (T) Float.valueOf(s);
            } catch (Exception e) {
                throw new IllegalValueException(String.format("Can't Conversion String:%s to [Float]", s));
            }
        } else if (clazz == String.class) {
            try {
                return (T) s;
            } catch (Exception e) {
                throw new IllegalValueException(String.format("Can't Conversion String:%s to [String]", s));
            }
        }
        throw new IllegalValueException(String.format("The Type Of String:%s Are Not In [Boolean, Integer, Double, Float, String]", s));
    }

    private static class IllegalValueException extends IllegalArgumentException {

        IllegalValueException(String s) {
            super(s);
        }
    }


}
