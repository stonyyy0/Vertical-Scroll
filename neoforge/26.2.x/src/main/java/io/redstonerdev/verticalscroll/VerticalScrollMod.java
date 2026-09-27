package io.redstonerdev.verticalscroll;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.lwjgl.glfw.GLFW;

@Mod(value = VerticalScrollMod.MOD_ID, dist = Dist.CLIENT)
public class VerticalScrollMod {

    public static final String MOD_ID = "verticalscroll";
    public static KeyMapping modifierKey;

    public VerticalScrollMod(IEventBus modEventBus, ModContainer modContainer) {
        VerticalScrollConfig.load();
        modEventBus.addListener(this::onRegisterKeyMappings);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class,
                (mc, parent) -> new VerticalScrollConfigScreen(parent));
    }

    private void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        modifierKey = new KeyMapping(
                "key.verticalscroll.modifier",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                new KeyMapping.Category(Identifier.fromNamespaceAndPath("verticalscroll", "category"))
        );
        event.register(modifierKey);
    }
}
