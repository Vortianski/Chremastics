package xox.labvorty.chremastics.data.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xox.labvorty.chremastics.data.configs.ClientConfig;
import xox.labvorty.chremastics.data.currency.Currency;
import xox.labvorty.chremastics.data.currency.CurrencyHandlers;
import xox.labvorty.chremastics.data.network.CoinPayload;
import xox.labvorty.chremastics.gui.widget.DepositZoneButton;
import xox.labvorty.chremastics.gui.widget.ImageButton;
import xox.labvorty.chremastics.gui.widget.ItemButton;
import xox.labvorty.chremastics.init.ChremasticsAttachments;
import xox.labvorty.chremastics.init.ChremasticsItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT)
public class CoinPurseOverlay {
    private static boolean purseOpen = false;
    private static ImageButton purseToggle;
    private static List<ItemButton> itemButtons = new ArrayList<>();
    private static Currency selectedCurrency = Currency.COPPER;
    private static DepositZoneButton depositZoneButton;
    private static DepositZoneButton depositAllButton;

    private static int amount = 1;
    private static final List<Button> withdrawSteppers = new ArrayList<>();
    private static DepositZoneButton withdrawButton;

    @SubscribeEvent
    public static void screenInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (!(screen instanceof InventoryScreen inventoryScreen)) return;

        purseToggle = new ImageButton(
                xPositionS(screen) - 20,
                yPositionS(screen) + 2,
                16,
                16,
                () -> new ImageButton.Image(
                        purseOpen ?
                                ResourceLocation.fromNamespaceAndPath("chremastics", "textures/item/coin_bag_overlay.png")
                                : ResourceLocation.fromNamespaceAndPath("chremastics", "textures/item/bag_small.png"),
                        16,
                        16,
                        16,
                        16
                ),
                button -> {
                    purseOpen = !purseOpen;
                }
        );
        event.addListener(purseToggle);

        itemButtons.clear();
        for (Currency currency : Currency.values()) {
            ItemButton itemButton = new ItemButton(
                    xPosition(screen) + 60 + 12 * currency.ordinal(),
                    yPosition(screen) + 82,
                    10,
                    10,
                    () -> CurrencyHandlers.getCoinFromCurrency(currency).getDefaultInstance(),
                    button -> {
                        selectedCurrency = currency;
                    },
                    () -> {
                        return selectedCurrency == currency;
                    }
            );
            itemButton.visible = purseOpen;
            itemButtons.add(itemButton);
            event.addListener(itemButton);
        }

        for (ItemButton itemButton : itemButtons) {
            event.addListener(itemButton);
        }

        depositZoneButton = new DepositZoneButton(
                xPosition(screen),
                yPosition(screen) + 34,
                85,
                20,
                "Deposit",
                () -> ItemStack.EMPTY,
                button -> {
                    PacketDistributor.sendToServer(new CoinPayload(0, 0, 0));
                }
        );
        depositZoneButton.visible = purseOpen;
        event.addListener(depositZoneButton);

        depositAllButton = new DepositZoneButton(
                xPosition(screen) + 90,
                yPosition(screen) + 34,
                20,
                20,
                "",
                () -> ChremasticsItems.COIN_BAG.get().getDefaultInstance(),
                button -> {
                    PacketDistributor.sendToServer(new CoinPayload(1, 0, 0));
                }
        );
        depositAllButton.visible = purseOpen;
        event.addListener(depositAllButton);

        withdrawSteppers.clear();
        int stepperY = yPosition(screen) + 58;
        int stepperW = 26;
        withdrawSteppers.add(addStepper(event, xPosition(screen), stepperY, stepperW, "-10", -10));
        withdrawSteppers.add(addStepper(event, xPosition(screen) + (stepperW + 2), stepperY, stepperW, "-1", -1));
        withdrawSteppers.add(addStepper(event, xPosition(screen) + (stepperW + 2) * 2, stepperY, stepperW, "+1", 1));
        withdrawSteppers.add(addStepper(event, xPosition(screen) + (stepperW + 2) * 3, stepperY, stepperW, "+10", 10));

