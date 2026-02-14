package com.saphienyako.lore_master.block.entity;

import com.saphienyako.lore_master.LoreMasterMod;
import com.saphienyako.lore_master.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, LoreMasterMod.MOD_ID);

    public static final Supplier<BlockEntityType<LibraryBellBlockEntity>> LIBRARY_BELL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("library_bell_block_entity", () ->
                    BlockEntityType.Builder.of(LibraryBellBlockEntity::new, ModBlocks.LIBRARY_BELL.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
