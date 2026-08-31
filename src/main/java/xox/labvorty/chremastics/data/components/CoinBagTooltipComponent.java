package xox.labvorty.chremastics.data.components;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record CoinBagTooltipComponent(int value) implements TooltipComponent {}