        withdrawButton = new DepositZoneButton(
                xPosition(screen),
                yPosition(screen) + 96,
                110,
                20,
                "Withdraw",
                () -> CurrencyHandlers.getCoinFromCurrency(selectedCurrency).getDefaultInstance(),
                -5,
                button -> {
                    PacketDistributor.sendToServer(new CoinPayload(2, amount, selectedCurrency.ordinal()));
                }
        );
        withdrawButton.visible = purseOpen;
        event.addListener(withdrawButton);
    }

    private static Button addStepper(ScreenEvent.Init.Post event, int x, int y, int w, String label, int delta) {
        Button button = Button.builder(Component.literal(label), b -> adjustAmount(delta))
                .bounds(x, y, w, 16)
                .build();
        button.visible = purseOpen;
        event.addListener(button);
        return button;
    }

    private static void adjustAmount(int delta) {
        amount = Math.max(1, amount + delta);
    }

    @SubscribeEvent
    public static void screenRender(ScreenEvent.Render.Pre event) {
        Screen screen = event.getScreen();
        if (!(screen instanceof InventoryScreen inventoryScreen)) return;
        GuiGraphics guiGraphics = event.getGuiGraphics();

        if (!itemButtons.isEmpty()) {
            for (ItemButton itemButton : itemButtons) {
                itemButton.visible = purseOpen;
            }
        }

        if (depositZoneButton != null) {
            depositZoneButton.visible = purseOpen;
        }

        if (depositAllButton != null) {
            depositAllButton.visible = purseOpen;
        }

        for (Button stepper : withdrawSteppers) {
            stepper.visible = purseOpen;
        }

        if (withdrawButton != null) {
            withdrawButton.visible = purseOpen;
        }

        if (purseOpen) {
            Minecraft minecraft = screen.getMinecraft();
            if (minecraft.player != null) {
                Currency[] sorted = Currency.values().clone();
                Arrays.sort(sorted, (a, b) -> Integer.compare(b.getValue(), a.getValue()));

                int cellWidth = 84 / 2;
                int remaining = minecraft.player.getData(ChremasticsAttachments.COIN_BALANCE);
                for (int i = 0; i < sorted.length; i++) {
                    Currency tier = sorted[i];
                    int count = remaining / tier.getValue();
                    remaining %= tier.getValue();

                    int col = i % 2;
                    int y = (i < 2) ? yPosition(screen) : yPosition(screen) + 16;
                    int x = xPosition(screen) + col * cellWidth;

                    guiGraphics.renderItem(CurrencyHandlers.getCoinFromCurrency(tier).getDefaultInstance(), x, y);
                    guiGraphics.drawString(minecraft.font, Component.literal("x" + count), x + 16, y + 4, 0xAAAAAA);
                }

                int indicatorY = yPosition(screen) + 78;
                guiGraphics.renderItem(CurrencyHandlers.getCoinFromCurrency(selectedCurrency).getDefaultInstance(), xPosition(screen), indicatorY);
                guiGraphics.drawString(minecraft.font, Component.literal("x" + amount), xPosition(screen) + 18, indicatorY + 4, 0xFFFFFF);
            }
        }
    }

    public static int xPositionS(Screen screen) {
        int width = screen.width;

        return width / 2 + 176 / 2 + ClientConfig.PURSE_BUTTON_OVERLAY_X_OFFSET.get();
    }

    public static int xPosition(Screen screen) {
        int width = screen.width;

        return width / 2 - 176 / 2 - 120 + ClientConfig.PURSE_OVERLAY_X_OFFSET.get();
    }

    public static int yPositionS(Screen screen) {
        int height = screen.height;

        return height / 2 - 166 / 2 + ClientConfig.PURSE_BUTTON_OVERLAY_Y_OFFSET.get();
    }

    public static int yPosition(Screen screen) {
        int height = screen.height;

        return height / 2 - 166 / 2 + ClientConfig.PURSE_OVERLAY_Y_OFFSET.get();
    }
}