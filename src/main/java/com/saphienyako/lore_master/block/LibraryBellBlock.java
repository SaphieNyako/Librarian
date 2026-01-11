package com.saphienyako.lore_master.block;



import com.saphienyako.lore_master.block.entity.LibraryBellBlockEntity;
import com.saphienyako.lore_master.entity.LoreMasterEntity;
import com.saphienyako.lore_master.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class LibraryBellBlock extends BaseEntityBlock implements EntityBlock {

    public static final VoxelShape SHAPE = box(0, 0, 0, 16, 16, 16);

    public LibraryBellBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@Nonnull BlockState state,@Nonnull Level level,@Nonnull BlockPos pos,@Nonnull BlockState newState, boolean moving) {
        if (!level.isClientSide && level instanceof ServerLevel) {
            LibraryBellBlockEntity tile =  new LibraryBellBlockEntity(pos, newState);
            if (tile.getLoreMaster() != null) {
                Entity loreMaster = ((ServerLevel) level).getEntity(tile.getLoreMaster());
                if (loreMaster instanceof Villager) ((Villager) loreMaster).releaseAllPois();
                if (loreMaster != null) loreMaster.remove(Entity.RemovalReason.DISCARDED);
            }
            if (tile.getSecurity() != null) {
                Entity security = ((ServerLevel) level).getEntity(tile.getSecurity());
                if (security != null) security.remove(Entity.RemovalReason.DISCARDED);
            }
        }
        super.onRemove(state, level, pos, newState, moving);
    }

    @Nonnull
    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter levelIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    private static LibraryBellBlockEntity getBlockEntity(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof LibraryBellBlockEntity bell ? bell : null;
    }


    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult trace) {
        if (level.isClientSide) {
            level.playSound(player, pos, SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.BLOCKS, 1f, 1.2f);
        } else {
            LibraryBellBlockEntity blockEntity = getBlockEntity(level,pos);
            if (player.getGameProfile().getId().equals(blockEntity.getPlayer())) {
                blockEntity.setAnnoyance(blockEntity.getAnnoyance() + 1);
            } else {
                blockEntity.setPlayer(player.getGameProfile().getId());
                blockEntity.setAnnoyance(0);
            }

            if (level instanceof ServerLevel) {
                Entity loreMaster = blockEntity.getLoreMaster() != null ? ((ServerLevel) level).getEntity(blockEntity.getLoreMaster()) : null;
                Entity security = blockEntity.getSecurity() != null ? ((ServerLevel) level).getEntity(blockEntity.getSecurity()) : null;
                if (blockEntity.getAnnoyance() >= 10 && loreMaster != null && loreMaster.isAlive()) {
                    blockEntity.setAnnoyance(0);
                    //TODO Replace with??
                    if (security == null) {
                        IronGolem golem = new IronGolem(EntityType.IRON_GOLEM, level);
                        golem.setPlayerCreated(false);
                        golem.setTarget(player);
                        player.sendSystemMessage(Component.translatable("message.lore_master.bell_angry"));
                        golem.setPos(loreMaster.getX(), loreMaster.getY(), loreMaster.getZ());
                        level.addFreshEntity(golem);
                        blockEntity.setSecurity(golem.getUUID());

                    } else {
                        security.setPos(loreMaster.getX(), loreMaster.getY(), loreMaster.getZ());
                        if (security instanceof Mob) {
                            ((Mob) security).setTarget(player);
                        }
                    }
                } else if (blockEntity.getAnnoyance() > 6) {
                    player.sendSystemMessage(Component.translatable("message.lore_master.bell_annoyed"));
                }

                if (loreMaster != null && loreMaster.isAlive()) {
                    if (loreMaster instanceof Villager) ((Villager) loreMaster).releaseAllPois();
                    loreMaster.remove(Entity.RemovalReason.DISCARDED);
                }

                LoreMasterEntity entity = new LoreMasterEntity(ModEntities.LORE_MASTER.get(), level);
                VillagerData villagerData = new VillagerData(VillagerType.byBiome(player.level().getBiome(pos)), VillagerProfession.LIBRARIAN, 1);
                entity.setVillagerData(villagerData);
                entity.setVillagerXp(1);
                entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                for (Direction dir : Direction.values()) {
                    if (dir.getAxis() != Direction.Axis.Y) {
                        BlockPos target = pos.below().relative(dir);
                        if (level.getBlockState(target).isAir()) {
                            entity.setPos(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
                            break;
                        }
                    }
                }
                level.addFreshEntity(entity);
                blockEntity.setLoreMaster(entity.getUUID());
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public boolean isRandomlyTicking(@Nonnull BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        LibraryBellBlockEntity entity = (LibraryBellBlockEntity) level.getBlockEntity(pos);

        if (entity != null && entity.getSecurity() != null) {
            Entity security = level.getEntity(entity.getSecurity());
            if (security != null) {
                entity.setDespawnTimer(entity.getDespawnTimer() + 1);
                if (entity.getDespawnTimer() >= 2) {
                    entity.setDespawnTimer(0);
                    security.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos blockPos,@Nonnull BlockState blockState) {
        return new LibraryBellBlockEntity(blockPos, blockState);
    }
}

