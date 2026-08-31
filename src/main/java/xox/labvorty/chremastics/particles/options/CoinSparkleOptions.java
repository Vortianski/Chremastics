package xox.labvorty.chremastics.particles.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import xox.labvorty.chremastics.init.ChremasticsParticleTypes;

public class CoinSparkleOptions implements ParticleOptions {
    public static final MapCodec<CoinSparkleOptions> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(o -> o.red),
                    Codec.FLOAT.fieldOf("g").forGetter(o -> o.green),
                    Codec.FLOAT.fieldOf("b").forGetter(o -> o.blue)
            ).apply(instance, CoinSparkleOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CoinSparkleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, o -> o.red,
            ByteBufCodecs.FLOAT, o -> o.green,
            ByteBufCodecs.FLOAT, o -> o.blue,
            CoinSparkleOptions::new
    );

    public final float red;
    public final float green;
    public final float blue;

    public CoinSparkleOptions(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public static CoinSparkleOptions fromColor(int argbColor) {
        float r = ((argbColor >> 16) & 0xFF) / 255F;
        float g = ((argbColor >> 8) & 0xFF) / 255F;
        float b = (argbColor & 0xFF) / 255F;
        return new CoinSparkleOptions(r, g, b);
    }

    @Override
    public ParticleType<?> getType() {
        return ChremasticsParticleTypes.COIN_SPARKLE.get();
    }
}