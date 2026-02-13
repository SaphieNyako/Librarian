package com.saphienyako.lore_master.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.UUID;

public class LibraryBellBlockEntity extends BlockEntity {

    private int annoyance = 0;
    private int despawnTimer = 0;


    private UUID player = null;


    private UUID loreMaster = null;

    private UUID security = null;

    public LibraryBellBlockEntity( BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.LIBRARY_BELL_BLOCK_ENTITY.get(), worldPosition, blockState);
    }

    public int getAnnoyance() {
        return this.annoyance;
    }

    public void setAnnoyance(int annoyance) {
        this.annoyance = annoyance;
    }

    public UUID getPlayer() {
        return this.player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    @Nonnull
    public UUID getLoreMaster() {
        return this.loreMaster;
    }

    public void setLoreMaster(UUID loreMaster) {
        this.loreMaster = loreMaster;
    }


    public UUID getSecurity() {
        return this.security;
    }

    public void setSecurity( UUID security) {
        this.security = security;
    }

    public int getDespawnTimer() {
        return despawnTimer;
    }

    public void setDespawnTimer(int despawnTimer) {
        this.despawnTimer = despawnTimer;
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.annoyance = nbt.getInt("Annoyance");
        this.player = nbt.hasUUID("Player") ? nbt.getUUID("Player") : null;
        this.loreMaster = nbt.hasUUID("LoreMaster") ? nbt.getUUID("LoreMaster") : null;
        this.security = nbt.hasUUID("Security") ? nbt.getUUID("Security") : null;
        this.despawnTimer = nbt.getInt("DespawnTimer");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("Annoyance", this.annoyance);
        nbt.putInt("DespawnTimer", this.despawnTimer);
        if (this.player == null) {
            nbt.remove("Player");
        } else {
            nbt.putUUID("Player", this.player);
        }
        if (this.loreMaster == null) {
            nbt.remove("LoreMaster");
        } else {
            nbt.putUUID("LoreMaster", this.loreMaster);
        }
        if (this.security == null) {
            nbt.remove("Security");
        } else {
            nbt.putUUID("Security", this.security);
        }
    }

    public void serverTick(ServerLevel level) {
        if (this.security == null) return;

        Entity securityEntity = level.getEntity(this.security);
        if (securityEntity == null || !securityEntity.isAlive()) {
            this.security = null;
            this.despawnTimer = 0;
            setChanged();
            return;
        }

        this.despawnTimer++;

        if (this.despawnTimer >= 20 * 5) {
            securityEntity.remove(Entity.RemovalReason.DISCARDED);
            this.security = null;
            this.despawnTimer = 0;
            setChanged();
        }
    }
}

