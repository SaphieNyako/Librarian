package com.saphienyako.lore_master.block.entity;

import com.saphienyako.lore_master.LoreMasterMod;
import com.saphienyako.lore_master.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LoreMasterMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<LibraryBellBlockEntity>> LIBRARY_BELL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("library_bell_block_entity", ()->
                    BlockEntityType.Builder.of(LibraryBellBlockEntity::new, ModBlocks.LIBRARY_BELL.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
