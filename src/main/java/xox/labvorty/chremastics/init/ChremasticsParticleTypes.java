package xox.labvorty.chremastics.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import xox.labvorty.chremastics.particles.options.CoinSparkleOptions;

public class ChremasticsParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, "chremastics");

    public static final DeferredHolder<ParticleType<?>, ParticleType<CoinSparkleOptions>> COIN_SPARKLE =
            PARTICLE_TYPES.register("coin_sparkle", () -> new ParticleType<>(false) {
                @Override
                public @NotNull MapCodec<CoinSparkleOptions> codec() {
                    return CoinSparkleOptions.CODEC;
                }

                @Override
                public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, CoinSparkleOptions> streamCodec() {
                    return CoinSparkleOptions.STREAM_CODEC;
                }
            });
}