package xox.labvorty.chremastics.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import xox.labvorty.chremastics.data.components.CoinBagTooltipComponent;
import xox.labvorty.chremastics.data.currency.CurrencyHandlers;
import xox.labvorty.chremastics.data.currency.PlayerCoinPurse;
import xox.labvorty.chremastics.init.ChremasticsDataComponents;
import xox.labvorty.vortylib.utilities.VortyLibUtilities;

import java.util.List;
import java.util.Optional;

public class CoinBagItem extends Item {
    public CoinBagItem() {
        super(
                new Properties()
                        .stacksTo(8)
                        .component(ChremasticsDataComponents.VALUE, 0L)
        );
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level,
            @NotNull Player player,
            @NotNull InteractionHand interactionHand
    ) {
        if (!level.isClientSide) {
            ItemStack itemStack = player.getItemInHand(interactionHand);

            long value = itemStack.getOrDefault(
                    ChremasticsDataComponents.VALUE,
                    0L
            );

            Pair<List<ItemStack>, Long> result =
                    CurrencyHandlers.getStacksFromValueLimited(
                            value,
                            CurrencyHandlers.MAX_PHYSICAL_STACKS
                    );

            for (ItemStack stack : result.getKey()) {
                VortyLibUtilities.tryInsertOrDrop(player, stack);
            }

            long remainingValue = result.getValue();

            if (remainingValue > 0) {
                PlayerCoinPurse.addBalance(
                        (ServerPlayer)player,
                        remainingValue
                );
            }

            if (!player.isCreative()) {
                itemStack.shrink(1);
            }
        }

        return super.use(level, player, interactionHand);
    }


    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack itemStack) {
        return Optional.of(new CoinBagTooltipComponent(itemStack.getOrDefault(ChremasticsDataComponents.VALUE, 0L)));
    }
}
