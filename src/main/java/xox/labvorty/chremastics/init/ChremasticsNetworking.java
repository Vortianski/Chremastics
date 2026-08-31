package xox.labvorty.chremastics.init;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import xox.labvorty.chremastics.Chremastics;
import xox.labvorty.chremastics.data.network.CoinPayload;
import xox.labvorty.chremastics.data.network.ConfigSyncPacket;
import xox.labvorty.vortylib.VortyLib;

@EventBusSubscriber(modid = Chremastics.MOD_ID)
public class ChremasticsNetworking {
    @SubscribeEvent
    public static void onCommon(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            VortyLib.addNetworkMessage(
                    CoinPayload.TYPE,
                    CoinPayload.STREAM_CODEC,
                    CoinPayload::handle
            );
            VortyLib.addNetworkMessage(
                    ConfigSyncPacket.TYPE,
                    ConfigSyncPacket.STREAM_CODEC,
                    ConfigSyncPacket::handle
            );
        });
    }
}