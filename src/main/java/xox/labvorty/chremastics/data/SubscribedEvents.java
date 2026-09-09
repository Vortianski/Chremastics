package xox.labvorty.chremastics.data;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;
import xox.labvorty.chremastics.data.configs.CommonConfig;
import xox.labvorty.chremastics.data.currency.CurrencyHandlers;
import xox.labvorty.chremastics.data.network.ConfigSyncPacket;
import xox.labvorty.chremastics.data.utilities.ClientData;
import xox.labvorty.chremastics.init.ChremasticsAttachments;
import xox.labvorty.chremastics.init.ChremasticsCommand;
import xox.labvorty.chremastics.init.ChremasticsDataComponents;
import xox.labvorty.chremastics.init.ChremasticsItems;

import java.util.List;

@EventBusSubscriber
public class SubscribedEvents {
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();

        if (livingEntity instanceof Player player
                && CommonConfig.DROP_COINS_ON_DEATH.get()) {

            double lossPercentage = CommonConfig.COINS_LOST_ON_DEATH.get();

            long value = player.getData(
                    ChremasticsAttachments.COIN_BALANCE.get()
            );

            long lostValue = (long) Math.ceil(value * lossPercentage);

            player.setData(
                    ChremasticsAttachments.COIN_BALANCE.get(),
                    Math.max(0L, value - lostValue)
            );

            if (lostValue <= 0)
                return;

            Pair<List<ItemStack>, Long> result =
                    CurrencyHandlers.getStacksFromValueLimited(
                            lostValue,
                            CurrencyHandlers.MAX_PHYSICAL_STACKS
                    );

            for (ItemStack stack : result.getKey()) {
                player.spawnAtLocation(stack);
            }

            long remainingValue = result.getValue();

            if (remainingValue > 0) {
                ItemStack bag = ChremasticsItems.COIN_BAG.get().getDefaultInstance();

                bag.set(
                        ChremasticsDataComponents.VALUE,
                        remainingValue
                );

                ItemEntity itemEntity = player.spawnAtLocation(bag);
                if (itemEntity != null) {
                    itemEntity.setUnlimitedLifetime();
                }
            }
        }
    }

    @SubscribeEvent
    public static void configLoad(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() != CommonConfig.SPEC) return;

        if (FMLEnvironment.dist == Dist.CLIENT) {
            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.isSingleplayer()) {
                ClientData.setBentCoinChance(CommonConfig.BENT_COIN_CHANCE.get());
                ClientData.setTradersInsigniaDiscount(CommonConfig.TRADERS_INSIGNIA_DISCOUNT.get());
            }
        } else {
            syncToAllPlayers();
        }

        if (event.getConfig().getSpec() == CommonConfig.SPEC && !(FMLEnvironment.dist == Dist.CLIENT)) {
            syncToAllPlayers();
        }
    }

    @SubscribeEvent
    public static void login(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(
                    serverPlayer,
                    new ConfigSyncPacket(
                            CommonConfig.BENT_COIN_CHANCE.get(),
                            CommonConfig.TRADERS_INSIGNIA_DISCOUNT.get()
                    )
            );
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ChremasticsCommand.register(event.getDispatcher());
    }

    public static void syncToAllPlayers() {
        PacketDistributor.sendToAllPlayers(new ConfigSyncPacket(CommonConfig.BENT_COIN_CHANCE.get(), CommonConfig.TRADERS_INSIGNIA_DISCOUNT.get()));
    }
}
