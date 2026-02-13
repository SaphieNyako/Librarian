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

    public static void register(IEventBus eventBus) {ITEMS.register(eventBus);}
}
