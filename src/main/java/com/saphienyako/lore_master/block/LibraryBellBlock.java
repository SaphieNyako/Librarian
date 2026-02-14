package com.saphienyako.lore_master.block;



import com.mojang.serialization.MapCodec;
import com.saphienyako.lore_master.block.entity.LibraryBellBlockEntity;
import com.saphienyako.lore_master.entity.BellsnickelEntity;
import com.saphienyako.lore_master.entity.LoreMasterEntity;
import com.saphienyako.lore_master.entity.ModEntities;
import com.saphienyako.lore_master.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Objects;

public class LibraryBellBlock extends BaseEntityBlock implements EntityBlock {

    public static final MapCodec<LibraryBellBlock> CODEC = simpleCodec(LibraryBellBlock::new);

    public static final VoxelShape SHAPE = box(0, 0, 0, 16, 16, 16);

    public LibraryBellBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock()) {

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof LibraryBellBlockEntity tile && level instanceof ServerLevel server) {

                if (tile.getLoreMaster() != null) {
                    Entity loreMaster = server.getEntity(tile.getLoreMaster());
                    if (loreMaster != null) {
                        loreMaster.remove(Entity.RemovalReason.DISCARDED);
                    }
                }

                if (tile.getSecurity() != null) {
                    Entity security = server.getEntity(tile.getSecurity());
                    if (security != null) {
                        security.remove(Entity.RemovalReason.DISCARDED);
                    }
                }
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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
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
                    if (security == null) {
                        summonSecurity(level, player, loreMaster.position(), blockEntity );
                    } else {
                        security.setPos(loreMaster.getX(), loreMaster.getY(), loreMaster.getZ());
                        if (security instanceof Mob) {
                            ((Mob) security).setTarget(player);
                        }
                    }
                } else if (blockEntity.getAnnoyance() > 6) {
                    player.sendSystemMessage(Component.translatable("message.lore_master.bell_annoyed"));
                }

                if (loreMaster != null) {
                    loreMaster.remove(Entity.RemovalReason.DISCARDED);
                }
                summonLoreMaster(level, player, blockEntity, pos);
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level,@Nonnull BlockState state,@Nonnull BlockEntityType<T> type) {
        if (level.isClientSide) return null;

        return (tickLevel, pos, blockState, be) -> {
            if (be instanceof LibraryBellBlockEntity bell && tickLevel instanceof ServerLevel serverLevel) {
                bell.serverTick(serverLevel);
            }
        };
    }




    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos blockPos,@Nonnull BlockState blockState) {
        return new LibraryBellBlockEntity(blockPos, blockState);
    }

    public void summonSecurity(Level level, Player player, Vec3 position, LibraryBellBlockEntity blockEntity){
        IronGolem golem = new IronGolem(EntityType.IRON_GOLEM, level);
        golem.setPlayerCreated(false);
        golem.setTarget(player);
        golem.setPersistenceRequired();
        player.sendSystemMessage(Component.translatable("message.lore_master.bell_angry"));
        golem.setPos(position);
        level.addFreshEntity(golem);
        blockEntity.setSecurity(golem.getUUID());
        if (ModList.get().isLoaded("feywild")) {
           golem.playSound(ModSounds.BELLSNICKEL_SECURITY.get());
        }
    }

    public void summonLoreMaster(Level level, Player player, LibraryBellBlockEntity blockEntity, BlockPos pos){
        if (ModList.get().isLoaded("feywild")) {
            BellsnickelEntity entity = new BellsnickelEntity(ModEntities.BELLSNICKEL.get(), level);
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
            if(blockEntity.getAnnoyance() < 10) {
                entity.playSound(getSummonSound(entity.level()));
            }
        } else {
            //if Feywild is not installed
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

        //if Feywild is installed

    }

    public SoundEvent getSummonSound(Level level){
        return switch (level.random.nextInt(5)) {
            case 0 -> ModSounds.BELLSNICKEL_SUMMON_01.get();
            case 1 -> ModSounds.BELLSNICKEL_SUMMON_02.get();
            case 2 -> ModSounds.BELLSNICKEL_SUMMON_03.get();
            default -> null;
        };
    }
}

