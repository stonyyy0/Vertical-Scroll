package io.redstonerdev.verticalscroll;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class VerticalScrollConfigScreen extends Screen {

    private final Screen parent;

    public VerticalScrollConfigScreen(Screen parent) {
        super(Component.translatable("config.verticalscroll.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        VerticalScrollConfig cfg = VerticalScrollConfig.get();

        addRenderableWidget(Button.builder(gapText(cfg.gapEnabled), btn -> {
            cfg.gapEnabled = !cfg.gapEnabled;
            btn.setMessage(gapText(cfg.gapEnabled));
            VerticalScrollConfig.save();
        }).bounds(width / 2 - 100, height / 2 - 20, 200, 20).build());

        addRenderableWidget(Button.builder(sensitivityText(cfg), btn -> {
            cfg.scrollSensitivity = cycleSensitivity(cfg.scrollSensitivity);
            btn.setMessage(sensitivityText(cfg));
            VerticalScrollConfig.save();
        }).bounds(width / 2 - 100, height / 2 + 8, 200, 20).build());

        addRenderableWidget(Button.builder(
                Component.translatable("gui.done"),
                btn -> onClose()
        ).bounds(width / 2 - 100, height / 2 + 36, 200, 20).build());
    }

    private static Component gapText(boolean enabled) {
        return Component.translatable("config.verticalscroll.gap",
                Component.translatable(enabled ? "options.on" : "options.off"));
    }

    private static Component sensitivityText(VerticalScrollConfig cfg) {
        return Component.translatable("config.verticalscroll.sensitivity", String.valueOf(cfg.scrollSensitivity));
    }

    // 1.0 -> 0.75 -> 0.5 -> 0.35 -> 0.25 -> 0.15 -> 2.0 -> (wrap). Lower value = less sensitive.
    private static double cycleSensitivity(double v) {
        if (v > 1.5)  return 1.0;
        if (v > 0.85) return 0.75;
        if (v > 0.6)  return 0.5;
        if (v > 0.42) return 0.35;
        if (v > 0.30) return 0.25;
        if (v > 0.20) return 0.15;
        if (v > 0.10) return 2.0;
        return 1.0;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float partialTick) {
        extractor.centeredText(font, title, width / 2, height / 2 - 50, 0xFFFFFF);
        super.extractRenderState(extractor, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        assert this.minecraft != null;
        this.minecraft.setScreen(parent);
    }
}
