package xox.labvorty.chremastics.data.loot_modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import xox.labvorty.chremastics.data.configs.CommonConfig;
import xox.labvorty.chremastics.data.currency.CurrencyHandlers;
import xox.labvorty.chremastics.init.ChremasticsCurioItems;
import xox.labvorty.chremastics.init.ChremasticsDataComponents;
import xox.labvorty.chremastics.init.ChremasticsItems;

public class ArtifactsLootModifier extends LootModifier {
    public static final MapCodec<ArtifactsLootModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> LootModifier.codecStart(instance).apply(instance, ArtifactsLootModifier::new));

    public ArtifactsLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(
            @NotNull ObjectArrayList<ItemStack> generatedLoot,
            @NotNull LootContext lootContext
    ) {
        if (!isAllowed(lootContext.getQueriedLootTableId(), lootContext.getRandom())) {
            return generatedLoot;
        }

        ItemStack itemStack = lootContext.getRandom().nextBoolean() ? ChremasticsCurioItems.BENT_COIN.get().getDefaultInstance() : ChremasticsCurioItems.TRADERS_INSIGNIA.get().getDefaultInstance();
        generatedLoot.add(itemStack);

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    private static boolean isAllowed(ResourceLocation lootTable, RandomSource randomSource) {
        if (randomSource.nextDouble() > CommonConfig.ARTIFACTS_SPAWN_CHANCE.get()) {
            return false;
        }

        return true;
    }
}