package xox.labvorty.chremastics.data.currency;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import xox.labvorty.chremastics.data.configs.CommonConfig;
import xox.labvorty.chremastics.init.ChremasticsItems;
import xox.labvorty.chremastics.items.CoinItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyHandlers {
    private static final int MAX_STACK_SIZE = 99;

    public static CoinItem getCoinFromCurrency(Currency currency) {
        return switch (currency) {
            case COPPER -> ChremasticsItems.COPPER_COIN.get();
            case SILVER -> ChremasticsItems.SILVER_COIN.get();
            case GOLD -> ChremasticsItems.GOLD_COIN.get();
            case PLATINUM -> ChremasticsItems.PLATINUM_COIN.get();
        };
    }

    public static List<ItemStack> getStacksFromValue(int value) {
        List<ItemStack> stacks = new ArrayList<>();

        if (value <= 0)
            return stacks;

        Currency[] currencies = Currency.values();

        for (int i = currencies.length - 1; i >= 0; i--) {
            Currency currency = currencies[i];

            int count = value / currency.getValue();

            if (count <= 0)
                continue;

            value %= currency.getValue();

            while (count > 0) {
                int stackSize = Math.min(count, MAX_STACK_SIZE);

                ItemStack stack = getCoinFromCurrency(currency).getDefaultInstance();
                stack.setCount(stackSize);

                stacks.add(stack);

                count -= stackSize;
            }
        }

        return stacks;
    }

    public static ItemStack combineCoins(List<ItemStack> stacks) {
        if (stacks.isEmpty())
            return ItemStack.EMPTY;

        int totalValue = 0;

        for (ItemStack stack : stacks) {
            if (stack.getItem() instanceof CoinItem coinItem) {
                totalValue += coinItem.getCurrency().getValue() * stack.getCount();
            }
        }

        if (totalValue <= 0)
            return ItemStack.EMPTY;

        Currency bestCurrency = Currency.COPPER;
        int bestAmount = totalValue;

        for (Currency currency : Currency.values()) {
            int amount = totalValue / currency.getValue();

            if (amount <= 0)
                continue;

            int remainder = totalValue % currency.getValue();

            // Round to whichever amount is closer.
            if (remainder >= currency.getValue() / 2) {
                amount++;
            }

            if (amount <= MAX_STACK_SIZE) {
                bestCurrency = currency;
                bestAmount = amount;
            }
        }

        ItemStack result = getCoinFromCurrency(bestCurrency).getDefaultInstance();
        result.setCount(bestAmount);

        return result;
    }

    public static Pair<ItemStack, Optional<ItemStack>> combineCoinsTwo(
            List<ItemStack> stacks
    ) {
        if (stacks.isEmpty())
            return Pair.of(ItemStack.EMPTY, Optional.empty());

        int totalValue = 0;

        for (ItemStack stack : stacks) {
            if (stack.getItem() instanceof CoinItem coinItem) {
                totalValue += coinItem.getCurrency().getValue() * stack.getCount();
            }
        }

        if (totalValue <= 0)
            return Pair.of(ItemStack.EMPTY, Optional.empty());

        List<ItemStack> result = new ArrayList<>();

        Currency[] currencies = Currency.values();

        for (int i = currencies.length - 1; i >= 0 && result.size() < 2; i--) {
            Currency currency = currencies[i];

            int count = totalValue / currency.getValue();

            if (count <= 0)
                continue;

            totalValue %= currency.getValue();

            while (count > 0 && result.size() < 2) {
                int stackSize = Math.min(count, MAX_STACK_SIZE);

                ItemStack stack = getCoinFromCurrency(currency).getDefaultInstance();
                stack.setCount(stackSize);

                result.add(stack);

                count -= stackSize;
            }
        }

        // Remaining value can be rounded into the second stack.
        if (totalValue > 0 && result.size() < 2) {
            Currency currency = Currency.COPPER;

            for (int i = currencies.length - 1; i >= 0; i--) {
                if (currencies[i].getValue() <= totalValue) {
                    currency = currencies[i];
                    break;
                }
            }

            int amount = totalValue / currency.getValue();

            if (totalValue % currency.getValue() >= currency.getValue() / 2) {
                amount++;
            }

            if (amount > 0) {
                ItemStack stack = getCoinFromCurrency(currency).getDefaultInstance();
                stack.setCount(Math.min(amount, MAX_STACK_SIZE));
                result.add(stack);
            }
        }

        ItemStack first = result.getFirst();
        Optional<ItemStack> second = result.size() > 1
                ? Optional.of(result.get(1))
                : Optional.empty();

        return Pair.of(first, second);
    }

    public static List<ItemStack> getStacksFromCurrency(Currency currency, int amount) {
        return getStacksFromValue(currency.getValue() * amount);
    }

    public static List<ItemStack> getChange(Currency currency, int value) {
        int remaining = currency.getValue() - value;

        if (remaining < 0)
            return List.of();

        return getStacksFromValue(remaining);
    }

    public static List<ItemStack> exchange(Currency from, Currency to) {
        if (to.getValue() >= from.getValue())
            return List.of();

        if (from.getValue() % to.getValue() != 0)
            return List.of();

        return getStacksFromCurrency(to, from.getValue() / to.getValue());
    }

    public static int buildValueFromRandomForBag(RandomSource randomSource) {
        int value = buildValueFromRandom(randomSource);

        if (value == 0) {
            value = randomSource.nextIntBetweenInclusive(
                    CommonConfig.COPPER_COIN_MIN_AMOUNT.get(),
                    CommonConfig.COPPER_COIN_MAX_AMOUNT.get()
            ) * Currency.COPPER.getValue();
        }

        return value;
    }

    public static int buildValueFromRandom(RandomSource randomSource) {
        int finalValue = 0;
        finalValue += (CommonConfig.COPPER_COIN_CHANCE.get() > randomSource.nextDouble())
                ? randomSource.nextIntBetweenInclusive(
                CommonConfig.COPPER_COIN_MIN_AMOUNT.get(),
                CommonConfig.COPPER_COIN_MAX_AMOUNT.get()
        ) * Currency.COPPER.getValue() : 0;
        finalValue += (CommonConfig.SILVER_COIN_CHANCE.get() > randomSource.nextDouble())
                ? randomSource.nextIntBetweenInclusive(
                CommonConfig.SILVER_COIN_MIN_AMOUNT.get(),
                CommonConfig.SILVER_COIN_MAX_AMOUNT.get()
        ) * Currency.SILVER.getValue() : 0;
        finalValue += (CommonConfig.GOLD_COIN_CHANCE.get() > randomSource.nextDouble())
                ? randomSource.nextIntBetweenInclusive(
                CommonConfig.GOLD_COIN_MIN_AMOUNT.get(),
                CommonConfig.GOLD_COIN_MAX_AMOUNT.get()
        ) * Currency.GOLD.getValue() : 0;
        finalValue += (CommonConfig.PLATINUM_COIN_CHANCE.get() > randomSource.nextDouble())
                ? randomSource.nextIntBetweenInclusive(
                CommonConfig.PLATINUM_COIN_MIN_AMOUNT.get(),
                CommonConfig.PLATINUM_COIN_MAX_AMOUNT.get()
        ) * Currency.PLATINUM.getValue() : 0;

        return finalValue;
    }

    public static boolean isCoinEntity(LivingEntity livingEntity) {
        return CommonConfig.COIN_ENTITIES.get().contains(BuiltInRegistries.ENTITY_TYPE.getKey(livingEntity.getType()).toString());
    }

    public static boolean isRestrictedCoinEntity(LivingEntity livingEntity) {
        return CommonConfig.COIN_ENTITIES.get().contains(BuiltInRegistries.ENTITY_TYPE.getKey(livingEntity.getType()).toString());
    }
}
