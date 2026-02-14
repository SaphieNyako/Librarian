package com.saphienyako.lore_master.item;

import com.saphienyako.lore_master.LoreMasterMod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LoreMasterMod.MOD_ID);

    public static void register(IEventBus eventBus) {ITEMS.register(eventBus);}
}
