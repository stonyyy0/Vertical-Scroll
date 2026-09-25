package io.redstonerdev.verticalscroll.mixin;

import io.redstonerdev.verticalscroll.ColumnScroller;
import io.redstonerdev.verticalscroll.VerticalScrollConfig;
import io.redstonerdev.verticalscroll.VerticalScrollMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public class MouseMixin {

    // Accumulated scroll distance, so a single item advance requires a full
    // "notch" of travel regardless of how many (possibly tiny) events the mouse sends.
    @Unique
    private static double verticalscroll_accumulated = 0.0;

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void verticalscroll_onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (vertical == 0) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;
        if (client.currentScreen != null) return;
        if (!VerticalScrollMod.modifierKey.isPressed()) return;

        ci.cancel();

        VerticalScrollConfig cfg = VerticalScrollConfig.get();
        double sensitivity = cfg.scrollSensitivity <= 0 ? 1.0 : cfg.scrollSensitivity;
        double threshold = 1.0 / sensitivity; // higher sensitivity -> smaller threshold

        double eff = vertical > 0 ? Math.abs(vertical) : -Math.abs(vertical);
        // Reset the accumulator when the user reverses direction.
        if ((eff > 0 && verticalscroll_accumulated < 0) || (eff < 0 && verticalscroll_accumulated > 0)) {
            verticalscroll_accumulated = 0;
        }
        verticalscroll_accumulated += eff;

        while (verticalscroll_accumulated >= threshold) {
            verticalscroll_accumulated -= threshold;
            ColumnScroller.rotate(client, true);
        }
        while (verticalscroll_accumulated <= -threshold) {
            verticalscroll_accumulated += threshold;
            ColumnScroller.rotate(client, false);
        }
    }
}
