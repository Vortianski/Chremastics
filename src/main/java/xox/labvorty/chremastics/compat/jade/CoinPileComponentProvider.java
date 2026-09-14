package xox.labvorty.chremastics.compat.jade;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import xox.labvorty.chremastics.blocks.CoinPileBlock;
import xox.labvorty.chremastics.data.currency.Currency;
import xox.labvorty.chremastics.data.currency.CurrencyHandlers;

public enum CoinPileComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            BlockAccessor accessor,
            IPluginConfig config
    ) {
        tooltip.clear();

        tooltip.add(
                Component.translatable("block.chremastics.coin_pile").withStyle(ChatFormatting.WHITE)
        );

        Currency currency = accessor.getBlockState().getValue(CoinPileBlock.CURRENCY);
        int layers = accessor.getBlockState().getValue(CoinPileBlock.LAYERS);
        Item coin = CurrencyHandlers.getCoinFromCurrency(currency);

        tooltip.add(
                coin.getName(coin.getDefaultInstance()),
                ChremasticsJadePlugin.COIN_PILE
        );

        tooltip.add(
                Component.translatable("chremastics.jade.layers", layers)
        );
    }

    @Override
    public ResourceLocation getUid() {
        return ChremasticsJadePlugin.COIN_PILE;
    }
}