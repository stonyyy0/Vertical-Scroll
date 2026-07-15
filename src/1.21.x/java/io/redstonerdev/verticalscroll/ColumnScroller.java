package io.redstonerdev.verticalscroll;

import io.redstonerdev.verticalscroll.mixin.PlayerInventoryAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

/**
 * Shared column-rotation logic, extracted from {@link io.redstonerdev.verticalscroll.mixin.MouseMixin}
 * so that both the scroll wheel and the "scroll one step" key binding can reuse it.
 */
public final class ColumnScroller {

    private ColumnScroller() {
    }

    public static void rotate(MinecraftClient client, boolean scrollUp) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.interactionManager == null) return;

        PlayerInventory inventory = player.getInventory();
        int hotbarIndex = ((PlayerInventoryAccessor) inventory).getSelectedSlot();

        int topRowSlot = 9  + hotbarIndex;
        int midRowSlot = 18 + hotbarIndex;
        int botRowSlot = 27 + hotbarIndex;
        int hotbarSlot = 36 + hotbarIndex;

        PlayerScreenHandler handler = player.playerScreenHandler;
        int syncId = handler.syncId;

        int[] slots = scrollUp
                ? new int[]{hotbarSlot, botRowSlot, midRowSlot, topRowSlot}
                : new int[]{topRowSlot, midRowSlot, botRowSlot, hotbarSlot};

        for (int slot : slots) {
            client.interactionManager.clickSlot(syncId, slot, 0, SlotActionType.PICKUP, player);
        }
        client.interactionManager.clickSlot(syncId, slots[0], 0, SlotActionType.PICKUP, player);

        // Vanilla hotbar "pop"/stretch animation on the item that just arrived in hand,
        // the same one shown when you pick an item up.
        inventory.getStack(hotbarIndex).setBobbingAnimationTime(5);
    }
}
