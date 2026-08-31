package xox.labvorty.chremastics.data.components.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import xox.labvorty.chremastics.data.currency.Currency;
import xox.labvorty.chremastics.data.currency.CurrencyHandlers;
import xox.labvorty.chremastics.items.CoinItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class CoinBagClientTooltipComponent implements ClientTooltipComponent {
    private static final int ENTRY_WIDTH = 32;

    private final int value;

    public CoinBagClientTooltipComponent(int value) {
        this.value = value;
    }

    private List<Entry> buildEntries() {
        Map<Currency, Integer> combined = new LinkedHashMap<>();

        for (ItemStack stack : CurrencyHandlers.getStacksFromValue(value)) {
            if (stack.getItem() instanceof CoinItem coinItem) {
                combined.merge(
                        coinItem.getCurrency(),
                        stack.getCount(),
                        Integer::sum
                );
            }
        }

        List<Entry> entries = new ArrayList<>();

        for (Map.Entry<Currency, Integer> entry : combined.entrySet()) {
            entries.add(new Entry(
                    entry.getKey(),
                    entry.getValue()
            ));
        }

        return entries;
    }

    @Override
    public void renderImage(
            @NotNull Font font,
            int x,
            int y,
            @NotNull GuiGraphics guiGraphics
    ) {
        RenderSystem.enableBlend();

        PoseStack poseStack = guiGraphics.pose();

        int currentX = x;

        for (Entry entry : buildEntries()) {
            Item item = CurrencyHandlers.getCoinFromCurrency(entry.currency);

            poseStack.pushPose();
            poseStack.translate(currentX, y, 0);
            poseStack.scale(0.5f, 0.5f, 0.5f);

            guiGraphics.renderFakeItem(item.getDefaultInstance(), 0, 0);

            poseStack.popPose();

            String amount = String.valueOf(entry.amount);

            guiGraphics.drawString(
                    font,
                    amount,
                    currentX + 9,
                    y,
                    -1
            );

            currentX += 2 + font.width(amount) + 8;
        }

        RenderSystem.disableBlend();
    }

    @Override
    public int getHeight() {
        return 9;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        int width = 0;

        for (Entry entry : buildEntries()) {
            width += 9 + font.width(String.valueOf(entry.amount)) + 8;
        }

        return Math.max(0, width - 8);
    }

    private record Entry(Currency currency, int amount) {}
}