package com.saphienyako.lore_master.network;

import com.saphienyako.lore_master.network.handler.OpenMenuMessageClientHandler;
import com.saphienyako.lore_master.screens.LibrarianScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.function.Supplier;

public record LoreMasterScreenMessage() implements CustomPacketPayload {

    public static final Type<LoreMasterScreenMessage> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("lore_master", "open_lore_screen"));

    public static final StreamCodec<FriendlyByteBuf, LoreMasterScreenMessage> STREAM_CODEC =
            StreamCodec.of(LoreMasterScreenMessage::encode, LoreMasterScreenMessage::decode);

    private static void encode(FriendlyByteBuf buffer, LoreMasterScreenMessage msg){

    }

    private static LoreMasterScreenMessage decode(FriendlyByteBuf buffer) {
        return new LoreMasterScreenMessage();
    }


    public static void handle(LoreMasterScreenMessage msg, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                OpenMenuMessageClientHandler.openMenu(msg);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
