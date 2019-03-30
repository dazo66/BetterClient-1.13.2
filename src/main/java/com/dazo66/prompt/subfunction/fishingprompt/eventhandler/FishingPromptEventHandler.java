package com.dazo66.prompt.subfunction.fishingprompt.eventhandler;

import com.dazo66.betterclient.event.FishingEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.dazo66.prompt.util.PromptUtils.playDingSound;

/**
 * @author Dazo66
 */
public class FishingPromptEventHandler {

    private Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onFishCaught(FishingEvent.FishCaughtEvent event){

        playDingSound("prompt.fishprompt.subtitles.fishcaught");

    }
}
