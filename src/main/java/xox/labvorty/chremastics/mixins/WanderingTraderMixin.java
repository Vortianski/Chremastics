package xox.labvorty.chremastics.mixins;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.entity.npc.WanderingTrader;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xox.labvorty.chremastics.data.configs.CommonConfig;
import xox.labvorty.chremastics.data.currency.VillagerTradeHandler;
import xox.labvorty.chremastics.data.utilities.CurioUtilities;
import xox.labvorty.chremastics.init.ChremasticsCurioItems;
import xox.labvorty.chremastics.mixin_helpers.AbstractVillagerAccessor;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin {
    @Shadow
    protected abstract void experimentalUpdateTrades();

    @Inject(
            method = "updateTrades",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chremastics$modifyTrades(CallbackInfo ci) {
        if (CommonConfig.REPLACE_EMERALDS_WITH_COINS.get()) {
            WanderingTrader wanderingTrader = (WanderingTrader)(Object)this;

            if (wanderingTrader.level().enabledFeatures().contains(FeatureFlags.TRADE_REBALANCE)) {
                this.experimentalUpdateTrades();
            } else {
                VillagerTrades.ItemListing[] avillagertrades$itemlisting = (VillagerTrades.ItemListing[])VillagerTrades.WANDERING_TRADER_TRADES.get(1);
                VillagerTrades.ItemListing[] avillagertrades$itemlisting1 = (VillagerTrades.ItemListing[])VillagerTrades.WANDERING_TRADER_TRADES.get(2);
                if (avillagertrades$itemlisting != null && avillagertrades$itemlisting1 != null) {
                    avillagertrades$itemlisting = VillagerTradeHandler.replaceListings(avillagertrades$itemlisting);
                    avillagertrades$itemlisting1 = VillagerTradeHandler.replaceListings(avillagertrades$itemlisting1);
                    MerchantOffers merchantoffers = wanderingTrader.getOffers();
                    ((AbstractVillagerAccessor)wanderingTrader).chremastics$addOffersFromItemListings(merchantoffers, avillagertrades$itemlisting, 5);
                    int i = wanderingTrader.getRandom().nextInt(avillagertrades$itemlisting1.length);
                    VillagerTrades.ItemListing villagertrades$itemlisting = avillagertrades$itemlisting1[i];
                    MerchantOffer merchantoffer = villagertrades$itemlisting.getOffer(wanderingTrader, wanderingTrader.getRandom());
                    if (merchantoffer != null) {
                        merchantoffers.add(merchantoffer);
                    }
                }
            }

            ci.cancel();
        }
    }

    @Inject(
            method = "experimentalUpdateTrades",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chremastics$modifyExperimentalTrades(CallbackInfo ci) {
        if (CommonConfig.REPLACE_EMERALDS_WITH_COINS.get()) {
            WanderingTrader wanderingTrader = (WanderingTrader)(Object)this;
            MerchantOffers merchantoffers = wanderingTrader.getOffers();

            for(Pair<VillagerTrades.ItemListing[], Integer> pair : VillagerTrades.EXPERIMENTAL_WANDERING_TRADER_TRADES) {
                VillagerTrades.ItemListing[] avillagertrades$itemlisting = (VillagerTrades.ItemListing[])pair.getLeft();
                avillagertrades$itemlisting = VillagerTradeHandler.replaceListings(avillagertrades$itemlisting);
                ((AbstractVillagerAccessor)wanderingTrader).chremastics$addOffersFromItemListings(merchantoffers, avillagertrades$itemlisting, (Integer)pair.getRight());
            }

            ci.cancel();
        }
    }
}
