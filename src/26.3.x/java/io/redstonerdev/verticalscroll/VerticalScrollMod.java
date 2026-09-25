package io.redstonerdev.verticalscroll;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class VerticalScrollMod implements ClientModInitializer {

    public static KeyMapping modifierKey;
    public static KeyMapping scrollStepKey;

    @Override
    public void onInitializeClient() {
        VerticalScrollConfig.load();

        // One shared Category instance so both keys group under a single
        // "Vertical Scroll" section in Controls.
        KeyMapping.Category category =
                new KeyMapping.Category(Identifier.fromNamespaceAndPath("verticalscroll", "category"));

        modifierKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.verticalscroll.modifier",
                InputConstants.Type.KEYBOARD,
                GLFW.GLFW_KEY_LEFT_ALT,
                category
        ));

        // Unbound by default; users assign it under Controls -> Vertical Scroll.
        scrollStepKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.verticalscroll.scroll_step",
                InputConstants.Type.KEYBOARD,
                GLFW.GLFW_KEY_UNKNOWN,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (scrollStepKey.consumeClick()) {
                ColumnScroller.rotate(client, true);
            }
        });
    }
}
