package xox.labvorty.chremastics.init;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xox.labvorty.chremastics.Chremastics;
import xox.labvorty.chremastics.data.currency.Currency;
import xox.labvorty.chremastics.items.CoinBagItem;
import xox.labvorty.chremastics.items.CoinItem;

public class ChremasticsItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Chremastics.MOD_ID);
    public static final DeferredHolder<Item, CoinItem> COPPER_COIN = ITEMS.register("copper_coin", () -> new CoinItem(Currency.COPPER, ChremasticsBlocks.COIN_PILE.get()));
    public static final DeferredHolder<Item, CoinItem> SILVER_COIN = ITEMS.register("silver_coin", () -> new CoinItem(Currency.SILVER, ChremasticsBlocks.COIN_PILE.get()));
    public static final DeferredHolder<Item, CoinItem> GOLD_COIN = ITEMS.register("gold_coin", () -> new CoinItem(Currency.GOLD, ChremasticsBlocks.COIN_PILE.get()));
    public static final DeferredHolder<Item, CoinItem> PLATINUM_COIN = ITEMS.register("platinum_coin", () -> new CoinItem(Currency.PLATINUM, ChremasticsBlocks.COIN_PILE.get()));
    public static final DeferredHolder<Item, CoinBagItem> COIN_BAG = ITEMS.register("coin_bag", CoinBagItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);

        if (ModList.get().isLoaded("curios")) {
            ChremasticsCurioItems.CURIO_ITEMS.register(eventBus);
        }
    }
}