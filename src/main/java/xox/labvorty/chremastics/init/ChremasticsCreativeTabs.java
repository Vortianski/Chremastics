package xox.labvorty.chremastics.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xox.labvorty.chremastics.Chremastics;
import xox.labvorty.chremastics.data.utilities.CreativeModeTabProvider;
import xox.labvorty.vortylib.data.creative_tab.ExpandableCreativeTab;

public class ChremasticsCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Chremastics.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CHREMASTICS_ITEMS = CREATIVE_MODE_TABS.register(
            "items",
            () -> ExpandableCreativeTab.builder()
                    .addGroup("coin_bags", ChremasticsItems.COPPER_COIN.get().getDefaultInstance(), CreativeModeTabProvider.getCoins())
                    .addGroup("coins", ChremasticsItems.COIN_BAG.get().getDefaultInstance(), CreativeModeTabProvider.getCoinBags())
                    .icon(() -> ChremasticsItems.PLATINUM_COIN.get().getDefaultInstance())
                    .title(Component.literal("Chremastics"))
                    .displayItems((parameters, output) -> {
                        if (ModList.get().isLoaded("curios")) {
                            output.accept(ChremasticsCurioItems.BENT_COIN.get());
                            output.accept(ChremasticsCurioItems.TRADERS_INSIGNIA.get());
                        }
                    })
                    .build()
    );
}
