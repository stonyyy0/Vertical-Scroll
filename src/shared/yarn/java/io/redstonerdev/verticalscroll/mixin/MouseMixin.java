package io.redstonerdev.verticalscroll.mixin;

import io.redstonerdev.verticalscroll.VerticalScrollMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void verticalscroll_onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (vertical == 0) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        if (client.currentScreen != null) return;

        if (!VerticalScrollMod.modifierKey.isPressed()) return;
        ci.cancel();

        rotateColumn(client, vertical > 0);
    }

    private static void rotateColumn(MinecraftClient client, boolean scrollUp) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.interactionManager == null) return;

        PlayerInventory inventory = player.getInventory();
        int hotbarIndex = ((PlayerInventoryAccessor) inventory).getSelectedSlot();

        int topRowSlot = 9  + hotbarIndex;
        int midRowSlot = 18 + hotbarIndex;
        int botRowSlot = 27 + hotbarIndex;
        PlayerScreenHandler handler = player.playerScreenHandler;
        int syncId = handler.syncId;

        int[] rows = scrollUp
                ? new int[]{botRowSlot, midRowSlot, topRowSlot}
                : new int[]{topRowSlot, midRowSlot, botRowSlot};
        for (int slot : rows) {
            client.interactionManager.clickSlot(syncId, slot, hotbarIndex, SlotActionType.SWAP, player);
        }
    }
}
