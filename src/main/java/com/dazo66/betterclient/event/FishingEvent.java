package com.dazo66.betterclient.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Dazo66
 */
public class FishingEvent extends Event {

    private EntityFishHook hook;
    private EntityPlayer angler;

    public FishingEvent(EntityPlayer anglerIn, EntityFishHook hookIn) {
        hook = hookIn;
        angler = anglerIn;
    }

    public EntityFishHook getHook() {
        return hook;
    }

    public EntityPlayer getAngler() {
        return angler;
    }

    public static class FishHookCreate extends FishingEvent {
        public FishHookCreate(EntityPlayer anglerIn, EntityFishHook hookIn) {
            super(anglerIn, hookIn);
        }
    }

    public static class FishCaughtEvent extends FishingEvent {

        public FishCaughtEvent(EntityPlayer anglerIn, EntityFishHook hookIn) {
            super(anglerIn, hookIn);
        }

        public static void post(EntityPlayer anglerIn, EntityFishHook hookIn) {
            MinecraftForge.EVENT_BUS.post(new FishCaughtEvent(anglerIn, hookIn));
        }
    }

}
