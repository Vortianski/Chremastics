package xox.labvorty.chremastics.data.utilities;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;
import xox.labvorty.vortylib.utilities.VortyLibCurioUtilities;

public class CurioUtilities {
    public static boolean hasCurioItem(LivingEntity livingEntity, Item item) {
        if (ModList.get().isLoaded("curios")) {
            return VortyLibCurioUtilities.hasCurio(livingEntity, item);
        }

        return false;
    }
}
