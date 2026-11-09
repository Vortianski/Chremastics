package xox.labvorty.chremastics.data.utilities;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xox.labvorty.chremastics.data.configs.ClientConfig;
import xox.labvorty.chremastics.data.network.OpenPursePayload;
import xox.labvorty.chremastics.gui.widget.ImageButton;

@EventBusSubscriber(value = Dist.CLIENT)
public class CoinPurseOverlay {
    @SubscribeEvent
    public static void screenInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();

        if (!(screen instanceof InventoryScreen)) {
            return;
        }

        ImageButton purseButton = new ImageButton(
                xPosition(screen),
                yPosition(screen),
                16,
                16,
                () -> new ImageButton.Image(
                        ResourceLocation.fromNamespaceAndPath(
                                "chremastics",
                                "textures/item/bag_small.png"
                        ),
                        16, 16, 16, 16
                ),
                button -> PacketDistributor.sendToServer(
                        new OpenPursePayload()
                )
        );

        event.addListener(purseButton);
    }

    private static int xPosition(Screen screen) {
        return screen.width / 2 + 88 - 20
                + ClientConfig.PURSE_BUTTON_OVERLAY_X_OFFSET.get();
    }

    private static int yPosition(Screen screen) {
        return screen.height / 2 - 83 + 2
                + ClientConfig.PURSE_BUTTON_OVERLAY_Y_OFFSET.get();
    }
}
