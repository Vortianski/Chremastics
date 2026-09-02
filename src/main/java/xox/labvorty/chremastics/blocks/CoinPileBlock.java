package xox.labvorty.chremastics.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xox.labvorty.chremastics.blocks.entities.CoinPileBlockEntity;
import xox.labvorty.chremastics.data.configs.ClientConfig;
import xox.labvorty.chremastics.data.currency.Currency;
import xox.labvorty.chremastics.data.currency.CurrencyHandlers;
import xox.labvorty.chremastics.data.events.CoinPileTakenEvent;
import xox.labvorty.chremastics.init.ChremasticsItems;
import xox.labvorty.chremastics.init.ChremasticsSoundTypes;
import xox.labvorty.chremastics.items.CoinItem;
import xox.labvorty.chremastics.particles.options.CoinSparkleOptions;

import java.util.List;
import java.util.UUID;

public class CoinPileBlock extends Block implements EntityBlock {
    public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 16);
    public static final EnumProperty<Currency> CURRENCY = EnumProperty.create("currency", Currency.class);

    public CoinPileBlock(Properties properties) {
        super(
                properties
                        .strength(0.1f)
                        .pushReaction(PushReaction.DESTROY)
                        .noOcclusion()
                        .dynamicShape()
                        .sound(ChremasticsSoundTypes.COIN)
        );
        registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(LAYERS, 1)
                        .setValue(CURRENCY, Currency.COPPER)
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(LAYERS, CURRENCY);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new CoinPileBlockEntity(blockPos, blockState);
    }

    @Override
    public void setPlacedBy(
            @NotNull Level level,
            @NotNull BlockPos blockPos,
            @NotNull BlockState blockState,
            @Nullable LivingEntity livingEntity,
            @NotNull ItemStack itemStack
    ) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);

        if (!level.isClientSide && livingEntity instanceof Player player && level.getBlockEntity(blockPos) instanceof CoinPileBlockEntity coinPileBlockEntity) {
            coinPileBlockEntity.setOwner(player.getUUID());
        }
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(
            @NotNull ItemStack itemStack,
            @NotNull BlockState blockState,
            @NotNull Level level,
            @NotNull BlockPos blockPos,
            @NotNull Player player,
            @NotNull InteractionHand interactionHand,
            @NotNull BlockHitResult blockHitResult
    ) {
        Currency currency = blockState.getValue(CURRENCY);
        int layers = blockState.getValue(LAYERS);

        if (itemStack.getItem() instanceof CoinItem coinItem) {
            Currency itemCurrency = coinItem.getCurrency();

            if (currency == itemCurrency && !player.isShiftKeyDown()) {
                if (layers < 16) {
                    if (!level.isClientSide) {
                        BlockState newState = blockState.setValue(LAYERS, layers + 1);
                        level.setBlock(blockPos, newState, 2);
                        level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.soundType.getPlaceSound(), SoundSource.PLAYERS);

                        if (!player.isCreative()) {
                            itemStack.shrink(1);
                        }
                    }

                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }

                return ItemInteractionResult.FAIL;
            }
        }

        return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(
            @NotNull BlockState blockState,
            @NotNull Level level,
            @NotNull BlockPos blockPos,
            @NotNull Player player,
            @NotNull BlockHitResult blockHitResult
    ) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(level.getBlockEntity(blockPos) instanceof CoinPileBlockEntity coinPileBlockEntity)) {
            return InteractionResult.PASS;
        }

        UUID owner = coinPileBlockEntity.getOwner();
        CoinPileTakenEvent event;

        if (owner == null) {
            event = new CoinPileTakenEvent.Unowned(player, blockPos);
        } else if (owner.equals(player.getUUID())) {
            event = new CoinPileTakenEvent.Owner(player, blockPos);
        } else {
            event = new CoinPileTakenEvent.NotOwner(player, blockPos, owner);
        }

        NeoForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            return InteractionResult.FAIL;
        }

        int layers = blockState.getValue(LAYERS);
        Currency currency = blockState.getValue(CURRENCY);

        Item coin = switch (currency) {
            case COPPER -> ChremasticsItems.COPPER_COIN.get();
            case SILVER -> ChremasticsItems.SILVER_COIN.get();
            case GOLD -> ChremasticsItems.GOLD_COIN.get();
            case PLATINUM -> ChremasticsItems.PLATINUM_COIN.get();
        };

        ItemStack coinStack = new ItemStack(coin, 1);

        if (!player.getInventory().add(coinStack)) {
            player.drop(coinStack, false);
        }

        level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.soundType.getBreakSound(), SoundSource.PLAYERS);

        if (layers <= 1) {
            level.removeBlock(blockPos, false);
        } else {
            level.setBlock(blockPos, blockState.setValue(LAYERS, layers - 1), 2);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        int layers = blockState.getValue(LAYERS);
        return Block.box(0, 0, 0, 16, layers, 16);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(
            @NotNull BlockState blockState,
            @NotNull BlockGetter blockGetter,
            @NotNull BlockPos blockPos,
            @NotNull CollisionContext collisionContext
    ) {
        int layers = blockState.getValue(LAYERS);
        return Block.box(0, 0, 0, 16, layers, 16);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState blockState, @NotNull LevelReader levelReader, @NotNull BlockPos blockPos) {
        return levelReader.getBlockState(blockPos.below()).isFaceSturdy(levelReader, blockPos.below(), Direction.UP);
    }

    @Override
    protected void neighborChanged(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Block block,
            @NotNull BlockPos fromPos,
            boolean isMoving
    ) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        if (fromPos.equals(pos.below())) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    protected void tick(
            @NotNull BlockState state,
            @NotNull ServerLevel serverLevel,
            @NotNull BlockPos pos,
            @NotNull net.minecraft.util.RandomSource random
    ) {
        if (!state.canSurvive(serverLevel, pos)) {
            serverLevel.destroyBlock(pos, true);
        }
    }

    @Override
    protected @NotNull List<ItemStack> getDrops(@NotNull BlockState blockState, LootParams.@NotNull Builder builder) {
        int layers = blockState.getValue(LAYERS);
        Currency currency = blockState.getValue(CURRENCY);

        ItemStack itemStack = new ItemStack(CurrencyHandlers.getCoinFromCurrency(currency), layers);

        return List.of(itemStack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos blockPos,
            @NotNull RandomSource randomSource
    ) {
        if (!ClientConfig.SPECIAL_EFFECTS.get()) return;
        if (ClientConfig.PARTICLE_CHANCE.get() < randomSource.nextDouble()) return;

        int layers = state.getValue(LAYERS);

        double x = blockPos.getX() + 0.5D + (randomSource.nextDouble() - 0.5D) * 0.6D;
        double y = blockPos.getY() + layers / 16.0D;
        double z = blockPos.getZ() + 0.5D + (randomSource.nextDouble() - 0.5D) * 0.6D;

        level.addParticle(
                CoinSparkleOptions.fromColor(state.getValue(CURRENCY).getSparkleColor()),
                x, y, z,
                0.0D,
                0.01D + randomSource.nextDouble() * 0.015D,
                0.0D
        );
    }
}