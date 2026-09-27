package io.redstonerdev.verticalscroll;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class VerticalScrollMod implements ClientModInitializer {

    public static KeyMapping modifierKey;

    @Override
    public void onInitializeClient() {
        VerticalScrollConfig.load();

        modifierKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.verticalscroll.modifier",
                InputConstants.Type.KEYBOARD,
                226,
                new KeyMapping.Category(
                        Identifier.fromNamespaceAndPath("verticalscroll", "category")
                )
        ));
    }
}