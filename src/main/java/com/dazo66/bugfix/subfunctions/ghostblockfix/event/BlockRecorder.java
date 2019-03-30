package com.dazo66.bugfix.subfunctions.ghostblockfix.event;

import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dazo66
 */
class BlockRecorder {

    private Map<BlockPos, Map.Entry<Long, IBlockState>> map = new HashMap<>();

    void onPlayDestroyBlock(EntityPlayerSP playerSP, BlockPos pos, IBlockState state){
        map.put(pos, Maps.immutableEntry(playerSP.world.getWorldInfo().getWorldTotalTime(), state));
    }

    void onBlockChangePacket(SPacketBlockChange packet){
        map.remove(packet.getBlockPosition());
    }

    Map<BlockPos, Map.Entry<Long, IBlockState>> getMap() {
        return map;
    }
}
