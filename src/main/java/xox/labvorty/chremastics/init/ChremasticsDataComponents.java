package xox.labvorty.chremastics.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;
import xox.labvorty.chremastics.Chremastics;

import java.util.function.Supplier;

public class ChremasticsDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Chremastics.MOD_ID);
    public static final Supplier<DataComponentType<Integer>> VALUE = DATA_COMPONENTS.registerComponentType(
            "value",
            builder -> builder
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
    );
}
