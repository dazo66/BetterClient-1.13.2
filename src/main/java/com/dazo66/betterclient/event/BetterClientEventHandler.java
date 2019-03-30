package com.dazo66.betterclient.event;

import com.dazo66.betterclient.BetterClient;
import com.dazo66.betterclient.util.langfileutil.LangFileUpdater;
import com.dazo66.betterclient.util.reflection.ReflectionHelper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.lang.reflect.Field;

/**
 * @author Dazo66
 */
public class BetterClientEventHandler {


    private Minecraft mc = Minecraft.getMinecraft();
    private long lastPostTime = 0;
    private LangFileUpdater updater;
    private long startInWaterTick = -1;
    private EntityFishHook hook;
    private double hookPrevPosY;
    private Field currentHookState = ReflectionHelper.getInstance().getField(EntityFishHook.class, "currentState");
    public BetterClientEventHandler() {
        if ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
            updater = new LangFileUpdater(new File("F:\\ModWorkSpace\\Mod\\BettterClient\\BettterClient-1.12.2\\src\\main\\resources\\assets\\betterclient\\lang").listFiles());
        }
    }

    @SubscribeEvent
    public void onEntityAddEvent(EntityEvent.EntityAddEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityFishHook) {
            EntityPlayer player = ((EntityFishHook) entity).getAngler();
            if (player.getUniqueID() == mc.player.getUniqueID()) {
                startInWaterTick = -1;
                MinecraftForge.EVENT_BUS.post(new FishingEvent.FishHookCreate(player, (EntityFishHook) entity));
            }
        }
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (hook != null && !hook.isDead) {
            try {
                String state = currentHookState.get(hook).toString();
                if ("BOBBING".equals(state) && mc.world.getBlockState(new BlockPos(hook.posX, hook.posY, hook.posZ)).getBlock() instanceof BlockLiquid) {
                    if (startInWaterTick == -1) {
                        startInWaterTick = mc.world.getWorldInfo().getWorldTime();
                    }
                    if (mc.world.getWorldInfo().getWorldTime() - startInWaterTick > 40) {
                        if (hookPrevPosY - hook.posY > 0.08d) {
                            tryPostFishBiteHookEvent();
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            hookPrevPosY = hook.posY;
        }
    }

    @SubscribeEvent
    public void onFishHookCreat(FishingEvent.FishHookCreate event) {
        if (event.getHook().getEntityWorld() instanceof WorldClient) {
            hook = event.getHook();
        }
    }

    @SubscribeEvent
    public void onSoundPlay(PlaySoundEvent event) {
        if ("entity.bobber.splash".equals(event.getName())) {
            if (hook != null && !hook.isDead) {
                ISound sound = event.getSound();
                if (isFloatInRange(sound.getXPosF(), (float) hook.posX, 1.0f)) {
                    if (isFloatInRange(sound.getYPosF(), (float) hook.posY, 1.0f)) {
                        if (isFloatInRange(sound.getZPosF(), (float) hook.posZ, 1.0f)) {
                            //                            System.out.println("sound find");
                            tryPostFishBiteHookEvent();
                        }
                    }
                }
            }
        }
    }

    public boolean isFloatInRange(float f1, float f2, float range) {
        return f2 < f1 + range && f2 > f1 - range;
    }

    @SubscribeEvent
    public void onI18n(I18nEvent event) {
        if ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
            if (!I18n.hasKey(event.getLangKey())) {
                String langKey = event.getLangKey();
                Language lang = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage();
                BetterClient.logger.warn(String.format("TranslateKey not found: key: %s, local: %s", langKey, lang.toString()));
                updater.put(langKey, null);
            }
        }
    }

    private void tryPostFishBiteHookEvent() {
        long time = mc.world.getWorldInfo().getWorldTime();
        if (time - lastPostTime > 80) {
            lastPostTime = time;
            MinecraftForge.EVENT_BUS.post(new FishingEvent.FishCaughtEvent(mc.player, hook));
        }
    }
}
