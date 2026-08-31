package xox.labvorty.chremastics.data.currency;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum Currency implements StringRepresentable {
    COPPER(0xFFA85F3F, 0xFFFF9A68, 0xFFFFD8C8, 1),
    SILVER(0xFF8E9AA5, 0xFFDDEBFF, 0xFFF3F7FF, 100),
    GOLD(0xFFC69220,0xFFFFE06B, 0xFFFFF1C2, 10000),
    PLATINUM(0xFFAEBBC8, 0xFFFFFFFF, 0xFFF9FBFF, 1000000);

    private final int nameColor;
    private final int glintColor;
    private final int sparkleColor;
    private final int value;

    Currency(
            int nameColor,
            int glintColor,
            int sparkleColor,
            int value
    ) {
        this.nameColor = nameColor;
        this.glintColor = glintColor;
        this.sparkleColor = sparkleColor;
        this.value = value;
    }

    public int getNameColor() {
        return nameColor;
    }

    public int getGlintColor() {
        return glintColor;
    }

    public int getSparkleColor() {
        return sparkleColor;
    }

    public int getValue() {
        return value;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
