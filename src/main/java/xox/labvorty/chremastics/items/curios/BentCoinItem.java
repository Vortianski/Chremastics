package xox.labvorty.chremastics.items.curios;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import xox.labvorty.chremastics.data.utilities.ClientData;

import java.util.List;

public class BentCoinItem extends Item implements ICurioItem {
    public BentCoinItem() {
        super(
                new Properties()
                        .stacksTo(1)
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("chremastics.tooltip.bent_coin", ClientData.getBentCoinChance() * 100, 100 - ClientData.getBentCoinChance() * 100).withStyle(style -> style.withColor(ChatFormatting.GRAY)));

        super.appendHoverText(itemStack, tooltipContext, tooltipComponents, tooltipFlag);
    }
}
