package com.dazo66.prompt.util;

import com.dazo66.betterclient.client.audio.FakeSubtitleSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.init.SoundEvents;

/**
 * @author Dazo66
 */
public class PromptUtils {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static void playDingSound(String subtitle) {
        ISound sound = FakeSubtitleSound.getRecord(SoundEvents.ENTITY_ARROW_HIT_PLAYER, 0.5f, 0.05F, ISound.AttenuationType.LINEAR, (float) mc.player.posX, (float) mc.player.posY, (float) mc.player.posZ, subtitle);
        mc.getSoundHandler().playSound(sound);
    }

}
