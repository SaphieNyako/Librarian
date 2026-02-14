package com.saphienyako.lore_master.network;

import com.saphienyako.lore_master.LoreMasterMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


public class LoreMasterNetwork {

    public static final String PROTOCOL_VERSION = "1";

    public static PayloadRegistrar REGISTRAR;

    public static void register(RegisterPayloadHandlersEvent event) {
        REGISTRAR = event.registrar(LoreMasterMod.MOD_ID)
                .versioned(PROTOCOL_VERSION);

        REGISTRAR.playToClient(
               LoreMasterScreenMessage.TYPE,
               LoreMasterScreenMessage.STREAM_CODEC,
               LoreMasterScreenMessage::handle
        );

        REGISTRAR.playToServer(
                RequestItemMessage.TYPE,
                RequestItemMessage.STREAM_CODEC,
                RequestItemMessage::handle
        );
    }
}
