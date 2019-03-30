package com.dazo66.prompt.subfunction.projectileprompt.eventhandler;

import com.dazo66.betterclient.event.SectionEvent;
import com.dazo66.betterclient.util.reflection.ReflectionHelper;
import com.dazo66.prompt.subfunction.projectileprompt.IProjectileTarget;
import com.dazo66.prompt.subfunction.projectileprompt.render.BowTarget;
import com.dazo66.prompt.subfunction.projectileprompt.render.FishingRodTarget;
import com.dazo66.prompt.subfunction.projectileprompt.render.SnowballTarget;
import com.dazo66.prompt.subfunction.projectileprompt.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author Dazo66
 */
public class ProjectilePromptEventHandler {

    private Minecraft mc = Minecraft.getMinecraft();
    private HashMap<Class<? extends Item>, IProjectileTarget> canUseItem = new HashMap<>();
    private Entity lastEntity;
    private static ReflectionHelper helper = ReflectionHelper.getInstance();

//    private Map<String, Map<String, Integer>> startList = new TreeMap<>();
//    private Map<String, Map<String, Integer>> endList = new TreeMap<>();

    public ProjectilePromptEventHandler(){
        canUseItem.put(ItemBow.class, new BowTarget());
        canUseItem.put(ItemSnowball.class, new SnowballTarget());
        canUseItem.put(ItemFishingRod.class, new FishingRodTarget());
    }

    @SubscribeEvent
    public void entityRenterTick(SectionEvent.End event){
        if ("root.gameRenderer.level.entities.entities".equals(event.getSection())) {
            if (lastEntity != null) {
                reset();
                lastEntity = null;
            }
            ItemStack itemStack = getFirstShootableItem();
            if (!itemStack.isEmpty()) {
                Item item = itemStack.getItem();
                if (canUseItem.keySet().contains(item.getClass())) {
                    try {
                        lastEntity = canUseItem.get(item.getClass()).getTarget();
                        if (lastEntity != null) {
                            RenderUtils.renderOutLine(lastEntity);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void reset() {
        if (lastEntity != null) {
            EnumFacing facing = mc.player.getHorizontalFacing().getOpposite();
            Vec3i vec3i = facing.getDirectionVec();
            Entity resetEntity = new EntityItem(mc.world, mc.player.posX + vec3i.getX() * 10, mc.player.posY + vec3i.getY() * 10,mc.player.posZ + vec3i.getZ() * 10, new ItemStack(Items.APPLE));
            RenderUtils.renderOutLine(resetEntity);
            lastEntity = null;
        }
    }

    private ItemStack getFirstShootableItem() {
        EntityPlayerSP player = mc.player;
        ItemStack itemStack;
        if (player != null) {
            for (EnumHand hand : EnumHand.values()) {
                itemStack = player.getHeldItem(hand);
                if (!itemStack.isEmpty()) {
                    if (canUseItem.keySet().contains(itemStack.getItem().getClass())) {
                        return itemStack;
                    }
                }
            }
        }
    return ItemStack.EMPTY;
    }
//        @SideOnly(Side.CLIENT)
//    @SubscribeEvent
//    public void onKeyInput(InputEvent.KeyInputEvent event) {
//        if (KeyLoader.key_F4.isPressed()) {
//            synchronized (startList) {
//                System.out.println("Start : ");
//                for (String s : startList.keySet()) {
//                    StringBuilder builder = new StringBuilder();
//                    builder.append(s);
//                    builder.append(": ");
//                    for (String s1 : startList.get(s).keySet()) {
//                        builder.append(s1);
//                        builder.append("->");
//                        builder.append(startList.get(s).get(s1) + ", ");
//                    }
//                    System.out.println(builder.toString());
//                }
//            }
//            synchronized (endList) {
//                System.out.println("End : ");
//                for (String s : endList.keySet()) {
//                    StringBuilder builder = new StringBuilder();
//                    builder.append(s);
//                    builder.append(": ");
//                    for (String s1 : endList.get(s).keySet()) {
//                        builder.append(s1);
//                        builder.append("->");
//                        builder.append(endList.get(s).get(s1) + ", ");
//                    }
//                    System.out.println(builder.toString());
//                }
//            }
//        }
//    }
//
//
//        @SubscribeEvent
//        public void onSectionStartEvent(SectionEvent.Start event) {
//            synchronized (startList) {
//                startList.computeIfAbsent(event.getThreadName(), k -> new TreeMap<>());
//                Map<String, Integer> map = startList.get(event.getThreadName());
//                Integer integer = map.get(event.getSection());
//                if (integer == null) {
//                    integer = 1;
//                }else {
//                    integer += 1;
//                }
//                map.put(event.getSection(), integer);
//            }
//        }
//
//        @SubscribeEvent
//        public void onSectionEndEvent(SectionEvent.End event){
//            synchronized (endList) {
//                endList.computeIfAbsent(event.getThreadName(), k -> new TreeMap<>());
//                Map<String, Integer> map = endList.get(event.getThreadName());
//                Integer integer = map.get(event.getSection());
//                if (integer == null) {
//                    integer = 1;
//                }else {
//                    integer += 1;
//                }
//                map.put(event.getSection(), integer);
//            }
//        }
}
