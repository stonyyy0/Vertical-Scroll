package io.redstonerdev.verticalscroll.mixin;

import io.redstonerdev.verticalscroll.VerticalScrollConfig;
import io.redstonerdev.verticalscroll.VerticalScrollMod;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class InGameHudMixin {

    private static final int SLOT_SIZE = 20;
    private static final int GAP_OFF = 1;
    private static final int GAP_ON = 3;

    private static final int Z_PUSH_FORWARD = 8000;

    @Inject(method = "render", at = @At("TAIL"))
    private void verticalscroll_renderColumnHud(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.screen != null) return;
        if (!VerticalScrollMod.modifierKey.isDown()) return;

        Inventory inventory = client.player.getInventory();
        int hotbarIndex = ((PlayerInventoryAccessor) inventory).getSelectedSlot();

        int scaledWidth  = guiGraphics.guiWidth();
        int scaledHeight = guiGraphics.guiHeight();

        int slotX = scaledWidth / 2 - 90 + hotbarIndex * SLOT_SIZE;

        int[] inventorySlots = {
            hotbarIndex + 9,
            hotbarIndex + 18,
            hotbarIndex + 27,
        };

        int columnHeight = inventorySlots.length * SLOT_SIZE;
        int columnTop = scaledHeight - 22 - (VerticalScrollConfig.get().gapEnabled ? GAP_ON : GAP_OFF) - columnHeight;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, Z_PUSH_FORWARD);
        try {
            guiGraphics.fill(
                slotX - 1, columnTop - 1,
                slotX + SLOT_SIZE + 1, scaledHeight - 22 - (VerticalScrollConfig.get().gapEnabled ? GAP_ON : GAP_OFF),
                0xAA000000
            );

            for (int i = 0; i < inventorySlots.length; i++) {
                int slotY = columnTop + i * SLOT_SIZE;

                guiGraphics.fill(slotX,              slotY,              slotX + SLOT_SIZE,     slotY + 1,              0xFF373737);
                guiGraphics.fill(slotX,              slotY,              slotX + 1,             slotY + SLOT_SIZE,      0xFF373737);
                guiGraphics.fill(slotX,              slotY + SLOT_SIZE - 1, slotX + SLOT_SIZE, slotY + SLOT_SIZE,      0xFF8B8B8B);
                guiGraphics.fill(slotX + SLOT_SIZE - 1, slotY,          slotX + SLOT_SIZE,     slotY + SLOT_SIZE,      0xFF8B8B8B);

                guiGraphics.fill(slotX + 1, slotY + 1, slotX + SLOT_SIZE - 1, slotY + SLOT_SIZE - 1, 0xFF555555);

                ItemStack stack = inventory.getItem(inventorySlots[i]);
                guiGraphics.renderItem(stack, slotX + 2, slotY + 2);
                guiGraphics.renderItemDecorations(client.font, stack, slotX + 2, slotY + 2);
            }

            int arrowX = slotX - 9;

            guiGraphics.drawString(
                client.font,
                Component.literal("▲"),
                arrowX, columnTop + 6,
                0xFFFFFFFF, true
            );

            guiGraphics.drawString(
                client.font,
                Component.literal("▼"),
                arrowX, columnTop + 2 * SLOT_SIZE + 6,
                0xFFFFFFFF, true
            );
        } finally {
            guiGraphics.pose().popPose();
        }
    }
}
