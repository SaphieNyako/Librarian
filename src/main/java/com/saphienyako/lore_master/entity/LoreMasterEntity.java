package com.saphienyako.lore_master.entity;

import com.saphienyako.lore_master.data.LibraryBooks;
import com.saphienyako.lore_master.network.LoreMasterNetwork;
import com.saphienyako.lore_master.network.LoreMasterScreenMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Objects;

public class LoreMasterEntity extends Villager {

    public LoreMasterEntity(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
    }

    public static boolean canSpawn(EntityType<? extends LoreMasterEntity> entityType, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).getTag(BlockTags.DIRT).contains(level.getBlockState(pos.below()).getBlock()) || Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).getTag(BlockTags.SAND).contains(level.getBlockState(pos.below()).getBlock());
    }

    @Nonnull
    @Override
    public InteractionResult interactAt(@Nonnull Player player, @Nonnull Vec3 vec, @Nonnull InteractionHand hand) {
        InteractionResult superResult = super.interactAt(player, vec, hand);
        Level level = player.level();
        if (superResult == InteractionResult.PASS) {
            if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                player.sendSystemMessage(Component.translatable("message.lore_master.initial"));
                LoreMasterNetwork.sendToPlayer(new LoreMasterScreenMessage(Component.translatable("entity.lore_master.lore_master"), LibraryBooks.getLibraryBooks()), serverPlayer);
                player.swing(hand, true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
}
