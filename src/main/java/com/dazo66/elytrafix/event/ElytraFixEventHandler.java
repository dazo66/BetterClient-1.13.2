package com.dazo66.elytrafix.event;

import com.dazo66.betterclient.util.reflection.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemFirework;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Dazo66
 */
public class ElytraFixEventHandler {

    private Minecraft mc = Minecraft.getMinecraft();
    private Method method = ReflectionHelper.getInstance().getMethod(Entity.class, "setFlag");
    private long lastTime = System.currentTimeMillis();

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void rightClickItemEvent(PlayerInteractEvent.RightClickItem event){
        ItemStack chest = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (mc.player.isSpectator() || mc.player.capabilities.isFlying) {
            return;
        }
        if (chest.getItem() == Items.ELYTRA && ItemElytra.isUsable(chest)) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                if (event.getItemStack().getItem() instanceof ItemFirework) {
                    ItemStack itemStack = event.getItemStack();
                    NBTTagCompound nbttagcompound = itemStack.getTagCompound();
                    if (nbttagcompound != null) {
                        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Fireworks");
                        int flightLevel = nbttagcompound1.getInteger("Flight");
                        if (flightLevel > 0) {
                            if (!mc.player.isElytraFlying()) {
                                mc.player.move(MoverType.SELF, 0d, 0.7d, 0d);
                                mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                                try {
                                    method.invoke(mc.player, 7, true);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                                lastTime = System.currentTimeMillis();
                            }
                        }
                    }
                }
            }
        }
    }


}
