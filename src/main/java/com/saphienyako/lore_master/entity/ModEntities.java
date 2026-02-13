package com.saphienyako.lore_master.entity;

import com.saphienyako.lore_master.LoreMasterMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LoreMasterMod.MOD_ID);

    public static final RegistryObject<EntityType<LoreMasterEntity>> LORE_MASTER =
            ENTITY_TYPES.register("lore_master", () -> EntityType.Builder.of(LoreMasterEntity::new, MobCategory.CREATURE).build("lore_master"));

    public static final RegistryObject<EntityType<BellsnickelEntity>> BELLSNICKEL =
            ENTITY_TYPES.register("bellsnickel", () -> EntityType.Builder.of(BellsnickelEntity::new, MobCategory.CREATURE).build("bellsnickel"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
