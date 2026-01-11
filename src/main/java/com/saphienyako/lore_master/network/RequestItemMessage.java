package com.saphienyako.lore_master.network;

import com.saphienyako.lore_master.data.LibraryBooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record RequestItemMessage(int bookId) {

    public static void encode(RequestItemMessage msg, FriendlyByteBuf buffer) {
        buffer.writeVarInt(msg.bookId());
    }

    public static RequestItemMessage decode(FriendlyByteBuf buffer) {
        return new RequestItemMessage(buffer.readVarInt());
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        ServerPlayer player = supplier.get().getSender();
        if (player != null) {
             player.getInventory().add(LibraryBooks.getBook(this.bookId).copy());
            }
    }
}

