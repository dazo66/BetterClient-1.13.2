package com.dazo66.fasttrading.event;

import jline.internal.Nullable;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Dazo66
 */
public class SetMerchantListEvent extends Event {

    public MerchantRecipeList list;

    public SetMerchantListEvent(@Nullable MerchantRecipeList list) {
        this.list = list;
    }

    public static void post(@Nullable MerchantRecipeList list) {
        MinecraftForge.EVENT_BUS.post(new SetMerchantListEvent(list));
    }
}
