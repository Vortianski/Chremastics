package xox.labvorty.chremastics.mixin_helpers;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffers;

public interface AbstractVillagerAccessor {
    void chremastics$addOffersFromItemListings(MerchantOffers givenMerchantOffers, VillagerTrades.ItemListing[] newTrades, int maxNumbers);
}
