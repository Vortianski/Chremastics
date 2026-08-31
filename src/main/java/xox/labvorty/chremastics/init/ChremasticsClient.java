package xox.labvorty.chremastics.init;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import xox.labvorty.chremastics.data.components.CoinBagTooltipComponent;
import xox.labvorty.chremastics.data.components.client.CoinBagClientTooltipComponent;
import xox.labvorty.chremastics.data.currency.CurrencyHandlers;
import xox.labvorty.chremastics.particles.CoinSparkleParticle;

@EventBusSubscriber(value = Dist.CLIENT)
public class ChremasticsClient {
    @SubscribeEvent
    public static void onClient(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    ChremasticsItems.COPPER_COIN.get(),
                    ResourceLocation.parse("chremastics:cvalue"),
                    (itemStack, clientLevel, livingEntity, i) -> ((float)itemStack.getCount() / 100F)
            );

            ItemProperties.register(
                    ChremasticsItems.SILVER_COIN.get(),
                    ResourceLocation.parse("chremastics:cvalue"),
                    (itemStack, clientLevel, livingEntity, i) -> ((float)itemStack.getCount() / 100F)
            );

            ItemProperties.register(
                    ChremasticsItems.GOLD_COIN.get(),
                    ResourceLocation.parse("chremastics:cvalue"),
                    (itemStack, clientLevel, livingEntity, i) -> ((float)itemStack.getCount() / 100F)
            );

            ItemProperties.register(
                    ChremasticsItems.PLATINUM_COIN.get(),
                    ResourceLocation.parse("chremastics:cvalue"),
                    (itemStack, clientLevel, livingEntity, i) -> ((float)itemStack.getCount() / 100F)
            );

            ItemProperties.register(
                    ChremasticsItems.COIN_BAG.get(),
                    ResourceLocation.parse("chremastics:cvalue"),
                    ((itemStack, clientLevel, livingEntity, i) -> {
                        int value = itemStack.getOrDefault(
                                ChremasticsDataComponents.VALUE,
                                0
                        );

                        int coinCount = 0;

                        for (ItemStack stack : CurrencyHandlers.getStacksFromValue(value)) {
                            coinCount += stack.getCount();
                        }

                        if (coinCount >= 50) return 1.0F;
                        if (coinCount >= 20) return 0.5F;

                        return 0.0F;
                    })
            );
        });
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ChremasticsParticleTypes.COIN_SPARKLE.get(), CoinSparkleParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(CoinBagTooltipComponent.class, component -> new CoinBagClientTooltipComponent(component.value()));
    }
}
