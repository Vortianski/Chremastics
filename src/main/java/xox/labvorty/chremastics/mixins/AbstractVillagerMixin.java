package xox.labvorty.chremastics.mixins;

import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xox.labvorty.chremastics.mixin_helpers.AbstractVillagerAccessor;

@Mixin(AbstractVillager.class)
public abstract class AbstractVillagerMixin implements AbstractVillagerAccessor {
    @Shadow
    protected abstract void addOffersFromItemListings(MerchantOffers givenMerchantOffers, VillagerTrades.ItemListing[] newTrades, int maxNumbers);

    @Override
    public void chremastics$addOffersFromItemListings(MerchantOffers givenMerchantOffers, VillagerTrades.ItemListing[] newTrades, int maxNumbers) {
        this.addOffersFromItemListings(givenMerchantOffers, newTrades, maxNumbers);
    }
}
