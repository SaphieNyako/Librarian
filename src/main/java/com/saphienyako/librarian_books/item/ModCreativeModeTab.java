package com.saphienyako.librarian_books.item;

import com.saphienyako.librarian_books.LibrarianBooks;
import com.saphienyako.librarian_books.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LibrarianBooks.MOD_ID);

    public static final RegistryObject<CreativeModeTab> LIBRARIAN_BOOKS_TAB = CREATIVE_MODE_TAB.register("librarian_books_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.LIBRARY_BELL.get()))
                    .title(Component.translatable("creative_tab.librarian_books_creative_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.LIBRARY_BELL.get());
                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
