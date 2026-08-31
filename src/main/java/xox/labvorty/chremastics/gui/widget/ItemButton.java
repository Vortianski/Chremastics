package xox.labvorty.chremastics.gui.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ItemButton extends Button {
    private final Supplier<ItemStack> stackSupplier;
    private final Supplier<Boolean> selectedSupplier;

    public ItemButton(int x, int y, int w, int h, Supplier<ItemStack> stackSupplier, OnPress onPress, Supplier<Boolean> selectedSupplier) {
        super(x, y, w, h, Component.empty(), onPress, DEFAULT_NARRATION);
        this.stackSupplier = stackSupplier;
        this.selectedSupplier = selectedSupplier;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();

        guiGraphics.fill(x, y, x + w, y + h, 0xFF8B8B8B);
        guiGraphics.fill(x, y, x + w, y + 1, 0xFFFFFFFF);
        guiGraphics.fill(x, y, x + 1, y + h, 0xFFFFFFFF);
        guiGraphics.fill(x, y + h - 1, x + w, y + h, 0xFF373737);
        guiGraphics.fill(x + w - 1, y, x + w, y + h, 0xFF373737);

        boolean selected = selectedSupplier != null && selectedSupplier.get();
        if (selected) {
            guiGraphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, 0x80FFFFFF);
        } else if (isHovered) {
            guiGraphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, 0x40FFFFFF);
        }

        ItemStack stack = stackSupplier.get();
        if (!stack.isEmpty()) {
            int iconX = x + (w - 16) / 2;
            int iconY = y + (h - 16) / 2;
            guiGraphics.renderItem(stack, iconX, iconY);
        }
    }
}