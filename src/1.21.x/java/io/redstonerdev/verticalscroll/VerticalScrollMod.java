package io.redstonerdev.verticalscroll;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class VerticalScrollMod implements ClientModInitializer {

    public static KeyBinding modifierKey;
    public static KeyBinding scrollStepKey;

    @Override
    public void onInitializeClient() {
        VerticalScrollConfig.load();

        // One shared Category instance so both keys group under a single
        // "Vertical Scroll" section in Controls.
        KeyBinding.Category category = KeyBinding.Category.create(Identifier.of("verticalscroll", "category"));

        modifierKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.verticalscroll.modifier",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                category
        ));

        // Unbound by default; users assign it under Controls -> Vertical Scroll.
        scrollStepKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.verticalscroll.scroll_step",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (scrollStepKey.wasPressed()) {
                ColumnScroller.rotate(client, true);
            }
        });
    }
}
