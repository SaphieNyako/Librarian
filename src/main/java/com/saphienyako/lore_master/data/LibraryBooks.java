package com.saphienyako.lore_master.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class LibraryBooks {
    private static List<ItemStack> books = ImmutableList.of();

    public static List<ItemStack> getLibraryBooks() {
        return books;
    }

    public static ItemStack getBook(int idx) {
        if (idx < 0 || idx >= books.size()) {
            return ItemStack.EMPTY;
        } else {
            return books.get(idx).copy();
        }
    }

    public static PreparableReloadListener createReloadListener(RegistryAccess registryAccess) {
        return new SimplePreparableReloadListener<Void>() {
            @Nonnull
            @Override
            protected Void prepare(@Nonnull ResourceManager manager, @Nonnull ProfilerFiller profiler) {
                return null;
            }

            @Override
            protected void apply(@Nonnull Void value, @Nonnull ResourceManager manager, @Nonnull ProfilerFiller profiler) {
                books = DatapackHelper.loadStackList(manager, "lore_master_books", "books", registryAccess);
            }
        };
    }
}
