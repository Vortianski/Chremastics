package xox.labvorty.chremastics.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xox.labvorty.chremastics.Chremastics;
import xox.labvorty.chremastics.blocks.entities.CoinPileBlockEntity;

public class ChremasticsBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Chremastics.MOD_ID);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CoinPileBlockEntity>> COIN_PILE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            "coin_pile_block_entity",
            () -> BlockEntityType.Builder.of(
                    CoinPileBlockEntity::new,
                    ChremasticsBlocks.COIN_PILE.get()
            ).build(null)
    );
}
