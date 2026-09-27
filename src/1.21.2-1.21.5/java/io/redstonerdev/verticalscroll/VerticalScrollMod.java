package io.redstonerdev.verticalscroll;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class VerticalScrollMod implements ClientModInitializer {

    public static KeyBinding modifierKey;

    @Override
    public void onInitializeClient() {
        VerticalScrollConfig.load();

        modifierKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.verticalscroll.modifier",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "key.categories.verticalscroll"
        ));
    }
}
