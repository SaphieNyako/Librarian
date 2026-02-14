package com.saphienyako.lore_master.entity;

import com.saphienyako.lore_master.LoreMasterMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, LoreMasterMod.MOD_ID);

    public static final Supplier<EntityType<LoreMasterEntity>> LORE_MASTER =
            ENTITY_TYPES.register("lore_master", () -> EntityType.Builder.of(LoreMasterEntity::new, MobCategory.CREATURE).build("lore_master"));

    public static final Supplier<EntityType<BellsnickelEntity>> BELLSNICKEL =
            ENTITY_TYPES.register("bellsnickel", () -> EntityType.Builder.of(BellsnickelEntity::new, MobCategory.CREATURE).build("bellsnickel"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
