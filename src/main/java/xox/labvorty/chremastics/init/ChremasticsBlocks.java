package xox.labvorty.chremastics.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xox.labvorty.chremastics.Chremastics;
import xox.labvorty.chremastics.blocks.CoinPileBlock;

public class ChremasticsBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(Chremastics.MOD_ID);
    public static final DeferredHolder<Block, CoinPileBlock> COIN_PILE = BLOCKS.register("coin_pile", () -> new CoinPileBlock(BlockBehaviour.Properties.of()));
}
