package xox.labvorty.chremastics.compat.jade;

import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import xox.labvorty.chremastics.blocks.CoinPileBlock;

@WailaPlugin
public class ChremasticsJadePlugin implements IWailaPlugin {
    public static final ResourceLocation COIN_PILE = ResourceLocation.fromNamespaceAndPath(
            "chremastics",
            "coin_pile"
    );

    @Override
    public void register(IWailaCommonRegistration registration) {
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(
                CoinPileComponentProvider.INSTANCE,
                CoinPileBlock.class
        );
    }
}
