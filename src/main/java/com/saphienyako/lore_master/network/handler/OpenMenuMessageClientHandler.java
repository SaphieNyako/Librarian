package com.saphienyako.lore_master.network.handler;

import com.saphienyako.lore_master.network.LoreMasterScreenMessage;
import com.saphienyako.lore_master.screens.LibrarianScreen;
import net.minecraft.client.Minecraft;

public class OpenMenuMessageClientHandler {

    public static void openMenu(LoreMasterScreenMessage msg) {
            Minecraft.getInstance().setScreen(new LibrarianScreen());
        }
}


