package io.redstonerdev.verticalscroll;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod(VerticalScrollMod.MOD_ID)
public class VerticalScrollMod {

    public static final String MOD_ID = "verticalscroll";
    public static KeyMapping modifierKey;

    public VerticalScrollMod(IEventBus modBus, ModContainer modContainer) {
        VerticalScrollConfig.load();
        modBus.addListener(this::onRegisterKeyMappings);
        modContainer.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (mc, parent) -> new VerticalScrollConfigScreen(parent))
        );
    }

    private void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        modifierKey = new KeyMapping(
                "key.verticalscroll.modifier",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "key.categories.verticalscroll"
        );
        event.register(modifierKey);
    }
}
