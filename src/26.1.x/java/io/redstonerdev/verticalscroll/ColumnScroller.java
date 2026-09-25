package io.redstonerdev.verticalscroll;

import io.redstonerdev.verticalscroll.mixin.PlayerInventoryAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.InventoryMenu;

/**
 * Shared column-rotation logic, extracted from {@link io.redstonerdev.verticalscroll.mixin.MouseMixin}
 * so that both the scroll wheel and the "scroll one step" key binding can reuse it.
 */
public final class ColumnScroller {

    private ColumnScroller() {
    }

    public static void rotate(Minecraft minecraft, boolean scrollUp) {
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.gameMode == null) return;

        Inventory inventory = player.getInventory();
        int hotbarIndex = ((PlayerInventoryAccessor) inventory).getSelectedSlot();

        int topRowSlot = 9  + hotbarIndex;
        int midRowSlot = 18 + hotbarIndex;
        int botRowSlot = 27 + hotbarIndex;
        int hotbarSlot = 36 + hotbarIndex;

        InventoryMenu handler = player.inventoryMenu;
        int containerId = handler.containerId;

        int[] slots = scrollUp
                ? new int[]{hotbarSlot, botRowSlot, midRowSlot, topRowSlot}
                : new int[]{topRowSlot, midRowSlot, botRowSlot, hotbarSlot};

        for (int slot : slots) {
            minecraft.gameMode.handleContainerInput(containerId, slot, 0, ContainerInput.PICKUP, player);
        }
        minecraft.gameMode.handleContainerInput(containerId, slots[0], 0, ContainerInput.PICKUP, player);

        // Vanilla hotbar "pop"/stretch animation on the item that just arrived in hand,
        // the same one shown when you pick an item up.
        inventory.getItem(hotbarIndex).setPopTime(5);
    }
}
