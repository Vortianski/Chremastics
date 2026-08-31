package xox.labvorty.chremastics.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xox.labvorty.chremastics.Chremastics;

public class ChremasticsSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, Chremastics.MOD_ID);
    public static final DeferredHolder<SoundEvent, SoundEvent> COIN_STEP = SOUND_EVENTS.register(
            "coin_step",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("chremastics", "coin_step"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> COIN_BREAK = SOUND_EVENTS.register(
            "coin_break",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("chremastics", "coin_break"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> COIN_FALL = SOUND_EVENTS.register(
            "coin_fall",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("chremastics", "coin_fall"))
    );
}
