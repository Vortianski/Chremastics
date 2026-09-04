package xox.labvorty.chremastics;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;
import xox.labvorty.chremastics.data.configs.ClientConfig;
import xox.labvorty.chremastics.data.configs.CommonConfig;
import xox.labvorty.chremastics.init.*;
import xox.labvorty.vortylib.data.config.ConfigHolder;
import xox.labvorty.vortylib.data.config.ModEntry;
import xox.labvorty.vortylib.data.config.ModRegistry;
import xox.labvorty.vortylib.data.config.SocialType;

import java.util.List;

@Mod(Chremastics.MOD_ID)
public class Chremastics {
    public static final String MOD_ID = "chremastics";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Chremastics(IEventBus modEventBus, ModContainer modContainer) {
        ChremasticsItems.register(modEventBus);
        ChremasticsBlocks.BLOCKS.register(modEventBus);
        ChremasticsBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ChremasticsDataComponents.DATA_COMPONENTS.register(modEventBus);
        ChremasticsSoundEvents.SOUND_EVENTS.register(modEventBus);
        ChremasticsParticleTypes.PARTICLE_TYPES.register(modEventBus);
        ChremasticsCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ChremasticsAttachments.ATTACHMENT_TYPES.register(modEventBus);
        ChremasticsLootModifiers.LOOT_MODIFIERS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }
}
