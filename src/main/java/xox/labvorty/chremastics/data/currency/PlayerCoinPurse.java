package xox.labvorty.chremastics.data.currency;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import xox.labvorty.chremastics.init.ChremasticsAttachments;

public class PlayerCoinPurse {
    public static void add(ServerPlayer serverPlayer, long amount) {
        if (amount <= 0) return;

        long balance = getBalance(serverPlayer);
        serverPlayer.setData(ChremasticsAttachments.COIN_BALANCE, balance + amount);
    }

    public static long getBalance(Player player) {
        return player.getData(ChremasticsAttachments.COIN_BALANCE);
    }

    public static void addBalance(ServerPlayer player, long amount) {
        if (amount <= 0) return;
        player.setData(ChremasticsAttachments.COIN_BALANCE, getBalance(player) + amount);
    }

    public static void setBalance(ServerPlayer player, long amount) {
        player.setData(
                ChremasticsAttachments.COIN_BALANCE,
                Math.max(0L, amount)
        );
    }

    public static boolean tryRemoveBalance(ServerPlayer player, long amount) {
        if (amount <= 0) return false;
        long current = getBalance(player);
        if (current < amount) return false;
        player.setData(ChremasticsAttachments.COIN_BALANCE, current - amount);

        return true;
    }
}