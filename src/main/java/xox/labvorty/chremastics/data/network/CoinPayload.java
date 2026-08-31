package xox.labvorty.chremastics.data.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import xox.labvorty.chremastics.Chremastics;
import xox.labvorty.chremastics.data.currency.Currency;
import xox.labvorty.chremastics.data.currency.CurrencyHandlers;
import xox.labvorty.chremastics.data.currency.PlayerCoinPurse;
import xox.labvorty.chremastics.items.CoinItem;
import xox.labvorty.vortylib.utilities.VortyLibUtilities;

public record CoinPayload(int id, int amount, int currencyOrdinal) implements CustomPacketPayload {
    public static final Type<CoinPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Chremastics.MOD_ID, "coin"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CoinPayload> STREAM_CODEC = StreamCodec.of(
            (RegistryFriendlyByteBuf friendlyByteBuf, CoinPayload coinPayload) -> {
                friendlyByteBuf.writeInt(coinPayload.id);
                friendlyByteBuf.writeInt(coinPayload.amount);
                friendlyByteBuf.writeInt(coinPayload.currencyOrdinal);
            },
            (RegistryFriendlyByteBuf friendlyByteBuf) -> new CoinPayload(
                    friendlyByteBuf.readInt(),
                    friendlyByteBuf.readInt(),
                    friendlyByteBuf.readInt()
            )
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final CoinPayload coinPayload, final IPayloadContext payloadContext) {
        if (payloadContext.flow() == PacketFlow.SERVERBOUND) {
            Player player = payloadContext.player();
            if (player instanceof ServerPlayer serverPlayer) {
                if (coinPayload.id == 0) {
                    AbstractContainerMenu abstractContainerMenu = serverPlayer.containerMenu;
                    ItemStack itemStack = abstractContainerMenu.getCarried();

                    if (!itemStack.isEmpty() && itemStack.getItem() instanceof CoinItem coinItem) {
                        PlayerCoinPurse.add(serverPlayer, coinItem.getCurrency().getValue() * itemStack.getCount());
                        abstractContainerMenu.setCarried(ItemStack.EMPTY);
                    }
                }

                if (coinPayload.id == 1) {
                    Inventory inventory = serverPlayer.getInventory();
                    for (ItemStack itemStack : inventory.items) {
                        if (!itemStack.isEmpty() && itemStack.getItem() instanceof CoinItem coinItem) {
                            PlayerCoinPurse.add(serverPlayer, coinItem.getCurrency().getValue() * itemStack.getCount());
                            itemStack.setCount(0);
                        }
                    }
                }

                if (coinPayload.id == 2) {
                    Currency[] tiers = Currency.values();
                    if (coinPayload.currencyOrdinal < 0 || coinPayload.currencyOrdinal >= tiers.length) return;
                    if (coinPayload.amount <= 0) return;

                    Currency currency = tiers[coinPayload.currencyOrdinal];
                    int requiredValue = currency.getValue() * coinPayload.amount();

                    if (!PlayerCoinPurse.tryRemoveBalance(serverPlayer, requiredValue)) return;

                    CoinItem coinItem = CurrencyHandlers.getCoinFromCurrency(currency);
                    int remaining = coinPayload.amount;
                    while (remaining > 0) {
                        int chunk = Math.min(99, remaining);
                        VortyLibUtilities.tryInsertOrDrop(serverPlayer, new ItemStack(coinItem, chunk));
                        remaining -= chunk;
                    }
                }
            }
        }

        if (payloadContext.flow() == PacketFlow.CLIENTBOUND) {
            //unused
        }
    }
}
