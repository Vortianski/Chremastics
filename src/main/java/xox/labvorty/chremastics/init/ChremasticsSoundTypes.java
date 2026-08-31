package xox.labvorty.chremastics.init;

import net.minecraft.world.level.block.SoundType;

@SuppressWarnings("deprecation")
public class ChremasticsSoundTypes {
    public static final SoundType COIN = new SoundType(
            1,
            1,
            ChremasticsSoundEvents.COIN_BREAK.get(), //break
            ChremasticsSoundEvents.COIN_STEP.get(), //step
            ChremasticsSoundEvents.COIN_STEP.get(), //place
            ChremasticsSoundEvents.COIN_STEP.get(), //hit
            ChremasticsSoundEvents.COIN_FALL.get() //fall
    );
}
