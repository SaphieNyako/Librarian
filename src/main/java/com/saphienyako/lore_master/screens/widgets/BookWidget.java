package com.saphienyako.lore_master.screens.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.saphienyako.lore_master.LoreMasterMod;
import com.saphienyako.lore_master.network.LoreMasterNetwork;
import com.saphienyako.lore_master.network.RequestItemMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class BookWidget extends Button {

    private static final ResourceLocation BUTTON = new ResourceLocation(LoreMasterMod.MOD_ID, "textures/gui/button.png");
    public static final int WIDTH = 25;
    public static final int HEIGHT = 25;

    protected final Screen screen;
    protected final int bookId;
    protected final ItemStack stack;

    public BookWidget(Screen screen, int x, int y, int bookId, ItemStack stack) {
        super(x, y, WIDTH, HEIGHT, stack.getDisplayName(), b -> {}, l -> Component.empty());
        this.screen = screen;
        this.bookId = bookId;
        this.stack = stack;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public void onPress() {
        super.onPress();
        LoreMasterNetwork.sendToServer(new RequestItemMessage(this.bookId));
        this.screen.onClose();
    }


    @Override
    public void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(BUTTON, this.getX(), this.getY(), 0, 0, 25, 25);
        if (this.isHovered(mouseX, mouseY)) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 10);
            graphics.blit(BUTTON, this.getX(), this.getY(), 25, 0, 25, 25);
            graphics.pose().popPose();
        }
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 50);
        graphics.renderFakeItem(this.stack, this.getX() + 4, this.getY() + 4);
        graphics.pose().popPose();
    }

    public boolean isHovered(int x, int y) {
        return this.getX() <= x && this.getX() + WIDTH >= x && this.getY() <= y && this.getY() + HEIGHT >= y;
    }
}
