package xox.labvorty.chremastics.data.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

import java.util.UUID;

public abstract class CoinPileTakenEvent extends Event implements ICancellableEvent {
    private final Player player;
    private final BlockPos pos;

    protected CoinPileTakenEvent(Player player, BlockPos pos) {
        this.player = player;
        this.pos = pos;
    }

    public Player getPlayer() { return player; }
    public BlockPos getPos() { return pos; }

    public static class NotOwner extends CoinPileTakenEvent {
        private final UUID owner;

        public NotOwner(Player player, BlockPos pos, UUID owner) {
            super(player, pos);
            this.owner = owner;
        }

        public UUID getOwner() { return owner; }
    }

    public static class Owner extends CoinPileTakenEvent {
        public Owner(Player player, BlockPos pos) {
            super(player, pos);
        }
    }

    public static class Unowned extends CoinPileTakenEvent {
        public Unowned(Player player, BlockPos pos) {
            super(player, pos);
        }
    }
}