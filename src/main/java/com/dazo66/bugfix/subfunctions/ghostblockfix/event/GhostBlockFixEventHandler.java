package com.dazo66.bugfix.subfunctions.ghostblockfix.event;

import com.dazo66.betterclient.event.PacketProcessEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Dazo66
 */
public class GhostBlockFixEventHandler {

    private Minecraft mc = Minecraft.getMinecraft();
    private BlockRecorder recorder = new BlockRecorder();

    @SubscribeEvent
    public void packetProcess(PacketProcessEvent<INetHandlerPlayClient> event) {
        Packet<INetHandlerPlayClient> packet = event.getPacket();
        if (packet.getClass() == SPacketBlockChange.class) {
            recorder.onBlockChangePacket((SPacketBlockChange) event.getPacket());
        } else if (packet instanceof SPacketEntityHeadLook) {

        }
    }

    @SubscribeEvent
    public void packetServerProcess(PacketProcessEvent<INetHandlerPlayServer> event) {
        //        String className = event.getPacket().getClass().getSimpleName();
        //        if (className.startsWith("SP") && !className.contains("Time")) {
        //            System.out.println(className);
        //        }
    }

    @SubscribeEvent
    public void playerDestroyBlock(LivingDestroyBlockEvent event) {
        if (event.getEntityLiving() == mc.player) {
            recorder.onPlayDestroyBlock(mc.player, event.getPos(), event.getState());
        }
    }

    @SubscribeEvent
    public void onChangeWorld(WorldEvent.Load event) {
        recorder.getMap().clear();
    }

    @SubscribeEvent
    public void clientTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Map<BlockPos, Map.Entry<Long, IBlockState>> map = recorder.getMap();
            if (mc.world != null) {
                Long time = mc.world.getWorldInfo().getWorldTotalTime();
                if (time % 10 == 0) {
                    Iterator<BlockPos> iterator = map.keySet().iterator();
                    while (iterator.hasNext()) {
                        BlockPos pos = iterator.next();
                        if (!mc.world.isBlockLoaded(pos)) {
                            iterator.remove();
                            continue;
                        }
                        Map.Entry<Long, IBlockState> entry = map.get(pos);
                        if (time - entry.getKey() > 10) {
                            if (mc.player.getHeldItemOffhand().isEmpty()) {
                                sendCPacketPlayerTryUseItemOnBlock(pos, EnumFacing.DOWN, EnumHand.OFF_HAND);
                                continue;
                            }
                            ItemStack mainhand = mc.player.getHeldItemMainhand();
                            if (mainhand.isEmpty()) {
                                sendCPacketPlayerTryUseItemOnBlock(pos, EnumFacing.DOWN, EnumHand.MAIN_HAND);
                                continue;
                            } else {
                                double dist = mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + 3;
                                dist *= dist;
                                boolean isReach = mc.player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) < dist;
                                if (!isReach) {
                                    sendCPacketPlayerTryUseItemOnBlock(pos, EnumFacing.DOWN, EnumHand.MAIN_HAND);
                                    continue;
                                }
                            }
                            if (mainhand.getItem() instanceof ItemTool) {
                                sendCPacketPlayerTryUseItemOnBlock(pos, EnumFacing.DOWN, EnumHand.OFF_HAND);
                            }
                        }
                    }
                }
            } else {
                if (!map.isEmpty()) {
                    map.clear();
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void sendCPacketPlayerTryUseItemOnBlock(BlockPos pos, EnumFacing facing, EnumHand hand) {
        if (mc.getConnection() != null && mc.world != null) {
            Vec3d vec = mc.player.getLookVec();
            float f = (float) (vec.x - (double) pos.getX());
            float f1 = (float) (vec.y - (double) pos.getY());
            float f2 = (float) (vec.z - (double) pos.getZ());
            mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.DOWN, EnumHand.OFF_HAND, f, f1, f2));
        }
    }


}
