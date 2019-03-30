package com.dazo66.prompt.subfunction.projectileprompt.render;

import com.dazo66.betterclient.util.reflection.ReflectionHelper;
import com.dazo66.prompt.subfunction.projectileprompt.IProjectileTarget;
import com.dazo66.prompt.subfunction.projectileprompt.util.ParticleRenderRange;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @author Dazo66
 */
public class BowTarget implements IProjectileTarget {

    private Minecraft mc = Minecraft.getMinecraft();
    private ItemBow itemBow = Items.BOW;
    private ReflectionHelper helper = ReflectionHelper.getInstance();
    private Field randerField;
    private Field inGround;
    private Method findPathMethod;

    public BowTarget() {
        randerField = helper.getField(Entity.class, "rand");
        findPathMethod = helper.getMethod(EntityArrow.class, "findEntityOnPath");
        inGround = helper.getField(EntityArrow.class, "inGround");
    }

    @Override
    public Entity getTarget() {
        EntityPlayerSP playerSP = mc.player;
        int useTime = playerSP.getItemInUseCount();
        if (useTime <= 0) {
            return null;
        }
        int i = itemBow.getMaxItemUseDuration(new ItemStack(itemBow)) - useTime;
        if (i < 0) {
            return null;
        }
        float velocity = ItemBow.getArrowVelocity(i);
        Entity viewEntity = mc.getRenderViewEntity();
        if (velocity >= 0.1f && viewEntity != null) {
            ItemStack itemArrow = new ItemStack(Items.ARROW);
            EntityArrow entityarrow = ((ItemArrow) itemArrow.getItem()).createArrow(mc.world, itemArrow, playerSP);
            try {
                randerField.set(entityarrow, new FakeRandom());
            } catch (IllegalAccessException ignore) {
            }
            entityarrow.shoot(playerSP, playerSP.rotationPitch, playerSP.rotationYaw, 0.0f, velocity * 3.0f, 1.0f);
            ParticleRenderRange.setRange(0.0d);
            Entity entity = getLastState(entityarrow);
            ParticleRenderRange.resetRange();
            if (entity != null && !(entity instanceof EntityPlayerSP) && entity.posY > 0) {
                return entity;
            }
        }
        return null;
    }

    public Entity getLastState(Entity entity) {
        while (true) {
            entity.ticksExisted++;
            Vec3d vec3d1 = new Vec3d(entity.posX, entity.posY, entity.posZ);
            Vec3d vec3d = new Vec3d(entity.posX + entity.motionX, entity.posY + entity.motionY, entity.posZ + entity.motionZ);
            Entity findEntity = null;
            try {
                findEntity = (Entity) findPathMethod.invoke(entity, vec3d1, vec3d);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            if (findEntity != null) {
                return findEntity;
            }
            try {
                if ((Boolean) inGround.get(entity)) {
                    return entity;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (entity.isDead) {
                entity.isDead = false;
                return entity;
            }
            if (entity.ticksExisted > 400) {
                return null;
            }
            entity.onUpdate();
        }
    }

}
