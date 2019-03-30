package com.dazo66.betterclient.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Dazo66
 */
public class BetterClientUtils {

    public static SoundEvent getRegisteredSoundEvent(String id) {
        SoundEvent soundevent = SoundEvent.REGISTRY.getObject(new ResourceLocation(id));

        if (soundevent == null) {
            throw new IllegalStateException("Invalid Sound requested: " + id);
        } else {
            return soundevent;
        }
    }

    public static <T> boolean setFinalField(Class<T> clazz, T object, String fieldName, Object newFieldValue) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(object, newFieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

}
