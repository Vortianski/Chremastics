package xox.labvorty.chremastics.gui.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ImageButton extends Button {
    private final Supplier<Image> imageSupplier;

    public ImageButton(
            int x,
            int y,
            int w,
            int h,
            Supplier<Image> imageSupplier,
            OnPress onPress
    ) {
        super(x, y, w, h, Component.empty(), onPress, DEFAULT_NARRATION);
        this.imageSupplier = imageSupplier;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Image image = imageSupplier.get();

        if (image == null || image.texture() == null) {
            return;
        }

        guiGraphics.blit(
                image.texture(),
                getX(),
                getY(),
                0,
                0,
                image.sizeX(),
                image.sizeY(),
                image.spriteSizeX(),
                image.spriteSizeY()
        );
    }

    public record Image(
            ResourceLocation texture,
            int sizeX,
            int sizeY,
            int spriteSizeX,
            int spriteSizeY
    ) {}
}