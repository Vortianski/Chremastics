package xox.labvorty.chremastics.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import xox.labvorty.chremastics.data.configs.ClientConfig;
import xox.labvorty.chremastics.data.currency.Currency;
import xox.labvorty.chremastics.data.currency.CurrencyHandlers;
import xox.labvorty.chremastics.data.network.CoinPayload;
import xox.labvorty.chremastics.data.network.OpenPursePayload;
import xox.labvorty.chremastics.gui.menu.ChremasticsMenu;
import xox.labvorty.chremastics.gui.widget.ImageButton;
import xox.labvorty.chremastics.gui.widget.ItemButton;
import xox.labvorty.chremastics.init.ChremasticsAttachments;
import java.util.Arrays;
import java.util.Locale;

public class ChremasticsScreen extends EffectRenderingInventoryScreen<ChremasticsMenu> {
    private Currency selectedCurrency = Currency.COPPER;
    private int amount = 1;

    public ChremasticsScreen(ChremasticsMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 166;
        titleLabelX = 97;
        titleLabelY = 6;
    }

    private int purseX() {
        return leftPos - 106;
    }

    private int currencyRows() {
        return Math.max(1, (Currency.values().length + 3) / 4);
    }

    private int controlsY() {
        return 92 + currencyRows() * 18 + 16;
    }

    @Override
    protected void init() {
        super.init();
        leftPos = (width - (282)) / 2 + 106;
        topPos = (height - 166) / 2;
        int x = purseX(), y = topPos;

        ImageButton purseButton = new ImageButton(
                width / 2 + 120 + ClientConfig.PURSE_BUTTON_OVERLAY_X_OFFSET.get(),
                height / 2 - 81 + ClientConfig.PURSE_BUTTON_OVERLAY_Y_OFFSET.get(),
                16,
                16,
                () -> new ImageButton.Image(
                        ResourceLocation.fromNamespaceAndPath(
                                "chremastics",
                                "textures/item/coin_bag_overlay.png"
                        ),
                        16, 16, 16, 16
                ),
                button -> {
                    if (minecraft != null && minecraft.player != null) {
                        minecraft.player.closeContainer();
                        minecraft.setScreen(new InventoryScreen(minecraft.player));
                    }
                }
        );
        addRenderableWidget(purseButton);

        Button depositAll = Button.builder(Component.translatable("gui.chremastics.deposit_all"), b -> PacketDistributor.sendToServer(new CoinPayload(1, 0, 0))).bounds(x + 48, y + 59, 46, 18).build();
        depositAll.setTooltip(Tooltip.create(Component.translatable("gui.chremastics.deposit_all.tooltip")));
        addRenderableWidget(depositAll);

        Currency[] tiers = Currency.values();
        for (int i = 0; i < tiers.length; i++) {
            Currency tier = tiers[i];
            Item item = CurrencyHandlers.getCoinFromCurrency(tier);
            int col = i % 4, row = i / 4;
            ItemButton button = new ItemButton(x + 6 + col * 22, y + 92 + row * 18,
                    21, 18, item::getDefaultInstance,
                    b -> selectedCurrency = tier, () -> selectedCurrency == tier);
            button.setTooltip(Tooltip.create(currencyName(tier)));
            addRenderableWidget(button);
        }

        int cy = y + controlsY();
        addStepper(x + 6, cy, "-10", -10);
        addStepper(x + 28, cy, "-1", -1);
        addStepper(x + 50, cy, "+1", 1);
        addStepper(x + 72, cy, "+10", 10);
        addRenderableWidget(Button.builder(Component.translatable("gui.chremastics.withdraw"), b -> PacketDistributor.sendToServer(new CoinPayload(2, amount, selectedCurrency.ordinal()))).bounds(x + 6, cy + 18, 100 - 6 * 2, 14).build());
    }

    private static Component currencyName(Currency currency) {
        return Component.translatable("currency.chremastics." + currency.name().toLowerCase(Locale.ROOT));
    }

    private void addStepper(int x, int y, String label, int delta) {
        addRenderableWidget(Button.builder(Component.literal(label), b ->
                amount = (int) Math.max(1L, Math.min(Integer.MAX_VALUE, (long) amount + delta)))
                .bounds(x, y, 21, 16).build());
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
        if (hoveredSlot != null && hoveredSlot == menu.getSlot(ChremasticsMenu.DEPOSIT_SLOT) && !hoveredSlot.hasItem()) {
            graphics.renderTooltip(font, Component.translatable("gui.chremastics.deposit_hint"), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(ResourceLocation.withDefaultNamespace("textures/gui/container/inventory.png"), leftPos, topPos, 0, 0, 176, 166);
        if (minecraft != null && minecraft.player != null) {
            InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, leftPos + 26, topPos + 8, leftPos + 75, topPos + 78, 30, 0.0625F, mouseX, mouseY, minecraft.player);
        }

        int x = purseX(), y = topPos;
        graphics.blit(ResourceLocation.fromNamespaceAndPath("chremastics", "textures/gui/coin_purse.png"), x, y, 0, 0, 100, 166, 100, 166);
        graphics.drawString(font, Component.translatable("gui.chremastics.purse"), x + 6, y + 6, 0x404040, false);
        renderBalance(graphics, x + 6, y + 17);
        graphics.drawString(font, Component.translatable("gui.chremastics.deposit"), x + 6, y + 50, 0x404040, false);
        int slotX = leftPos + ChremasticsMenu.DEPOSIT_X;
        int slotY = topPos + ChremasticsMenu.DEPOSIT_Y;
        graphics.fill(slotX - 1, slotY - 1, slotX + 17, slotY + 17, 0xFF101010);
        graphics.fill(slotX, slotY, slotX + 16, slotY + 16, 0xFF686868);
        graphics.drawString(font, Component.translatable("gui.chremastics.currency"), x + 6, y + 82, 0x404040, false);
        graphics.drawString(font, Component.translatable("gui.chremastics.amount", amount), x + 6, y + controlsY() - 12, 0x404040, false);
    }

    private void renderBalance(GuiGraphics graphics, int x, int y) {
        if (minecraft == null || minecraft.player == null) return;
        Currency[] tiers = Currency.values().clone();
        Arrays.sort(tiers, (a, b) -> Integer.compare(b.getValue(), a.getValue()));
        long remaining = Math.max(0L, minecraft.player.getData(ChremasticsAttachments.COIN_BALANCE));
        for (int i = 0; i < Math.min(4, tiers.length); i++) {
            Currency tier = tiers[i];
            if (tier.getValue() <= 0) continue;
            long count = remaining / tier.getValue();
            remaining %= tier.getValue();
            int cellX = x + (i % 2) * 44;
            int cellY = y + (i / 2) * 16;
            graphics.renderItem(CurrencyHandlers.getCoinFromCurrency(tier).getDefaultInstance(), cellX, cellY);
            graphics.drawString(font, "x" + count, cellX + 16, cellY + 4, 0x404040, false);
        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, Component.translatable("container.crafting"), titleLabelX, titleLabelY, 0xFF404040, false);
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeft, int guiTop, int mouseButton) {
        boolean purse = mouseX >= purseX() && mouseX < purseX() + 100
                && mouseY >= topPos && mouseY < topPos + 166;
        return !purse && super.hasClickedOutside(mouseX, mouseY, guiLeft, guiTop, mouseButton);
    }
}
