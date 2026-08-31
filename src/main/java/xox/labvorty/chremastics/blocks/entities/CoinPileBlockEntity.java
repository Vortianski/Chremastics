package xox.labvorty.chremastics.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import xox.labvorty.chremastics.init.ChremasticsBlockEntities;

import java.util.UUID;

public class CoinPileBlockEntity extends BlockEntity {
    private UUID owner;

    public CoinPileBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ChremasticsBlockEntities.COIN_PILE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public UUID getOwner() { return owner; }

    public void setOwner(UUID owner) {
        this.owner = owner;
        setChanged();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(compoundTag, registries);

        if (owner != null) {
            compoundTag.putUUID("Owner", owner);
        }
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(compoundTag, registries);

        owner = compoundTag.hasUUID("Owner") ? compoundTag.getUUID("Owner") : null;
    }
}