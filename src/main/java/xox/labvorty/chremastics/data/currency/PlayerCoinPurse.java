package xox.labvorty.chremastics.data.currency;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import xox.labvorty.chremastics.init.ChremasticsAttachments;

public class PlayerCoinPurse {
    public static void add(ServerPlayer serverPlayer, int amount) {
        if (amount <= 0) return;

        int balance = getBalance(serverPlayer);
        serverPlayer.setData(ChremasticsAttachments.COIN_BALANCE, balance + amount);
    }

    public static int getBalance(Player player) {
        return player.getData(ChremasticsAttachments.COIN_BALANCE);
    }

    public static void addBalance(ServerPlayer player, int amount) {
        if (amount <= 0) return;
        player.setData(ChremasticsAttachments.COIN_BALANCE, getBalance(player) + amount);
    }

    public static boolean tryRemoveBalance(ServerPlayer player, int amount) {
        if (amount <= 0) return false;
        int current = getBalance(player);
        if (current < amount) return false;
        player.setData(ChremasticsAttachments.COIN_BALANCE, current - amount);

        return true;
    }
}