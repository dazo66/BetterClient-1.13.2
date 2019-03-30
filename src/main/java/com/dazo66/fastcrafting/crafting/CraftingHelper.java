package com.dazo66.fastcrafting.crafting;

import com.dazo66.betterclient.util.reflection.ReflectionHelper;
import com.dazo66.fastcrafting.gui.GuiInventoryModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IShapedRecipe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dazo66
 */
public class CraftingHelper {

    private Minecraft mc = Minecraft.getMinecraft();
    private EntityPlayerSP player = mc.player;
    private Gui gui;
    private Container inventorySlots;
    private IRecipe iRecipe;
    private NonNullList<Ingredient> list;
    private GuiInventoryEnum type;
    private Method clickMethod;
    private GuiInventoryModifier modifier;
    private int[] invSlot;
    private int[] tabSlot;

    public CraftingHelper(Gui guiIn, GuiInventoryModifier modifierIn) {
        gui = guiIn;
        modifier = modifierIn;
        inventorySlots = modifier.getInventorySlots();
        type = modifier.getGuiType();
        invSlot = invSlotIndex();
        tabSlot = tabSlotIndex();
        clickMethod = ReflectionHelper.getInstance().getMethod(gui.getClass(), "mouseClicked");
    }

    private void craft(int count) {
        int i = 0;
        out:
        for (Ingredient ing : list) {
            i++;
            for (ItemStack itemStack : ing.getMatchingStacks()) {
                Slot fromSlot;
                if ((fromSlot = findItem(itemStack)) != null) {
                    Slot toSlot = inventorySlots.getSlot(getToSlotID(i));
                    ItemStack itemStack1 = fromSlot.getStack();
                    if (itemStack1.getCount() < count) {
                        countNotEnough(fromSlot, itemStack, count);
                    }
                    moveItem(fromSlot, toSlot, count);
                    continue out;
                }
            }
        }
        takeItemOut();
    }

    public void craftMode(boolean isShiftKeyDown, boolean isCtrlKeyDown) {
        clearCraftSlot();
        if (isCtrlKeyDown && isShiftKeyDown) {
            craftAll();
        } else if (isShiftKeyDown) {
            craftStack();
        } else {
            craftOne();
        }
        craftEnd();
    }

    private void craftEnd() {
        sendFakePackge(1, 0, ClickType.QUICK_MOVE, new ItemStack(Items.DIAMOND_SWORD));
    }

    private void clearCraftSlot() {
        for (int i : tabSlot) {
            Slot slot = inventorySlots.getSlot(i);
            if (slot.slotNumber == 0) {
                continue;
            }
            if (slot.getHasStack()) {
                slotClick(slot, 0, ClickType.QUICK_MOVE);
            }
        }
        if (!mc.player.inventory.getItemStack().isEmpty()) {
            Slot slot = getFirstEmptySlot();
            if (null == slot) {
                if (type == GuiInventoryEnum.CRAFTING_TABLE) {
                    click(1, 1);
                } else if (type == GuiInventoryEnum.INVENTORY) {
                    click(1, 1);
                }
            } else {
                slotClick(getFirstEmptySlot(), 0, ClickType.PICKUP);
            }
        }
    }

