package xox.labvorty.chremastics.init;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xox.labvorty.chremastics.Chremastics;
import xox.labvorty.chremastics.items.curios.BentCoinItem;
import xox.labvorty.chremastics.items.curios.TradersInsigniaItem;

public class ChremasticsCurioItems {
    public static final DeferredRegister<Item> CURIO_ITEMS = DeferredRegister.createItems(Chremastics.MOD_ID);
    public static final DeferredHolder<Item, Item> BENT_COIN = CURIO_ITEMS.register("bent_coin", BentCoinItem::new);
    public static final DeferredHolder<Item, Item> TRADERS_INSIGNIA = CURIO_ITEMS.register("traders_insignia", TradersInsigniaItem::new);
}
