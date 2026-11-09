package xox.labvorty.chremastics.gui.menu;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import xox.labvorty.chremastics.items.CoinItem;
import xox.labvorty.chremastics.data.currency.PlayerCoinPurse;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;
import xox.labvorty.chremastics.init.ChremasticsMenuType;

import java.util.Optional;

public class ChremasticsMenu extends RecipeBookMenu<RecipeInput, Recipe<RecipeInput>> {
    private static final EquipmentSlot[] ARMOR = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private static final ResourceLocation[] ARMOR_ICONS = {InventoryMenu.EMPTY_ARMOR_SLOT_HELMET, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS};
    private final Player player;
    private final CraftingContainer craftMatrix = new TransientCraftingContainer(this, 2, 2);
    private final ResultContainer craftResult = new ResultContainer();
    private final SimpleContainer depositInventory = new SimpleContainer(1);
    private boolean processingDeposit;
    public static final int DEPOSIT_SLOT = 46;
    public static final int DEPOSIT_X = -(100 + 6) + 8;
    public static final int DEPOSIT_Y = 60;

    public ChremasticsMenu(int id, Inventory inventory, FriendlyByteBuf ignored) {
        this(id, inventory);
    }

    public ChremasticsMenu(int id, Inventory inventory) {
        super(ChremasticsMenuType.CHREMASTICS.get(), id);
        player = inventory.player;

        addSlot(new ResultSlot(player, craftMatrix, craftResult, 0, 154, 28));
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                addSlot(new Slot(craftMatrix, col + row * 2, 98 + col * 18, 18 + row * 18));
            }
        }

        for (int i = 0; i < 4; i++) {
            final EquipmentSlot equipment = ARMOR[i];
            final ResourceLocation icon = ARMOR_ICONS[i];
            addSlot(new Slot(inventory, 39 - i, 8, 8 + i * 18) {
                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.canEquip(equipment, player);
                }

                @Override
                public boolean mayPickup(@NotNull Player player) {
                    ItemStack stack = getItem();
                    return (stack.isEmpty() || player.isCreative() || !EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) && super.mayPickup(player);
                }

                @Override
                public void set(@NotNull ItemStack stack) {
                    ItemStack old = getItem();
                    super.set(stack);
                    player.onEquipItem(equipment, old, stack);
                }

                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, icon);
                }
            });
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inventory, 9 + row * 9 + col, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inventory, col, 8 + col * 18, 142));
        }

        addSlot(new Slot(inventory, 40, 77, 62) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });

        addSlot(new Slot(depositInventory, 0, DEPOSIT_X, DEPOSIT_Y) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() instanceof CoinItem;
            }

            @Override
            public boolean mayPickup(@NotNull Player player) {
                return false;
            }

            @Override
            public int getMaxStackSize() {
                return 99;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                consumeDeposit();
            }
        });
    }

    private void consumeDeposit() {
        if (processingDeposit || !(player instanceof ServerPlayer serverPlayer)) return;
        ItemStack stack = depositInventory.getItem(0);
        if (stack.isEmpty()) return;
        if (!(stack.getItem() instanceof CoinItem coin)) return;
        processingDeposit = true;
        try {
            long value = (long) coin.getCurrency().getValue() * stack.getCount();
            if (value > 0 && value <= Integer.MAX_VALUE) {
                PlayerCoinPurse.add(serverPlayer, (int) value);
                depositInventory.setItem(0, ItemStack.EMPTY);
                broadcastChanges();
            }
        } finally {
            processingDeposit = false;
        }
    }

    @Override
    public boolean canDragTo(@NotNull Slot slot) {
        return slot.container != depositInventory && super.canDragTo(slot);
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        if (player.level().isClientSide) return;
        ServerPlayer serverPlayer = (ServerPlayer) player;
        ItemStack output = ItemStack.EMPTY;
        Optional<RecipeHolder<CraftingRecipe>> recipe = player.level().getServer().getRecipeManager()
                .getRecipeFor(RecipeType.CRAFTING, craftMatrix.asCraftInput(), player.level());
        if (recipe.isPresent()) {
            RecipeHolder<CraftingRecipe> holder = recipe.get();
            if (craftResult.setRecipeUsed(player.level(), serverPlayer, holder)) {
                ItemStack result = holder.value().assemble(craftMatrix.asCraftInput(), player.level().registryAccess());
                if (result.isItemEnabled(player.level().enabledFeatures())) output = result;
            }
        }
        craftResult.setItem(0, output);
        setRemoteSlot(0, output);
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, incrementStateId(), 0, output));
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        craftResult.clearContent();
        if (!player.level().isClientSide) {
            consumeDeposit();
            clearContainer(player, depositInventory);
            clearContainer(player, craftMatrix);
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) { return player.isAlive(); }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        if (index < 0 || index >= slots.size()) return ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (index == DEPOSIT_SLOT || !slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        EquipmentSlot equipment = player.getEquipmentSlotForItem(stack);

        if (index == 0) {
            if (!moveItemStackTo(stack, 9, 45, true)) return ItemStack.EMPTY;
            slot.onQuickCraft(stack, original);
        } else if (index >= 1 && index < 9) {
            if (!moveItemStackTo(stack, 9, 45, false)) return ItemStack.EMPTY;
        } else if (equipment.getType() == EquipmentSlot.Type.HUMANOID_ARMOR
                && !slots.get(8 - equipment.getIndex()).hasItem()) {
            int target = 8 - equipment.getIndex();
            if (!moveItemStackTo(stack, target, target + 1, false)) return ItemStack.EMPTY;
        } else if (equipment == EquipmentSlot.OFFHAND && !slots.get(45).hasItem()) {
            if (!moveItemStackTo(stack, 45, 46, false)) return ItemStack.EMPTY;
        } else if (index >= 9 && index < 36) {
            if (!moveItemStackTo(stack, 36, 45, false)) return ItemStack.EMPTY;
        } else if (index >= 36 && index < 45) {
            if (!moveItemStackTo(stack, 9, 36, false)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(stack, 9, 45, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        if (index == 0) player.drop(stack, false);
        return original;
    }

    @Override
    public @NotNull RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int index) {
        return index != 0;
    }

    @Override
    public void fillCraftSlotsStackedContents(@NotNull StackedContents contents) {
        craftMatrix.fillStackedContents(contents);
    }

    @Override
    public void clearCraftingContent() {
        craftMatrix.clearContent();
        craftResult.clearContent();
    }

    @Override
    public boolean recipeMatches(RecipeHolder recipe) {
        return recipe.value().matches(craftMatrix.asCraftInput(), player.level());
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return 2;
    }

    @Override
    public int getGridHeight() {
        return 2;
    }

    @Override
    public int getSize() {
        return 5;
    }
}
