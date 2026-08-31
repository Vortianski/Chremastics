package xox.labvorty.chremastics.mixins;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xox.labvorty.chremastics.data.configs.CommonConfig;
import xox.labvorty.chremastics.data.currency.CurrencyHandlers;
import xox.labvorty.chremastics.data.currency.VillagerTradeHandler;
import xox.labvorty.chremastics.data.utilities.CurioUtilities;
import xox.labvorty.chremastics.init.ChremasticsCurioItems;
import xox.labvorty.chremastics.init.ChremasticsDataComponents;
import xox.labvorty.chremastics.init.ChremasticsItems;
import xox.labvorty.chremastics.mixin_helpers.AbstractVillagerAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(Villager.class)
public abstract class VillagerMixin {
    @Shadow
    public abstract VillagerData getVillagerData();

    @Shadow
    public abstract int getPlayerReputation(Player player);

    @Inject(
            method = "updateTrades",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chremastics$replaceTrades(CallbackInfo ci) {
        Villager villager = (Villager) (Object) this;

        VillagerData villagerData = this.getVillagerData();

        Int2ObjectMap<VillagerTrades.ItemListing[]> trades;

        if (villager.level().enabledFeatures().contains(FeatureFlags.TRADE_REBALANCE)) {
            Int2ObjectMap<VillagerTrades.ItemListing[]> experimental =
                    VillagerTrades.EXPERIMENTAL_TRADES.get(villagerData.getProfession());

            trades = experimental != null
                    ? experimental
                    : VillagerTrades.TRADES.get(villagerData.getProfession());
        } else {
            trades = VillagerTrades.TRADES.get(villagerData.getProfession());
        }

        if (trades != null && !trades.isEmpty()) {
            VillagerTrades.ItemListing[] listings =
                    trades.get(villagerData.getLevel());

            if (listings != null) {
                MerchantOffers offers = villager.getOffers();

                if (CommonConfig.REPLACE_EMERALDS_WITH_COINS.get()) {
                    listings = VillagerTradeHandler.replaceListings(listings);
                }

                ((AbstractVillagerAccessor) villager)
                        .chremastics$addOffersFromItemListings(
                                offers,
                                listings,
                                2
                        );
            }
        }

        ci.cancel();
    }

    @Inject(method = "updateSpecialPrices", at = @At("TAIL"))
    private void chremastics$applyCustomDiscount(Player player, CallbackInfo ci) {
        Villager villager = (Villager) (Object) this;

        double discount = CommonConfig.TRADERS_INSIGNIA_DISCOUNT.get();
        if (discount <= 0 || !CurioUtilities.hasCurioItem(
                player,
                ChremasticsCurioItems.TRADERS_INSIGNIA.get()
        )) {
            return;
        }

        MerchantOffers offers = villager.getOffers();

        for (int i = 0; i < offers.size(); i++) {
            MerchantOffer offer = offers.get(i);

            MerchantOffer discountedOffer =
                    VillagerTradeHandler.applyDiscount(offer, discount);

            discountedOffer.addToSpecialPriceDiff(
                    -Mth.floor(
                            (float) getPlayerReputation(player)
                                    * discountedOffer.getPriceMultiplier()
                    )
            );

            offers.set(i, discountedOffer);
        }
    }
}
