package io.redstonerdev.verticalscroll.mixin;

import io.redstonerdev.verticalscroll.VerticalScrollConfig;
import io.redstonerdev.verticalscroll.VerticalScrollMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {

    private static final int SLOT_SIZE = 20;
    private static final int GAP_OFF = 1;
    private static final int GAP_ON = 3;

    private static final int Z_PUSH_FORWARD = 8192;

    @Inject(method = "render", at = @At("TAIL"))
    private void verticalscroll_renderColumnHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.currentScreen != null) return;
        if (!VerticalScrollMod.modifierKey.isPressed()) return;

        PlayerInventory inventory = client.player.getInventory();
        int hotbarIndex = ((PlayerInventoryAccessor) inventory).getSelectedSlot();

        int scaledWidth  = client.getWindow().getScaledWidth();
        int scaledHeight = client.getWindow().getScaledHeight();

        int slotX = scaledWidth / 2 - 90 + hotbarIndex * SLOT_SIZE;

        int[] inventorySlots = {
            hotbarIndex + 9,
            hotbarIndex + 18,
            hotbarIndex + 27,
        };

        int columnHeight = inventorySlots.length * SLOT_SIZE;
        int columnTop = scaledHeight - 22 - (VerticalScrollConfig.get().gapEnabled ? GAP_ON : GAP_OFF) - columnHeight;

        context.getMatrices().push();
        context.getMatrices().translate(0, 0, Z_PUSH_FORWARD);
        try {
            context.fill(
                slotX - 1, columnTop - 1,
                slotX + SLOT_SIZE + 1, scaledHeight - 22 - (VerticalScrollConfig.get().gapEnabled ? GAP_ON : GAP_OFF),
                0xAA000000
            );

            for (int i = 0; i < inventorySlots.length; i++) {
                int slotY = columnTop + i * SLOT_SIZE;

                context.fill(slotX,              slotY,              slotX + SLOT_SIZE,     slotY + 1,              0xFF373737);
                context.fill(slotX,              slotY,              slotX + 1,             slotY + SLOT_SIZE,      0xFF373737);
                context.fill(slotX,              slotY + SLOT_SIZE - 1, slotX + SLOT_SIZE, slotY + SLOT_SIZE,      0xFF8B8B8B);
                context.fill(slotX + SLOT_SIZE - 1, slotY,          slotX + SLOT_SIZE,     slotY + SLOT_SIZE,      0xFF8B8B8B);

                context.fill(slotX + 1, slotY + 1, slotX + SLOT_SIZE - 1, slotY + SLOT_SIZE - 1, 0xFF555555);

                ItemStack stack = inventory.getStack(inventorySlots[i]);
                context.drawItem(stack, slotX + 2, slotY + 2);
                context.drawStackOverlay(client.textRenderer, stack, slotX + 2, slotY + 2);
            }

            int arrowX = slotX - 9;

            context.drawText(
                client.textRenderer,
                Text.literal("▲"),
                arrowX, columnTop + 6,
                0xFFFFFFFF, true
            );

            context.drawText(
                client.textRenderer,
                Text.literal("▼"),
                arrowX, columnTop + 2 * SLOT_SIZE + 6,
                0xFFFFFFFF, true
            );
        } finally {
            context.getMatrices().pop();
        }
    }
}
