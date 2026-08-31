package xox.labvorty.chremastics.mixins;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xox.labvorty.chremastics.data.configs.ClientConfig;
import xox.labvorty.chremastics.data.currency.Currency;
import xox.labvorty.chremastics.items.CoinItem;
import xox.labvorty.chremastics.particles.options.CoinSparkleOptions;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @OnlyIn(Dist.CLIENT)
    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void chremastics$animateCoin(CallbackInfo ci) {
        ItemEntity itemEntity = (ItemEntity) (Object) this;

        if (!(itemEntity.level() instanceof ClientLevel level)) {
            return;
        }

        if (!(itemEntity.getItem().getItem() instanceof CoinItem coinItem)) {
            return;
        }

        RandomSource randomSource = itemEntity.getRandom();

        if (!ClientConfig.SPECIAL_EFFECTS.get()) return;
        if (ClientConfig.PARTICLE_CHANCE.get() < randomSource.nextDouble()) return;

        Currency currency = coinItem.getCurrency();
        CoinSparkleOptions options = CoinSparkleOptions.fromColor(currency.getSparkleColor());

        double x = itemEntity.getX() + (randomSource.nextDouble() - 0.5D) * 0.4D;
        double y = itemEntity.getY() + 0.15D;
        double z = itemEntity.getZ() + (randomSource.nextDouble() - 0.5D) * 0.4D;

        double xd = 0.0D;
        double yd = 0.01D + randomSource.nextDouble() * 0.015D;
        double zd = 0.0D;

        level.addParticle(
                options,
                x, y, z,
                xd, yd, zd
        );
    }
}