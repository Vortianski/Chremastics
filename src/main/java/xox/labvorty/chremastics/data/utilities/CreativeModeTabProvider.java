package xox.labvorty.chremastics.data.utilities;

import net.minecraft.world.item.ItemStack;
import xox.labvorty.chremastics.init.ChremasticsDataComponents;
import xox.labvorty.chremastics.init.ChremasticsItems;

import java.util.ArrayList;
import java.util.List;

public class CreativeModeTabProvider {
    public static List<ItemStack> getCoins() {
        List<ItemStack> itemStacks = new ArrayList<>();

        itemStacks.add(ChremasticsItems.COPPER_COIN.get().getDefaultInstance());
        itemStacks.add(ChremasticsItems.SILVER_COIN.get().getDefaultInstance());
        itemStacks.add(ChremasticsItems.GOLD_COIN.get().getDefaultInstance());
        itemStacks.add(ChremasticsItems.PLATINUM_COIN.get().getDefaultInstance());

        return itemStacks;
    }

    public static List<ItemStack> getCoinBags() {
        List<ItemStack> itemStacks = new ArrayList<>();
        List<Integer> values = List.of(
                1,
                33,
                66,
                99,
                100,
                3300,
                6600,
                9900,
                10000,
                330000,
                660000,
                990000,
                1000000,
                33000000,
                66000000,
                99000000
        );

        for (int value : values) {
            ItemStack itemStack = ChremasticsItems.COIN_BAG.get().getDefaultInstance();
            itemStack.set(ChremasticsDataComponents.VALUE, value);
            itemStacks.add(itemStack);
        }

        return itemStacks;
    }
}
