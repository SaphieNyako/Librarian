package com.saphienyako.lore_master.item;

import com.saphienyako.lore_master.LoreMasterMod;
import com.saphienyako.lore_master.entity.ModEntities;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LoreMasterMod.MOD_ID);

    public static final RegistryObject<Item> SPAWN_EGG_LORE_MASTER = ITEMS.register("spawn_egg_lore_master", () -> new ForgeSpawnEggItem(ModEntities.LORE_MASTER, 0xf085a9, 0xa1db67, new Item.Properties()));


    public static void register(IEventBus eventBus) {ITEMS.register(eventBus);}
}
