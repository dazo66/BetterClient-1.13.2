package com.dazo66.betterclient.event;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Dazo66
 */
public class EntityEvent extends Event {

    private World world;
    private Entity entity;

    public EntityEvent(World worldIn, Entity entityIn) {
        world = worldIn;
        entity = entityIn;
    }

    public Entity getEntity() {
        return entity;
    }

    public World getWorld() {
        return world;
    }

    public static class EntityAddEvent extends EntityEvent {
        public EntityAddEvent(World worldIn, Entity entityIn) {
            super(worldIn, entityIn);
        }

        public static void post(World worldIn, Entity entityIn) {
            MinecraftForge.EVENT_BUS.post(new EntityAddEvent(worldIn, entityIn));
        }
    }


}
