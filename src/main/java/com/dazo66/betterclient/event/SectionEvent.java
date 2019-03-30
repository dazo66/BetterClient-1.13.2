package com.dazo66.betterclient.event;

import net.minecraft.profiler.Profiler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * This event is fired on both side.
 * <p>
 * This event will post at :
 * {@link Profiler#endStartSection(String)}
 * {@link Profiler#endSection()}
 * {@link Profiler#func_194340_a(Supplier)}
 * <p>
 * All availadble Section can post to {@link Start} and {@link End}.
 * <p>
 * Available Section :
 * <p>
 * Start :
 * <p>
 * Client thread:
 * <p>
 * root,
 * root.display_update,
 * root.fpslimit_wait,
 * root.gameRenderer,
 * root.gameRenderer.gui,
 * root.gameRenderer.gui.bossHealth,
 * root.gameRenderer.gui.chat,
 * root.gameRenderer.gui.forgeHudText,
 * root.gameRenderer.gui.toolHighlight,
 * root.gameRenderer.level,
 * root.gameRenderer.level.aboveClouds,
 * root.gameRenderer.level.camera,
 * root.gameRenderer.level.center,
 * root.gameRenderer.level.clear,
 * root.gameRenderer.level.clouds,
 * root.gameRenderer.level.culling,
 * root.gameRenderer.level.destroyProgress,
 * root.gameRenderer.level.entities,
 * root.gameRenderer.level.entities.blockentities,
 * root.gameRenderer.level.entities.entities,
 * root.gameRenderer.level.entities.global,
 * root.gameRenderer.level.entities.prepare,
 * root.gameRenderer.level.forge_render_last,
 * root.gameRenderer.level.frustum,
 * root.gameRenderer.level.hand,
 * root.gameRenderer.level.lightTex,
 * root.gameRenderer.level.litParticles,
 * root.gameRenderer.level.outline,
 * root.gameRenderer.level.particles,
 * root.gameRenderer.level.pick,
 * root.gameRenderer.level.prepareterrain,
 * root.gameRenderer.level.sky,
 * root.gameRenderer.level.terrain,
 * root.gameRenderer.level.terrain.filterempty,
 * root.gameRenderer.level.terrain.render_Cutout,
 * root.gameRenderer.level.terrain.render_Mipped Cutout,
 * root.gameRenderer.level.terrain.render_Solid,
 * root.gameRenderer.level.terrain_setup,
 * root.gameRenderer.level.terrain_setup.camera,
 * root.gameRenderer.level.terrain_setup.captureFrustum,
 * root.gameRenderer.level.terrain_setup.cull,
 * root.gameRenderer.level.terrain_setup.culling,
 * root.gameRenderer.level.terrain_setup.rebuildNear,
 * root.gameRenderer.level.terrain_setup.rebuildNear.build near,
 * root.gameRenderer.level.terrain_setup.renderlistcamera,
 * root.gameRenderer.level.terrain_setup.update,
 * root.gameRenderer.level.terrain_setup.update.iteration,
 * root.gameRenderer.level.translucent,
 * root.gameRenderer.level.translucent.filterempty,
 * root.gameRenderer.level.translucent.render_Translucent,
 * root.gameRenderer.level.translucent.translucent_sort,
 * root.gameRenderer.level.updatechunks,
 * root.gameRenderer.level.weather,
 * root.gameRenderer.mouse,
 * root.preRenderErrors,
 * root.render,
 * root.render.display,
 * root.scheduledExecutables,
 * root.scheduledExecutables.display_update,
 * root.sound,
 * root.tick,
 * root.tick.animateTick,
 * root.tick.gameMode,
 * root.tick.gameRenderer,
 * root.tick.gui,
 * root.tick.keyboard,
 * root.tick.level,
 * root.tick.level.blocks,
 * root.tick.level.blocks.buildList,
 * root.tick.level.blocks.getChunk,
 * root.tick.level.blocks.getChunk.checkedPosition < toCheckCount,
 * root.tick.level.blocks.getChunk.getBrightness,
 * root.tick.level.chunkCache,
 * root.tick.level.entities,
 * root.tick.level.entities.blockEntities,
 * root.tick.level.entities.blockEntities.${tileentity_id},
 * root.tick.level.entities.global,
 * root.tick.level.entities.pendingBlockEntities,
 * root.tick.level.entities.regular,
 * root.tick.level.entities.regular.remove,
 * root.tick.level.entities.regular.tick,
 * root.tick.level.entities.regular.tick.ai,
 * root.tick.level.entities.regular.tick.ai.newAi,
 * root.tick.level.entities.regular.tick.chunkCheck,
 * root.tick.level.entities.regular.tick.entityBaseTick,
 * root.tick.level.entities.regular.tick.headTurn,
 * root.tick.level.entities.regular.tick.jump,
 * root.tick.level.entities.regular.tick.livingEntityBaseTick,
 * root.tick.level.entities.regular.tick.looting,
 * root.tick.level.entities.regular.tick.mobBaseTick,
 * root.tick.level.entities.regular.tick.push,
 * root.tick.level.entities.regular.tick.rangeChecks,
 * root.tick.level.entities.regular.tick.travel,
 * root.tick.level.entities.regular.tick.travel.move,
 * root.tick.level.entities.regular.tick.travel.rest,
 * root.tick.level.entities.remove,
 * root.tick.level.reEntryProcessing,
 * root.tick.levelRenderer,
 * root.tick.mouse,
 * root.tick.particles,
 * root.tick.particles.0,
 * root.tick.particles.0.0,
 * root.tick.particles.0.1,
 * root.tick.particles.1,
 * root.tick.particles.1.0,
 * root.tick.particles.1.1,
 * root.tick.particles.2,
 * root.tick.particles.2.0,
 * root.tick.particles.2.1,
 * root.tick.particles.3,
 * root.tick.particles.3.0,
 * root.tick.particles.3.1,
 * root.tick.pendingConnection,
 * root.tick.pick,
 * root.tick.textures,
 * root.tick.textures.display_update,
 * root.toasts,
 * etc...
 * <p>
 * Server thread:
 * <p>
 * root,
 * root.commandFunctions,
 * root.connection,
 * root.connection.ai,
 * root.connection.ai.newAi,
 * root.connection.entityBaseTick,
 * root.connection.entityBaseTick.portal,
 * root.connection.entityBaseTick.portal.moving,
 * root.connection.entityBaseTick.portal.placing,
 * root.connection.entityBaseTick.portal.placing.chunkCheck,
 * root.connection.headTurn,
 * root.connection.jump,
 * root.connection.keepAlive,
 * root.connection.livingEntityBaseTick,
 * root.connection.push,
 * root.connection.rangeChecks,
 * root.connection.travel,
 * root.connection.travel.move,
 * root.connection.travel.rest,
 * root.dim_unloading,
 * root.jobs,
 * root.jobs.move,
 * root.jobs.rest,
 * root.levels,
 * root.levels.${world_name},
 * root.levels.${world_name}.tick,
 * root.levels.${world_name}.tick.chunkMap,
 * root.levels.${world_name}.tick.chunkSource,
 * root.levels.${world_name}.tick.chunkSource.chunkCheck,
 * root.levels.${world_name}.tick.entities,
 * root.levels.${world_name}.tick.entities.blockEntities,
 * root.levels.${world_name}.tick.entities.blockEntities.${tileentity_id},
 * root.levels.${world_name}.tick.entities.global,
 * root.levels.${world_name}.tick.entities.pendingBlockEntities,
 * root.levels.${world_name}.tick.entities.players,
 * root.levels.${world_name}.tick.entities.players.remove,
 * root.levels.${world_name}.tick.entities.players.tick,
 * root.levels.${world_name}.tick.entities.players.tick.chunkCheck,
 * root.levels.${world_name}.tick.entities.regular,
 * root.levels.${world_name}.tick.entities.regular.remove,
 * root.levels.${world_name}.tick.entities.regular.tick,
 * root.levels.${world_name}.tick.entities.regular.tick.ai,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi.checkDespawn,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi.controls,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi.controls.jump,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi.controls.look,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi.controls.move,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi.goalSelector,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi.goalSelector.goalSetup,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi.goalSelector.goalSetup.pathfind,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi.goalSelector.goalTick,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi.mob tick,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi.navigation,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi.sensing,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi.targetSelector,
 * root.levels.${world_name}.tick.entities.regular.tick.ai.newAi.targetSelector.goalSetup,
 * root.levels.${world_name}.tick.entities.regular.tick.chunkCheck,
 * root.levels.${world_name}.tick.entities.regular.tick.entityBaseTick,
 * root.levels.${world_name}.tick.entities.regular.tick.entityBaseTick.portal,
 * root.levels.${world_name}.tick.entities.regular.tick.headTurn,
 * root.levels.${world_name}.tick.entities.regular.tick.jump,
 * root.levels.${world_name}.tick.entities.regular.tick.livingEntityBaseTick,
 * root.levels.${world_name}.tick.entities.regular.tick.looting,
 * root.levels.${world_name}.tick.entities.regular.tick.mobBaseTick,
 * root.levels.${world_name}.tick.entities.regular.tick.push,
 * root.levels.${world_name}.tick.entities.regular.tick.rangeChecks,
 * root.levels.${world_name}.tick.entities.regular.tick.travel,
 * root.levels.${world_name}.tick.entities.regular.tick.travel.move,
 * root.levels.${world_name}.tick.entities.regular.tick.travel.rest,
 * root.levels.${world_name}.tick.entities.remove,
 * root.levels.${world_name}.tick.mobSpawner,
 * root.levels.${world_name}.tick.portalForcer,
 * root.levels.${world_name}.tick.tickBlocks,
 * root.levels.${world_name}.tick.tickBlocks.playerCheckLight,
 * root.levels.${world_name}.tick.tickBlocks.playerCheckLight.checkedPosition < toCheckCount,
 * root.levels.${world_name}.tick.tickBlocks.playerCheckLight.getBrightness,
 * root.levels.${world_name}.tick.tickBlocks.pollingChunks,
 * root.levels.${world_name}.tick.tickBlocks.pollingChunks.checkNextLight,
 * root.levels.${world_name}.tick.tickBlocks.pollingChunks.forcedChunkLoading,
 * root.levels.${world_name}.tick.tickBlocks.pollingChunks.getChunk,
 * root.levels.${world_name}.tick.tickBlocks.pollingChunks.iceandsnow,
 * root.levels.${world_name}.tick.tickBlocks.pollingChunks.regularChunkLoading,
 * root.levels.${world_name}.tick.tickBlocks.pollingChunks.thunder,
 * root.levels.${world_name}.tick.tickBlocks.pollingChunks.tickBlocks,
 * root.levels.${world_name}.tick.tickBlocks.pollingChunks.tickBlocks.randomTick,
 * root.levels.${world_name}.tick.tickBlocks.pollingChunks.tickChunk,
 * root.levels.${world_name}.tick.tickPending,
 * root.levels.${world_name}.tick.tickPending.cleaning,
 * root.levels.${world_name}.tick.tickPending.ticking,
 * root.levels.${world_name}.tick.village,
 * root.levels.${world_name}.timeSync,
 * root.levels.${world_name}.tracker,
 * root.players,
 * root.save,
 * root.snooper,
 * root.tallying,
 * root.tickables,
 * etc...
 * <p>
 * <p>
 * ${world_name} = this map name;
 * ${tileentity_id} = tileentity id  Example: "minecraft:shulker_box", "minecraft:furnace";
 *
 * @author Dazo66
 * @see net.minecraft.profiler.Profiler
 * @see net.minecraft.tileentity.TileEntity
 */
public class SectionEvent extends Event {

    private static Map<String, String> lastSection = new ConcurrentHashMap<>();

    private String threadName;

    public SectionEvent() {
        super();
        threadName = Thread.currentThread().getName();
    }

    public static void postStart(String sectionName) {
        String s = lastSection.get(Thread.currentThread().getName());
        if (s != null) {
            s = s + "." + sectionName;
        } else {
            s = sectionName;
        }
        lastSection.put(Thread.currentThread().getName(), s);
        MinecraftForge.EVENT_BUS.post(new Start(s));
    }

    public static void postEnd() {
        String section = lastSection.get(Thread.currentThread().getName());
        if (section != null) {
            MinecraftForge.EVENT_BUS.post(new End(section));
            int i = section.lastIndexOf(".");
            if (i != -1) {
                String s = section.substring(0, i);
                lastSection.put(Thread.currentThread().getName(), s);
            } else {
                lastSection.remove(Thread.currentThread().getName());
            }
        }
    }

    public static void supplierStart(Supplier<String> supplier) {
        postStart(supplier.get());
    }

    public String getThreadName() {
        return threadName;
    }

    public static class Start extends SectionEvent {

        private String section;

        public Start(String sectionName) {
            super();
            section = sectionName;
        }

        public String getSection() {
            return section;
        }

    }

    public static class End extends SectionEvent {

        private String section;

        public End(String sectionNameIn) {
            super();
            section = sectionNameIn;
        }

        public String getSection() {
            return section;
        }

    }

}
