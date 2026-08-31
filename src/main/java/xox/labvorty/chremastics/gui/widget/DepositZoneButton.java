package xox.labvorty.chremastics.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class DepositZoneButton extends Button {
    private final String label;
    private final Supplier<ItemStack> stackSupplier;
    private final int contentOffsetX;

    public DepositZoneButton(
            int x,
            int y,
            int w,
            int h,
            String label,
            Supplier<ItemStack> stackSupplier,
            OnPress onPress
    ) {
        this(x, y, w, h, label, stackSupplier, 0, onPress);
    }

    public DepositZoneButton(
            int x,
            int y,
            int w,
            int h,
            String label,
            Supplier<ItemStack> stackSupplier,
            int contentOffsetX,
            OnPress onPress
    ) {
        super(x, y, w, h, Component.empty(), onPress, DEFAULT_NARRATION);
        this.label = label;
        this.stackSupplier = stackSupplier;
        this.contentOffsetX = contentOffsetX;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();

        Font font = Minecraft.getInstance().font;

        guiGraphics.fill(
                x, y,
                x + w, y + h,
                isHovered ? 0xFF5A5A5A : 0xFF3E3E3E
        );

        int dash = 4;

        for (int dx = 0; dx < w; dx += dash * 2) {
            guiGraphics.fill(
                    x + dx, y,
                    Math.min(x + dx + dash, x + w),
                    y + 1,
                    0xFFAAAAAA
            );
            guiGraphics.fill(
                    x + dx, y + h - 1,
                    Math.min(x + dx + dash, x + w),
                    y + h,
                    0xFFAAAAAA
            );
        }

        for (int dy = 0; dy < h; dy += dash * 2) {
            guiGraphics.fill(
                    x, y + dy,
                    x + 1,
                    Math.min(y + dy + dash, y + h),
                    0xFFAAAAAA
            );
            guiGraphics.fill(
                    x + w - 1, y + dy,
                    x + w,
                    Math.min(y + dy + dash, y + h),
                    0xFFAAAAAA
            );
        }

        ItemStack stack = stackSupplier.get();
        boolean hasItem = !stack.isEmpty();
        boolean hasText = !label.isEmpty();

        int iconSize = 16;
        int spacing = 4;

        if (!hasItem && !hasText) {
            return;
        }

        if (!hasItem) {
            int textWidth = font.width(label);

            int textX = x + (w - textWidth) / 2;
            int textY = y + (h - font.lineHeight) / 2;

            guiGraphics.drawString(
                    font,
                    label,
                    textX,
                    textY,
                    0xFFFFFF
            );

            return;
        }

        if (!hasText) {
            int iconX = x + (w - iconSize) / 2;
            int iconY = y + (h - iconSize) / 2;

            guiGraphics.renderItem(stack, iconX, iconY);

            return;
        }

        int textWidth = font.width(label);
        int contentWidth = iconSize + spacing + textWidth;
        int contentX = x + (w - contentWidth) / 2 + contentOffsetX;
        int iconY = y + (h - iconSize) / 2;
        int textY = y + (h - font.lineHeight) / 2;

        guiGraphics.renderItem(
                stack,
                contentX,
                iconY
        );

        guiGraphics.drawString(
                font,
                label,
                contentX + iconSize + spacing,
                textY,
                0xFFFFFF
        );
    }
}