    private void click(int x, int y) {
        try {
            clickMethod.invoke(gui, x, y, 0);
            System.out.println(clickMethod);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private int getToSlotID(int index) {
        if (iRecipe instanceof IShapedRecipe) {
            int height = ((IShapedRecipe) iRecipe).getRecipeHeight();
            int width = ((IShapedRecipe) iRecipe).getRecipeWidth();
            if (type == GuiInventoryEnum.CRAFTING_TABLE) {
                if (width == 2 && (height == 3 || height == 2)) {
                    switch (index) {
                        case 3:
                            return 4;
                        case 4:
                            return 5;
                        case 5:
                            return 7;
                        case 6:
                            return 8;
                        default:
                    }
                } else if (width == 1 && (height == 3 || height == 2)) {
                    switch (index) {
                        case 2:
                            return 4;
                        case 3:
                            return 7;
                        default:
                    }
                }
            } else {
                if (width == 1 && height == 2) {
                    switch (index) {
                        case 2:
                            return 3;
                        default:
                    }
                }
            }

        }
        return index;
    }

    private Slot getFirstEmptySlot() {
        for (int i : invSlot) {
            if (!inventorySlots.getSlot(i).getHasStack()) {
                return inventorySlots.getSlot(i);
            }
        }
        return null;
    }

    private void craftAll() {
        int totalCraftCount = getMaxCraftingCount();
        int maxStackSize = iRecipe.getRecipeOutput().getMaxStackSize();
        int totalCraftTime = totalCraftCount / maxStackSize + (totalCraftCount % maxStackSize == 0 ? 0 : 1);
        int craftOnceCount = getMaxCraftCountOnce();
        for (; totalCraftTime > 0; totalCraftTime--) {
            int count;
            if (totalCraftCount > craftOnceCount) {
                count = craftOnceCount;
            } else {
                count = totalCraftCount;
            }
            totalCraftCount -= count;
            craft(count);
        }
    }

    private void craftStack() {
        int totalCanCraftCount = getMaxCraftingCount();
        int maxStackSize = iRecipe.getRecipeOutput().getMaxStackSize();
        int totalCraftCount = totalCanCraftCount > maxStackSize ? maxStackSize : totalCanCraftCount;
        int craftOnceCount = getMaxCraftCountOnce();
        int totalCraftTime = totalCraftCount / craftOnceCount + totalCraftCount % craftOnceCount == 0 ? 0 : 1;
        for (; totalCraftTime > 0; totalCraftTime--) {
            int count;
            if (totalCraftCount > craftOnceCount) {
                count = craftOnceCount;
            } else {
                count = totalCraftCount;
            }
            totalCraftCount -= count;
            craft(count);
        }
    }

    private void craftOne() {
        craft(1);
    }

    private void countNotEnough(Slot slot, ItemStack targetItem, int targetCount) {
        ArrayList<Slot> slots = findAllItem(targetItem);
        for (Slot slot1 : slots) {
            if (slot.slotNumber == slot1.slotNumber) {
                continue;
            }

            if (targetItem.getCount() < targetCount) {
                leftClickSlot(slot1, ClickType.PICKUP);
                leftClickSlot(slot, ClickType.PICKUP);
                leftClickSlot(slot1, ClickType.PICKUP);
            } else {
                return;
            }

        }
    }

    private void takeItemOut() {
        Slot resultSlot = inventorySlots.getSlot(0);
        sendFakePackge(resultSlot.slotNumber, 0, ClickType.QUICK_MOVE, iRecipe.getRecipeOutput());
        for (int i : tabSlot) {
            Slot slot = inventorySlots.getSlot(i);
            if (slot.slotNumber == 0) {
                continue;
            }
            slot.putStack(ItemStack.EMPTY);
        }
    }

    private Slot findItem(ItemStack itemStack) {
        for (int i : invSlot) {
            Slot slot = inventorySlots.getSlot(i);
            if (slot.getHasStack() && isItemEqual(slot.getStack(), itemStack)) {
                return slot;
            }
        }
        return null;
    }

    private ArrayList<Slot> findAllItem(ItemStack itemStack) {
        ArrayList<Slot> slots = new ArrayList<>();
        for (int i : invSlot) {
            Slot slot = inventorySlots.getSlot(i);
            if (slot.getHasStack() && isItemEqual(slot.getStack(), itemStack)) {
                slots.add(slot);
            }
        }
        return slots;
    }

    private void moveItem(Slot from, Slot to, int targetCount) {
        int count = from.getStack().getCount();
        if (targetCount == count) {
            moveAllItem(from, to);
        } else if (targetCount * 3 / 4 > count) {
            slotClick(from, 0, ClickType.PICKUP);
            simpleRightClickTimes(from, count - targetCount);
            slotClick(to, 0, ClickType.PICKUP);
        } else if (targetCount * 2 > count) {
            moveHelfItem(from, to);
            slotClick(from, 0, ClickType.PICKUP);
            simpleRightClickTimes(to, targetCount - (count % 2 == 0 ? count / 2 : count / 2 + 1));
            slotClick(from, 0, ClickType.PICKUP);
        } else if (targetCount * 2 == count) {
            moveHelfItem(from, to);
        } else if (targetCount * 4 > count) {
            slotClick(from, 1, ClickType.PICKUP);
            simpleRightClickTimes(from, (count % 2 == 0 ? count / 2 : count / 2 + 1) - targetCount);
            slotClick(to, 0, ClickType.PICKUP);
        } else if (targetCount * 4 <= count) {
            slotClick(from, 0, ClickType.PICKUP);
            simpleRightClickTimes(to, targetCount);
            slotClick(from, 0, ClickType.PICKUP);
        }
    }

    private void moveAllItem(Slot from, Slot to) {
        leftClickSlot(from, ClickType.PICKUP);
        leftClickSlot(to, ClickType.PICKUP);
    }

    private void moveHelfItem(Slot from, Slot to) {
        rightClickSlot(from, ClickType.PICKUP);
        leftClickSlot(to, ClickType.PICKUP);
    }

    private void leftClickSlot(Slot solt, ClickType type) {
        slotClick(solt, 0, type);
    }

    private void rightClickSlot(Slot solt, ClickType type) {
        slotClick(solt, 1, type);
    }

    private void simpleRightClickTimes(Slot slot, int times) {

        for (int i = 0; i < times; i++) {
            rightClickSlot(slot, ClickType.PICKUP);
        }
    }

    private void sendFakePackge(int slotId, int mouesButton, ClickType type, ItemStack itemstack) {
        this.mc.getConnection().sendPacket(new CPacketClickWindow(this.inventorySlots.windowId, slotId, mouesButton, type, itemstack, this.mc.player.openContainer.getNextTransactionID(player.inventory)));
    }

    private void slotClick(Slot slot, int mouseButton, ClickType type) {
        this.mc.playerController.windowClick(this.inventorySlots.windowId, slot.slotNumber, mouseButton, type, mc.player);
    }

    private int toolItemCount(ItemStack[] canCraftingMaterial) {
        int tool = 0;

        for (int i : invSlot) {
            ItemStack itemStack = this.inventorySlots.getSlot(i).getStack();
            if (isItemContainsList(canCraftingMaterial, itemStack)) {
                tool += itemStack.getCount();
            }

        }

        return tool;

    }

    private boolean isItemStackListEqual(ItemStack[] list1, ItemStack[] list2) {

        if (list1.length == list2.length) {
            for (int i = list1.length - 1; i > -1; i--) {
                if (!list1[i].isItemEqual(list2[i])) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;

    }

    private int sameMaterialCount(int id) {

        int temp = 0;
        for (Ingredient aList : list) {
            if (isItemStackListEqual(list.get(id).getMatchingStacks(), aList.getMatchingStacks())) {
                temp++;
            }
        }
        return temp;
    }

    private int getMin(List<Integer> intList) {
        int max = intList.get(0);
        for (int i = 1; i < intList.size(); i++) {
            if (intList.get(i) < max) {
                max = intList.get(i);
            }
        }
        return max;
    }

    private int getMaxCraftingCount() {

        List<Integer> intList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMatchingStacks().length == 0) {
                continue;
            }
            intList.add(toolItemCount(list.get(i).getMatchingStacks()) / sameMaterialCount(i));
        }

        return getMin(intList);
    }

    private int getMaxCraftCountOnce() {

        List<Integer> intList = new ArrayList<>();
        for (Ingredient aList : list) {
            ItemStack[] itemStacks = aList.getMatchingStacks();
            if (itemStacks.length == 0) {
                continue;
            }
            intList.add(itemStacks[0].getMaxStackSize());
        }
        return getMin(intList);
    }

    private boolean isItemContainsList(ItemStack[] itemList, ItemStack item) {

        for (ItemStack anItemList : itemList) {
            if (isItemEqual(anItemList, item)) {
                return true;
            }
        }
        return false;
    }

    private boolean isItemEqual(ItemStack one, ItemStack two) {
        return !(one.isEmpty() || two.isEmpty()) && one.getItem() == two.getItem() && (one.getItemDamage() == two.getItemDamage() || one.getItemDamage() == 32767 || two.getItemDamage() == 32767);
    }

    private int[] invSlotIndex() {
        int start = type == GuiInventoryEnum.INVENTORY ? 9 : 10;
        int[] intlist = new int[36];
        for (int i = 0; i < 36; i++) {
            intlist[i] = start++;
        }
        return intlist;
    }

    private int[] tabSlotIndex() {
        int end = type == GuiInventoryEnum.CRAFTING_TABLE ? 10 : 5;
        int[] intlist = new int[end];
        for (int i = 0; i < end; i++) {
            intlist[i] = i;
        }
        return intlist;
    }

    /***SetMethod****/

    public void setIRecipe(IRecipe iRecipeIn) {
        iRecipe = iRecipeIn;
    }

    public void setList(NonNullList<Ingredient> listIn) {
        list = listIn;
    }

    public void setType(GuiInventoryEnum type) {
        this.type = type;
    }
}
