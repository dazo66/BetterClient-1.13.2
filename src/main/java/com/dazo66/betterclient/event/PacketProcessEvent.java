package com.dazo66.betterclient.event;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Dazo66
 */
@Cancelable
public class PacketProcessEvent<T extends INetHandler> extends Event {

    private T processor;
    private Packet<T> packet;

    public PacketProcessEvent(T processor, Packet<T> packet) {
        this.processor = processor;
        this.packet = packet;
    }

    public static <T extends INetHandler> boolean post(Packet<T> packetIn, T processor) {
        PacketProcessEvent<T> event = new PacketProcessEvent<>(processor, packetIn);
        MinecraftForge.EVENT_BUS.post(event);
        return event.isCanceled();
    }

    public Packet<T> getPacket() {
        return packet;
    }

    public T getProcessor() {
        return processor;
    }
}
