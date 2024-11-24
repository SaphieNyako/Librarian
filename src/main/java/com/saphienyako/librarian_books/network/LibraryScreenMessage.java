package com.saphienyako.librarian_books.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public record LibraryScreenMessage(Component title, List<ItemStack> books) {

    public static void encode(LibraryScreenMessage msg, FriendlyByteBuf buffer) {
        buffer.writeComponent(msg.title());
        PacketUtil.writeList(msg.books(), buffer, FriendlyByteBuf::writeItem);
    }

    public static LibraryScreenMessage decode(FriendlyByteBuf buffer) {
        Component cmp = buffer.readComponent();
        List<ItemStack> stacks = PacketUtil.readList(buffer, FriendlyByteBuf::readItem);
        return new LibraryScreenMessage(cmp, stacks);
    }
    public void handle(Supplier<NetworkEvent.Context> supplier) {
       // Minecraft.getInstance().setScreen(new LibrarianScreen(msg.title(), msg.books())); //TODO add Screen
    }
}
