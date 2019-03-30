package com.dazo66.betterclient.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;

/**
 * @author Dazo66
 */
public class EventPoster {

    public static boolean onPlayerDestroy(BlockPos pos) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null && mc.player.world != null) {
            World playerWorld = mc.player.world;
            IBlockState state = playerWorld.getBlockState(pos);
            return MinecraftForge.EVENT_BUS.post(new LivingDestroyBlockEvent(mc.player, pos, state));
        }
        return false;
    }

}
