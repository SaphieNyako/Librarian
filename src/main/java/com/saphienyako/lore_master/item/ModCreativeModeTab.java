package com.saphienyako.lore_master.item;

import com.saphienyako.lore_master.LoreMasterMod;
import com.saphienyako.lore_master.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class ModCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LoreMasterMod.MOD_ID);

    public static final Supplier<CreativeModeTab> LIBRARIAN_BOOKS_TAB = CREATIVE_MODE_TAB.register("lore_master_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.LIBRARY_BELL.get()))
                    .title(Component.translatable("creative_tab.lore_master_creative_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.LIBRARY_BELL.get());
                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
