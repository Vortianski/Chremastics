package xox.labvorty.chremastics.data.currency;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.apache.commons.lang3.tuple.Pair;
import xox.labvorty.chremastics.data.configs.CommonConfig;
import xox.labvorty.chremastics.init.ChremasticsDataComponents;
import xox.labvorty.chremastics.init.ChremasticsItems;
import xox.labvorty.chremastics.items.CoinItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VillagerTradeHandler {
    public static VillagerTrades.ItemListing[] replaceListings(VillagerTrades.ItemListing[] itemListings) {
        List<VillagerTrades.ItemListing> modified = new ArrayList<>();

        for (VillagerTrades.ItemListing listing : itemListings) {
            modified.add((trader, random) -> {
                MerchantOffer offer = listing.getOffer(trader, random);

                if (offer == null) {
                    return null;
                }

                return replaceEmeralds(offer);
            });
        }

        return modified.toArray(new VillagerTrades.ItemListing[0]);
    }

    public static MerchantOffer replaceEmeralds(MerchantOffer merchantOffer) {
        ItemCost itemCost = merchantOffer.getItemCostA();
        Optional<ItemCost> itemCostOptional = merchantOffer.getItemCostB();
        ItemStack itemStack = merchantOffer.getResult();
        boolean modified = false;

        Pair<ItemStack, Optional<ItemStack>> optionalCombined = null;
        ItemStack combined = null;

        if (itemCost.itemStack().is(Items.EMERALD)) {
            int value = itemCost.itemStack().getCount() * CommonConfig.EMERALD_VALUE.get();
            List<ItemStack> itemStacks = CurrencyHandlers.getStacksFromValue(value);
            optionalCombined = CurrencyHandlers.combineCoinsTwo(itemStacks);
            combined = CurrencyHandlers.combineCoins(itemStacks);
            modified = true;
        }

        if (itemCostOptional.isPresent()) {
            ItemCost costB = itemCostOptional.get();
            if (costB.itemStack().is(Items.EMERALD)) {
                int value = costB.itemStack().getCount() * CommonConfig.EMERALD_VALUE.get();
                List<ItemStack> itemStacks = CurrencyHandlers.getStacksFromValue(value);
                ItemStack stack = CurrencyHandlers.combineCoins(itemStacks);
                itemCostOptional = Optional.of(
                        new ItemCost(
                                stack.getItem(),
                                stack.getCount()
                        )
                );

                modified = true;
            }

            if (combined != null) {
                itemCost = new ItemCost(
                        combined.getItem(),
                        combined.getCount()
                );
            }
        } else {
            if (combined != null) {
                itemCost = new ItemCost(
                        optionalCombined.getKey().getItem(),
                        optionalCombined.getKey().getCount()
                );
                if (optionalCombined.getValue().isPresent()) {
                    itemCostOptional = Optional.of(
                            new ItemCost(
                                    optionalCombined.getValue().get().getItem(),
                                    optionalCombined.getValue().get().getCount()
                            )
                    );
                }
            }
        }

        if (itemStack.is(Items.EMERALD)) {
            boolean useBag = CommonConfig.USE_BAGS_FOR_TRADES.get();
            int value = itemStack.getCount() * CommonConfig.EMERALD_VALUE.get();

            if (useBag) {
                ItemStack bagStack = ChremasticsItems.COIN_BAG.get().getDefaultInstance();
                bagStack.set(ChremasticsDataComponents.VALUE.get(), value);
                itemStack = bagStack;
            } else {
                itemStack = CurrencyHandlers.combineCoins(CurrencyHandlers.getStacksFromValue(value));
            }

            modified = true;
        }

        if (!modified) {
            return merchantOffer;
        }

        return new MerchantOffer(
                itemCost,
                itemCostOptional,
                itemStack,
                merchantOffer.getUses(),
                merchantOffer.getMaxUses(),
                merchantOffer.getXp(),
                merchantOffer.getPriceMultiplier(),
                merchantOffer.getDemand()
        );
    }

    public static MerchantOffer applyDiscount(MerchantOffer merchantOffer, double discount) {
        if (discount <= 0) {
            return merchantOffer;
        }

        ItemCost costA = merchantOffer.getItemCostA();
        Optional<ItemCost> costB = merchantOffer.getItemCostB();

        int totalValue = getCoinValue(costA);
        if (costB.isPresent()) {
            totalValue += getCoinValue(costB.get());
        }

        if (totalValue <= 0) {
            return merchantOffer;
        }

        int discountedValue = Math.max(1, (int) Math.floor(totalValue * (1.0 - discount)));

        List<ItemStack> stacks = CurrencyHandlers.getStacksFromValue(discountedValue);
        Pair<ItemStack, Optional<ItemStack>> combined = CurrencyHandlers.combineCoinsTwo(stacks);

        ItemStack newA = combined.getKey();
        if (newA.isEmpty()) {
            return merchantOffer;
        }

        ItemCost newCostA = new ItemCost(newA.getItem(), newA.getCount());

        Optional<ItemCost> newCostB = combined.getValue().map(stack ->
                new ItemCost(stack.getItem(), stack.getCount())
        );

        return new MerchantOffer(
                newCostA,
                newCostB,
                merchantOffer.getResult(),
                merchantOffer.getUses(),
                merchantOffer.getMaxUses(),
                merchantOffer.getXp(),
                merchantOffer.getPriceMultiplier(),
                merchantOffer.getDemand()
        );
    }

    private static int getCoinValue(ItemCost cost) {
        ItemStack stack = cost.itemStack();

        if (stack.getItem() instanceof CoinItem coinItem) {
            return coinItem.getCurrency().getValue() * stack.getCount();
        }

        return 0;
    }
}
