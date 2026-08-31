package xox.labvorty.chremastics.items;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import xox.labvorty.chremastics.blocks.CoinPileBlock;
import xox.labvorty.chremastics.data.currency.Currency;
import xox.labvorty.vortylib.data.text.ComponentFormatter;

import java.util.List;

public class CoinItem extends BlockItem {
    private final Currency currency;

    public CoinItem(Currency currency, Block block) {
        super(
                block,
                new Properties()
                        .stacksTo(99)
        );
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    protected BlockState getPlacementState(@NotNull BlockPlaceContext blockPlaceContext) {
        BlockState state = super.getPlacementState(blockPlaceContext);

        if (state != null && state.hasProperty(CoinPileBlock.CURRENCY)) {
            state = state.setValue(CoinPileBlock.CURRENCY, currency);
        }

        return state;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack) {
        Minecraft minecraft = Minecraft.getInstance();
        Component component = Component.translatable(this.getDescriptionId(itemStack));
        int ticks = 0;
        if (minecraft.level != null) {
            ticks = (int)minecraft.level.getGameTime();
        }

        return ComponentFormatter.gradientText(component.getString(), List.of(currency.getNameColor(), currency.getGlintColor()), ticks);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        Minecraft minecraft = Minecraft.getInstance();
        int ticks = 0;
        if (minecraft.level != null) {
            ticks = (int)minecraft.level.getGameTime();
        }

        tooltipComponents.add(ComponentFormatter.gradientText(Component.translatable("chremastics.coin.value", currency.getValue()).getString(), List.of(Currency.COPPER.getNameColor(), Currency.COPPER.getGlintColor()), ticks));

        super.appendHoverText(itemStack, tooltipContext, tooltipComponents, tooltipFlag);
    }
}