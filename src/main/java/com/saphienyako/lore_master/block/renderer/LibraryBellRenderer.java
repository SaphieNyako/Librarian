package com.saphienyako.lore_master.block.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.saphienyako.lore_master.block.entity.LibraryBellBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class LibraryBellRenderer<T extends LibraryBellBlockEntity> implements BlockEntityRenderer<LibraryBellBlockEntity> {


    public LibraryBellRenderer(BlockEntityRendererProvider.Context context) {
    }


    @Override
    public void render(@Nonnull LibraryBellBlockEntity libraryBellBlockEntity, float v,@Nonnull PoseStack poseStack,@Nonnull MultiBufferSource multiBufferSource, int i, int i1) {

    }
}
