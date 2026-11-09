package xox.labvorty.chremastics.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xox.labvorty.chremastics.Chremastics;
import xox.labvorty.chremastics.gui.menu.ChremasticsMenu;

public class ChremasticsMenuType {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, Chremastics.MOD_ID);
    public static final DeferredHolder<MenuType<?>, MenuType<ChremasticsMenu>> CHREMASTICS = MENU_TYPES.register(
            "chremastics",
            () -> IMenuTypeExtension.create(ChremasticsMenu::new)
    );
}
