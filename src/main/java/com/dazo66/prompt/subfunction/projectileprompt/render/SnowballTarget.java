package com.dazo66.prompt.subfunction.projectileprompt.render;

import com.dazo66.betterclient.util.reflection.ReflectionHelper;
import com.dazo66.prompt.subfunction.projectileprompt.IProjectileTarget;
import com.dazo66.prompt.subfunction.projectileprompt.util.ParticleRenderRange;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * @author Dazo66
 */
public class SnowballTarget implements IProjectileTarget {

    private Minecraft mc = Minecraft.getMinecraft();
    private ReflectionHelper helper = ReflectionHelper.getInstance();
    private Field inGround;
    private Method findPathMethod;
    private Field randerField;
    private Entity entity;
    private boolean end = false;

    public SnowballTarget(){
        randerField = helper.getField(Entity.class, "rand");
        findPathMethod = helper.getMethod(EntityArrow.class, "findEntityOnPath");
        inGround = helper.getField(EntityArrow.class, "inGround");
    }

    @Override
    public Entity getTarget() {
        end = false;
        EntityThrowable throwable = new EntityThrowable(mc.world, mc.player) {
            @Override
            protected void onImpact(RayTraceResult result) {
                switch (result.typeOfHit) {
                    case ENTITY:
                        entity = result.entityHit;
                        end = true;
                        break;
                    case BLOCK:
                        entity = this;
                        end = true;
                    default:
                }
            }
        };
        throwable.shoot(mc.player, mc.player.rotationPitch, mc.player.rotationYaw, 0.0F, 1.5F, 1.0F);
        ParticleRenderRange.setRange(0.0d);
        do {
            throwable.onUpdate();
            throwable.ticksExisted++;
        }
        while (!end && throwable.ticksExisted < 400);
        ParticleRenderRange.resetRange();
        return entity;
    }


}
