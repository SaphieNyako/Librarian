package com.saphienyako.lore_master;

import com.mojang.logging.LogUtils;
import com.saphienyako.lore_master.block.ModBlocks;
import com.saphienyako.lore_master.block.entity.LibraryBellBlockEntity;
import com.saphienyako.lore_master.block.entity.ModBlockEntities;
import com.saphienyako.lore_master.block.renderer.LibraryBellRenderer;
import com.saphienyako.lore_master.data.LibraryBooks;
import com.saphienyako.lore_master.entity.LoreMasterEntity;
import com.saphienyako.lore_master.entity.ModEntities;
import com.saphienyako.lore_master.item.ModCreativeModeTab;
import com.saphienyako.lore_master.item.ModItems;
import com.saphienyako.lore_master.network.LoreMasterNetwork;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LoreMasterMod.MOD_ID)
public class LoreMasterMod
{
    public static final String MOD_ID = "lore_master";
    private static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal")
    public LoreMasterMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::entityAttributes);

        ModCreativeModeTab.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(this::reloadData);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(LoreMasterNetwork::register);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        //Added ModCreativeModeTab for the mod itself
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.LORE_MASTER.get(), VillagerRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.LIBRARY_BELL_BLOCK_ENTITY.get(), LibraryBellRenderer::new);
        }
    }

    private void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.LORE_MASTER.get(), LoreMasterEntity.createAttributes().build());
    }

    private void spawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.LORE_MASTER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LoreMasterEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    public void reloadData(AddReloadListenerEvent event) {
        event.addListener(LibraryBooks.createReloadListener());
    }
}
