package com.dazo66.prompt.subfunction.furnaceprompt.eventhandler;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.dazo66.prompt.util.PromptUtils.playDingSound;

/**
 * @author Dazo66
 */
public class FurnacePromptEventHandler {

    private Minecraft mc = Minecraft.getMinecraft();
    private BlockPos furnacePos;
    private List<BlockPos> checkList = new ArrayList<>();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        String state = null;
        if (furnacePos != null && !checkList.contains(furnacePos)) {
            try {
                state = mc.world.getBlockState(furnacePos).toString();
            }catch (Exception ignore) {
            }
            if (state != null && state.contains("lit_furnace")) {
                checkList.add(furnacePos);
                furnacePos = null;
            }
        }
        Iterator<BlockPos> iterable = checkList.iterator();
        while (iterable.hasNext()) {
            state = mc.world.getBlockState(iterable.next()).toString();
            if (!state.contains("furnace")) {
                iterable.remove();
            } else {
                if (!state.contains("lit")) {
                    iterable.remove();
                    playDingSound("prompt.furnaceprompt.subtitles.furnacefinished");
                }
            }
        }
    }

    @SubscribeEvent
    public void onRightClickFurnace(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos = event.getPos();
        String state = mc.world.getBlockState(pos).toString();
        if (state.contains("furnace")) {
            furnacePos = pos;
        }
    }





}
