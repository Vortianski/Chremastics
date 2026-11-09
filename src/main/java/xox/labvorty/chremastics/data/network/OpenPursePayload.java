
package xox.labvorty.chremastics.data.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import xox.labvorty.chremastics.gui.menu.ChremasticsMenu;

public record OpenPursePayload() implements CustomPacketPayload {
    public static final Type<OpenPursePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("chremastics", "open_purse"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenPursePayload> STREAM_CODEC = StreamCodec.unit(new OpenPursePayload());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final OpenPursePayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }

            player.openMenu(new SimpleMenuProvider(
                    (containerId, inventory, p) ->
                            new ChremasticsMenu(containerId, inventory),
                    Component.translatable(
                            "container.chremastics.purse"
                    )
            ));
        });
    }
}
