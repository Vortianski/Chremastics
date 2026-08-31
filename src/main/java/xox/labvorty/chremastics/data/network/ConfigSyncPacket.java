package xox.labvorty.chremastics.data.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import xox.labvorty.chremastics.Chremastics;
import xox.labvorty.chremastics.data.utilities.ClientData;

public record ConfigSyncPacket(double bentCoinChance, double tradersInsigniaDiscount) implements CustomPacketPayload {
    public static final Type<ConfigSyncPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Chremastics.MOD_ID, "config_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ConfigSyncPacket> STREAM_CODEC = StreamCodec.of(
            (RegistryFriendlyByteBuf friendlyByteBuf, ConfigSyncPacket configSyncPacket) -> {
                friendlyByteBuf.writeDouble(configSyncPacket.bentCoinChance);
                friendlyByteBuf.writeDouble(configSyncPacket.tradersInsigniaDiscount);
            },
            (RegistryFriendlyByteBuf friendlyByteBuf) -> new ConfigSyncPacket(
                    friendlyByteBuf.readDouble(),
                    friendlyByteBuf.readDouble()
            )
    );

    @Override
    public @NotNull Type<ConfigSyncPacket> type() {
        return TYPE;
    }

    public static void handle(final ConfigSyncPacket configSyncPacket, final IPayloadContext payloadContext) {
        if (payloadContext.flow() == PacketFlow.CLIENTBOUND) {
            ClientData.setBentCoinChance(configSyncPacket.bentCoinChance);
            ClientData.setTradersInsigniaDiscount(configSyncPacket.tradersInsigniaDiscount);
        }
    }
}
