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

        ModRegistry.register(
                ModEntry.builder(MOD_ID, Component.literal("Chremastics"))
                        .commonConfig(
                                ConfigHolder.builder(CommonConfig.SPEC)
                                        .addBoolean(Component.literal("Use Whitelist"), Component.literal("Whether Chremastics should use whitelist and blacklist for coin drops"), CommonConfig.USE_COIN_ENTITY_WHITELIST, false)
                                        .addEntityList(Component.literal("Coin Entities"), Component.literal("Entities that drop coins after death"), CommonConfig.COIN_ENTITIES, List.of())
                                        .addEntityList(Component.literal("Restricted Coin Entities"), Component.literal("Entities that will never drop coins after death"), CommonConfig.RESTRICTED_COIN_ENTITIES, List.of())
                                        .addBoolean(Component.literal("Replace Emeralds"), Component.literal("Whether coins are used instead of emeralds in trades"), CommonConfig.REPLACE_EMERALDS_WITH_COINS, true)
                                        .addBoolean(Component.literal("Use Coin Bags for Trades"), Component.literal("Whether coins are used instead of emeralds in trades"), CommonConfig.USE_BAGS_FOR_TRADES, false)
                                        .addIntField(Component.literal("Emerald Value"), Component.literal("How much copper coins 1 emerald is worth"), CommonConfig.EMERALD_VALUE, 100, 0, 1000000)
                                        .addInt(Component.literal("Coin Drop Type"), Component.literal("Coin drop type, where:\n0 - determine value from health \n1 - randomly determine using chance values\n2 - don't drop"), CommonConfig.COIN_DROP_TYPE, 1, 0, 2)
                                        .addDouble(Component.literal("Copper Coin Drop Chance"), Component.literal("Chance for copper coins to appear in coin bags, and when coinDropType is 1 drop from entities"), CommonConfig.COPPER_COIN_CHANCE, 0.85, 0, 1, 2)
                                        .addInt(Component.literal("Copper Coin Minimal Amount"), Component.literal("Minimum number of copper coins in coin bags or random entity drops"), CommonConfig.COPPER_COIN_MIN_AMOUNT, 1, 0, 99)
                                        .addInt(Component.literal("Copper Coin Maximal Amount"), Component.literal("Maximum number of copper coins in coin bags or random entity drops"), CommonConfig.COPPER_COIN_MAX_AMOUNT, 15, 0, 99)
                                        .addDouble(Component.literal("Silver Coin Drop Chance"), Component.literal("Chance for silver coins to appear in coin bags, and when coinDropType is 1 drop from entities"), CommonConfig.SILVER_COIN_CHANCE, 0.30, 0, 1, 2)
                                        .addInt(Component.literal("Silver Coin Minimal Amount"), Component.literal("Minimum number of silver coins in coin bags or random entity drops"), CommonConfig.SILVER_COIN_MIN_AMOUNT, 1, 0, 99)
                                        .addInt(Component.literal("Silver Coin Maximal Amount"), Component.literal("Maximum number of silver coins in coin bags or random entity drops"), CommonConfig.SILVER_COIN_MAX_AMOUNT, 4, 0, 99)
                                        .addDouble(Component.literal("Gold Coin Drop Chance"), Component.literal("Chance for gold coins to appear in coin bags, and when coinDropType is 1 drop from entities"), CommonConfig.GOLD_COIN_CHANCE, 0.01, 0, 1, 2)
                                        .addInt(Component.literal("Gold Coin Minimal Amount"), Component.literal("Minimum number of gold coins in coin bags or random entity drops"), CommonConfig.GOLD_COIN_MIN_AMOUNT, 1, 0, 99)
                                        .addInt(Component.literal("Gold Coin Maximal Amount"), Component.literal("Maximum number of gold coins in coin bags or random entity drops"), CommonConfig.GOLD_COIN_MAX_AMOUNT, 1, 0, 99)
                                        .addDouble(Component.literal("Platinum Coin Drop Chance"), Component.literal("Chance for platinum coins to appear in coin bags, and when coinDropType is 1 drop from entities"), CommonConfig.PLATINUM_COIN_CHANCE, 0, 0, 1, 2)
                                        .addInt(Component.literal("Platinum Coin Minimal Amount"), Component.literal("Minimum number of platinum coins in coin bags or random entity drops"), CommonConfig.PLATINUM_COIN_MIN_AMOUNT, 0, 0, 99)
                                        .addInt(Component.literal("Platinum Coin Maximal Amount"), Component.literal("Maximum number of platinum coins in coin bags or random entity drops"), CommonConfig.PLATINUM_COIN_MAX_AMOUNT, 0, 0, 99)
                                        .addDouble(Component.literal("Bent Coin Trigger Chance"), Component.literal("Chance for Bent Coin to successfully trigger"), CommonConfig.BENT_COIN_CHANCE, 0.4, 0, 1, 2)
                                        .addDouble(Component.literal("Traders Insignia Discount"), Component.literal("How much less coins player has to pay for an item when Traders Insignia is equipped"), CommonConfig.TRADERS_INSIGNIA_DISCOUNT, 0.1, 0, 1, 2)
                                        .addBoolean(Component.literal("Drop Coins on Death"), Component.literal("Whether player loses coins after death"), CommonConfig.DROP_COINS_ON_DEATH, true)
                                        .addDouble(Component.literal("Coins Lost on Death"), Component.literal("Percentage of coins lost on death"), CommonConfig.COINS_LOST_ON_DEATH, 0.5, 0, 1, 2)
                                        .addDouble(Component.literal("Coin Bag Spawn Chance"), Component.literal("How frequently coin bag spawns in chests"), CommonConfig.COIN_BAG_SPAWN_CHANCE, 0.05, 0, 1, 2)
                                        .addDouble(Component.literal("Artifacts Spawn Chance"), Component.literal("How frequently artifacts spawn in chests"), CommonConfig.ARTIFACTS_SPAWN_CHANCE, 0.05, 0, 1, 2)
                                        .build()
                        )
                        .clientConfig(
                                ConfigHolder.builder(ClientConfig.SPEC)
                                        .addBoolean(Component.literal("Special Effects"), Component.literal("Whether particles and other visual effects appear in game"), ClientConfig.SPECIAL_EFFECTS, true)
                                        .addDouble(Component.literal("Particle Chance"), Component.literal("Chance for a particle to appear each frame for coin item entities and coin piles"), ClientConfig.PARTICLE_CHANCE, 0.02, 0, 1, 2)
                                        .addIntField(Component.literal("Purse Offset X"), Component.literal("How much X offset purse has"), ClientConfig.PURSE_OVERLAY_X_OFFSET, 0, -1000, 1000)
                                        .addIntField(Component.literal("Purse Offset Y"), Component.literal("How much Y offset purse has"), ClientConfig.PURSE_OVERLAY_Y_OFFSET, 0, -1000, 1000)
                                        .addIntField(Component.literal("Purse Button Offset X"), Component.literal("How much X offset purse button has"), ClientConfig.PURSE_OVERLAY_X_OFFSET, 0, -1000, 1000)
                                        .addIntField(Component.literal("Purse Button Offset Y"), Component.literal("How much Y offset purse button has"), ClientConfig.PURSE_OVERLAY_Y_OFFSET, 0, -1000, 1000)
                                        .build()
                        )
                        .addSocial(SocialType.discord("https://discord.gg/ZesGqhGnAN"))
                        .addSocial(SocialType.kofi("https://ko-fi.com/vortianski"))
                        .build()
        );
    }
}
