package com.saphienyako.lore_master;

import com.mojang.logging.LogUtils;
import com.saphienyako.lore_master.block.ModBlocks;
import com.saphienyako.lore_master.block.entity.LibraryBellBlockEntity;
import com.saphienyako.lore_master.block.entity.ModBlockEntities;
import com.saphienyako.lore_master.block.renderer.LibraryBellRenderer;
import com.saphienyako.lore_master.data.LibraryBooks;
import com.saphienyako.lore_master.entity.BellsnickelEntity;
import com.saphienyako.lore_master.entity.LoreMasterEntity;
import com.saphienyako.lore_master.entity.ModEntities;
import com.saphienyako.lore_master.entity.model.BellsnickelModel;
import com.saphienyako.lore_master.entity.model.ModModelLayers;
import com.saphienyako.lore_master.entity.renderer.BellsnickelRenderer;
import com.saphienyako.lore_master.item.ModCreativeModeTab;
import com.saphienyako.lore_master.item.ModItems;
import com.saphienyako.lore_master.network.LoreMasterNetwork;
import com.saphienyako.lore_master.sounds.ModSounds;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(LoreMasterMod.MOD_ID)
public class LoreMasterMod
{
    public static final String MOD_ID = "lore_master";
    private static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal")
    public LoreMasterMod(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::entityAttributes);
        modEventBus.addListener(LoreMasterNetwork::register);

        ModCreativeModeTab.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModSounds.register(modEventBus);

        NeoForge.EVENT_BUS.addListener(this::reloadData);

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    public void reloadData(AddReloadListenerEvent event) {
        event.addListener(LibraryBooks.createReloadListener(event.getRegistryAccess()));
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        //Added ModCreativeModeTab for the mod itself
    }

    private void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.LORE_MASTER.get(), LoreMasterEntity.createAttributes().build());
        event.put(ModEntities.BELLSNICKEL.get(), BellsnickelEntity.getDefaultAttributes().build());
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.LORE_MASTER.get(), VillagerRenderer::new);
            EntityRenderers.register(ModEntities.BELLSNICKEL.get(), BellsnickelRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.LIBRARY_BELL_BLOCK_ENTITY.get(), LibraryBellRenderer::new);
        }

        @SubscribeEvent
        private static void spawnPlacement(RegisterSpawnPlacementsEvent event) {
            event.register(ModEntities.LORE_MASTER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LoreMasterEntity::canSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
            event.register(ModEntities.BELLSNICKEL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BellsnickelEntity::canSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        }

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition( ModModelLayers.BELLSNICKEL_LAYER, BellsnickelModel::createBodyLayer);
        }

    }
}
