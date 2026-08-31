package xox.labvorty.chremastics.init;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import xox.labvorty.chremastics.Chremastics;
import xox.labvorty.chremastics.data.loot_modifiers.ArtifactsLootModifier;
import xox.labvorty.chremastics.data.loot_modifiers.CoinBagLootModifier;

import java.util.function.Supplier;

public class ChremasticsLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(
                    NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS,
                    Chremastics.MOD_ID
            );

    public static final Supplier<MapCodec<CoinBagLootModifier>> COIN_PURSE =
            LOOT_MODIFIERS.register(
                    "coin_bag",
                    () -> CoinBagLootModifier.CODEC
            );

    public static final Supplier<MapCodec<ArtifactsLootModifier>> ARTIFACTS = LOOT_MODIFIERS.register(
            "artifacts",
            () -> ArtifactsLootModifier.CODEC
    );
}
