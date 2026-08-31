package xox.labvorty.chremastics.mixins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oshi.util.tuples.Pair;
import xox.labvorty.chremastics.data.configs.CommonConfig;
import xox.labvorty.chremastics.data.currency.Currency;
import xox.labvorty.chremastics.data.currency.CurrencyHandlers;
import xox.labvorty.chremastics.data.utilities.CurioUtilities;
import xox.labvorty.chremastics.init.ChremasticsCurioItems;
import xox.labvorty.chremastics.init.ChremasticsItems;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    protected abstract boolean shouldDropLoot();

    @Inject(
            method = "dropAllDeathLoot",
            at = @At("TAIL")
    )
    private void chremastics$dropCoins(ServerLevel serverLevel, DamageSource damageSource, CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        Entity entity = damageSource.getEntity();

        if (livingEntity instanceof Player || livingEntity instanceof ServerPlayer || CurrencyHandlers.isRestrictedCoinEntity(livingEntity) || !(shouldDropLoot() && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))) {
            return;
        }

        int value = chremastics$determineValue(livingEntity, entity);

        if (CommonConfig.USE_COIN_ENTITY_WHITELIST.get()) {
            if (CurrencyHandlers.isCoinEntity(livingEntity)) {
                chremastics$dropCoins(livingEntity, value, entity);
            } else {
                return;
            }
        }

        if (livingEntity instanceof NeutralMob || livingEntity instanceof Enemy || livingEntity instanceof Villager) {
            chremastics$dropCoins(livingEntity, value, entity);
        }
    }

    @Unique
    private int chremastics$determineValue(LivingEntity livingEntity, Entity damageSourceEntity) {
        int dropType = CommonConfig.COIN_DROP_TYPE.get();

        return switch (dropType) {
            case 0 -> {
                yield (int)Math.ceil(livingEntity.getMaxHealth());
            }
            case 1 -> {
                yield CurrencyHandlers.buildValueFromRandom(livingEntity.getRandom());
            }
            default -> 0;
        };
    }

    @Unique
    private void chremastics$dropCoins(
            LivingEntity livingEntity,
            int value,
            Entity entity
    ) {
        List<ItemStack> itemStacks = CurrencyHandlers.getStacksFromValue(value);

        if (entity instanceof LivingEntity living) {
            boolean hasBentCoin = CurioUtilities.hasCurioItem(
                    living,
                    ChremasticsCurioItems.BENT_COIN.get()
            );

            if (hasBentCoin) {
                if (livingEntity.getRandom().nextDouble() < CommonConfig.BENT_COIN_CHANCE.get()) {
                    for (ItemStack itemStack : itemStacks) {
                        livingEntity.spawnAtLocation(itemStack.copy());
                    }

                    for (ItemStack itemStack : itemStacks) {
                        livingEntity.spawnAtLocation(itemStack.copy());
                    }
                } else {
                    livingEntity.spawnAtLocation(
                            new ItemStack(ChremasticsItems.COPPER_COIN.get())
                    );
                }
            } else {
                for (ItemStack itemStack : itemStacks) {
                    livingEntity.spawnAtLocation(itemStack.copy());
                }
            }
        } else {
            for (ItemStack itemStack : itemStacks) {
                livingEntity.spawnAtLocation(itemStack.copy());
            }
        }
    }
}
