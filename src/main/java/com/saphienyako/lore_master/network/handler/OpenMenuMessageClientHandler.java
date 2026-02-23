package com.saphienyako.lore_master.network.handler;

import com.saphienyako.lore_master.screens.LibrarianScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class OpenMenuMessageClientHandler {

    public static void openMenu(Component title, List<ItemStack> books) {
        Minecraft.getInstance().setScreen(new LibrarianScreen(title, books));
    }

}
