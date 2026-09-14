# Chremastics

Chremastics is a Terraria-inspired monetary system for Minecraft.

The mod adds a complete currency system with **Copper, Silver, Gold, and Platinum Coins**, along with coin bags, coin piles, configurable entity coin drops, and more. Emerald-based villager trades can also be replaced with coins, providing a more consistent and customizable economy.

Almost everything can be customized through configuration, including coin drop behavior, entity whitelists and blacklists, currency values, drop chances, death penalties, chest loot, visual effects, and the position of the purse UI.

Chremastics is designed to be flexible enough to work as anything from a simple emerald replacement to the foundation of a full RPG-style economy.

**Requirements:** Minecraft 1.21.1 NeoForge <br>
**Required dependencies:** VortyLib <br>
**Optional dependencies:** Curios, Jade

## Features

* Four tiers of currency: Copper, Silver, Gold and Platinum
* Coin Bags that can be found in the world, containing a random amount of coins
* Coin Piles for physically storing and displaying currency in the world
* Configurable entity coin drops
* Entity whitelists and blacklists for controlling coin drops
* Optional emerald-to-coin villager trading
* Purse overlay for keeping track of your money
* Configurable death penalties
* Configurable chest loot
* Configurable currency values and drop chances
* Configurable coin particles and visual effects
* Optional Curios integration
* Optional Jade integration
* Developer-friendly events and API hooks

## Developer Support

Chremastics provides events that allow other mods to customize or restrict interactions with coin piles.

For example, `CoinPileTakenEvent` is fired when a player attempts to take a coin from a coin pile. The event has three variants:

* `CoinPileTakenEvent.Owner` - the player owns the coin pile
* `CoinPileTakenEvent.NotOwner` - the coin pile belongs to another player
* `CoinPileTakenEvent.Unowned` - the coin pile has no owner

All variants are cancellable, allowing other mods to prevent the interaction when necessary.

This can be useful for implementing things such as protected storage, permissions, claims, faction systems, or custom economy rules.

## Configuration

Chremastics exposes extensive configuration for both clients and servers.

### Server-side

* Currency values and drop chances
* Entity coin drops
* Entity whitelists and blacklists
* Death penalties
* Chest loot
* Villager trading

### Client-side

* Purse overlay position
* Coin particles and visual effects
* Other client-side presentation options

The goal is to let modpack creators decide how simple or complex their economy should be without requiring additional mods or custom code.

## Compatibility

Chremastics is designed with modded environments in mind and aims to integrate cleanly with other mods.

* Curios integration is optional
* Jade integration is available
* Waystones integration is available using and can be tweaked using config
* Villager trading can optionally use Chremastics currency instead of emeralds
* Configuration allows the mod to adapt to different modpack designs

## Currency

Each currency tier has a different value, allowing large amounts of money to be represented without requiring enormous item stacks. Exact values can be changed through configuration.

| Currency | Value     |
| -------- | --------: |
| Copper   |         1 |
| Silver   |       100 |
| Gold     |    10,000 |
| Platinum | 1,000,000 |

## Links

[Modrinth](https://modrinth.com/mod/chremastics) · [CurseForge](https://www.curseforge.com/minecraft/mc-mods/chremastics) · [GitHub](https://github.com/Vortianski/Chremastics) · [Discord](https://discord.gg/ZesGqhGnAN)