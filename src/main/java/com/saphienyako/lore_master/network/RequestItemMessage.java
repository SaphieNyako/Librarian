package com.saphienyako.lore_master.network;

import com.saphienyako.lore_master.LoreMasterMod;
import com.saphienyako.lore_master.data.LibraryBooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RequestItemMessage(int bookId) implements CustomPacketPayload {

    public static final Type<RequestItemMessage> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(LoreMasterMod.MOD_ID, "request_item"));

    public static final StreamCodec<FriendlyByteBuf, RequestItemMessage> STREAM_CODEC =
            StreamCodec.of(RequestItemMessage::encode, RequestItemMessage::decode);

    private static void encode(FriendlyByteBuf buffer, RequestItemMessage msg){
        buffer.writeVarInt(msg.bookId());
    }

    public static RequestItemMessage decode(FriendlyByteBuf buffer) {
        return new RequestItemMessage(buffer.readVarInt());
    }

    public static void handle(RequestItemMessage msg, IPayloadContext context) {
        context.enqueueWork(() -> {
            context.player().getInventory().add(LibraryBooks.getBook(msg.bookId()).copy());
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

