package xox.labvorty.chremastics.compat.waystones;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.Waystone;
import net.blay09.mods.waystones.api.WaystoneTeleportContext;
import net.blay09.mods.waystones.api.event.WaystoneTeleportEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import xox.labvorty.chremastics.data.configs.CommonConfig;
import xox.labvorty.chremastics.init.ChremasticsAttachments;

import java.util.Optional;

public final class WaystonesHandler {
    private WaystonesHandler() {}

    public static void register() {
        Balm.getEvents().onEvent(WaystoneTeleportEvent.Pre.class, WaystonesHandler::onPre);
        Balm.getEvents().onEvent(WaystoneTeleportEvent.Post.class, WaystonesHandler::onPost);
    }

    private static void onPre(WaystoneTeleportEvent.Pre event) {
        if (CommonConfig.USE_COINS_IN_WAYSTONES.get()) {
            WaystoneTeleportContext context = event.getContext();
            if (!(context.getEntity() instanceof Player player)) {
                return;
            }

            long price = calculatePrice(context);
            long balance = player.getData(ChremasticsAttachments.COIN_BALANCE.get());

            if (balance < price) {
                player.displayClientMessage(Component.translatable("chremastics.waystones.not_enough_coins", price, balance).withStyle(ChatFormatting.RED), false);
                event.setCanceled(true);
            }
        }
    }

    private static void onPost(WaystoneTeleportEvent.Post event) {
        if (CommonConfig.USE_COINS_IN_WAYSTONES.get()) {
            WaystoneTeleportContext context = event.getContext();
            if (!(context.getEntity() instanceof Player player)) {
                return;
            }

            long price = calculatePrice(context);
            long balance = player.getData(ChremasticsAttachments.COIN_BALANCE.get());
            long newBalance = Math.max(0L, balance - price);
            player.displayClientMessage(Component.translatable("chremastics.waystones.spent_coins", price).withStyle(ChatFormatting.YELLOW), false);
            player.setData(ChremasticsAttachments.COIN_BALANCE.get(), newBalance);
        }
    }

    private static long calculatePrice(WaystoneTeleportContext context) {
        Optional<Waystone> from = context.getFromWaystone();
        Waystone to = context.getTargetWaystone();

        if (from.isEmpty() || context.isDimensionalTeleport()) {
            return CommonConfig.WAYSTONES_COINS_FOR_DIMENSION.get();
        }

        BlockPos fromPos = from.get().getPos();
        BlockPos toPos = to.getPos();
        double distance = Math.sqrt(fromPos.distSqr(toPos));
        return Math.round(distance * CommonConfig.WAYSTONES_COINS_PER_BLOCK.get());
    }
}