package com.dazo66.fasttrading.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

import java.util.Iterator;


/**
 * @author Dazo66
 */
public class ItemStackUtils {

    public static String[] tooltipI18n(String s, Object... args) {
        return I18n.format(s, args).replace("\\n", "##&&").split("##&&");
    }

    public static boolean areItemEqualIgnoreCount(ItemStack stackA, ItemStack stackB) {
        if (stackA.getItem() != stackB.getItem()) {
            return false;
        } else if (stackA.getItemDamage() != stackB.getItemDamage()) {
            return false;
        } else if (stackA.getTagCompound() == null && stackB.getTagCompound() != null) {
            return false;
        } else {
            return (stackA.getTagCompound() == null || stackA.getTagCompound().equals(stackB.getTagCompound())) && stackA.areCapsCompatible(stackB);
        }
    }

    public static boolean areNbtEqual(NBTBase nbtA, NBTBase nbtB) {
        if (nbtA == null && nbtB == null) {
            return true;
        } else if (nbtA != null && nbtB != null) {
            if (nbtA.getClass() != nbtB.getClass()) {
                return false;
            }
            if (nbtA instanceof NBTTagCompound) {
                NBTTagCompound nbtCompoundA = (NBTTagCompound) nbtA;
                NBTTagCompound nbtCompoundB = (NBTTagCompound) nbtB;
                if (nbtCompoundA.getKeySet().size() != nbtCompoundB.getKeySet().size()) {
                    return false;
                }
                for (String key : nbtCompoundA.getKeySet()) {
                    NBTBase nbtBaseA = nbtCompoundA.getTag(key);
                    NBTBase nbtBaseB = nbtCompoundB.getTag(key);
                    if (!areNbtEqual(nbtBaseA, nbtBaseB)) {
                        return false;
                    }
                }
            } else if (nbtA instanceof NBTTagList) {
                Iterator nbtListA = ((NBTTagList) nbtA).iterator();
                Iterator nbtListB = ((NBTTagList) nbtB).iterator();
                while (nbtListA.hasNext()) {
                    if (!nbtListB.hasNext()) {
                        return false;
                    }
                    if (!areNbtEqual((NBTBase) nbtListA.next(), (NBTBase) nbtListB.next())) {
                        return false;
                    }
                }
                return !nbtListB.hasNext();
            } else if (nbtA instanceof NBTPrimitive) {
                NBTPrimitive byteA = (NBTPrimitive) nbtA;
                NBTPrimitive byteB = (NBTPrimitive) nbtB;
                return byteA.getByte() == byteB.getByte();
            } else if (nbtA instanceof NBTTagString) {
                NBTTagString stringA = (NBTTagString) nbtA;
                NBTTagString stringB = (NBTTagString) nbtB;
                return stringA.getString().equals(stringB.getString());
            } else if (nbtA instanceof NBTTagByteArray) {
                NBTTagByteArray byteArrayA = (NBTTagByteArray) nbtA;
                NBTTagByteArray byteArrayB = (NBTTagByteArray) nbtB;
                return byteArrayA.equals(byteArrayB);
            } else if (nbtA instanceof NBTTagIntArray) {
                NBTTagIntArray intArrayA = (NBTTagIntArray) nbtA;
                NBTTagIntArray intArrayB = (NBTTagIntArray) nbtB;
                return intArrayA.equals(intArrayB);
            } else if (nbtA instanceof NBTTagLongArray) {
                NBTTagLongArray longArrayA = (NBTTagLongArray) nbtA;
                NBTTagLongArray longArrayB = (NBTTagLongArray) nbtB;
                return longArrayA.equals(longArrayB);
            }
            return true;
        }
        return false;
    }

}
