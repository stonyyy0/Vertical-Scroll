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

        addRenderableWidget(Button.builder(translatableToggle("config.verticalscroll.invert_scroll", cfg.invertedScroll), btn -> {
            cfg.invertedScroll = !cfg.invertedScroll;
            btn.setMessage(translatableToggle("config.verticalscroll.invert_scroll", cfg.invertedScroll));
            VerticalScrollConfig.save();
        }).bounds(width / 2 - 100, height / 2 - 48, 200, 20).build());

        addRenderableWidget(Button.builder(translatableToggle("config.verticalscroll.gap", cfg.gapEnabled), btn -> {
            cfg.gapEnabled = !cfg.gapEnabled;
            btn.setMessage(translatableToggle("config.verticalscroll.gap", cfg.gapEnabled));
            VerticalScrollConfig.save();
        }).bounds(width / 2 - 100, height / 2 - 20, 200, 20).build());

        addRenderableWidget(Button.builder(translatableToggle("config.verticalscroll.hide.hud", cfg.hideHud), btn -> {
            cfg.hideHud = !cfg.hideHud;
            btn.setMessage(translatableToggle("config.verticalscroll.hide.hud", cfg.hideHud));
            VerticalScrollConfig.save();
        }).bounds(width / 2 - 100, height / 2 + 8, 200, 20).build());

        addRenderableWidget(Button.builder(
                Component.translatable("gui.done"),
                btn -> onClose()
        ).bounds(width / 2 - 100, height / 2 + 36, 200, 20).build());
    }

    private static Component translatableToggle(String path, boolean enabled) {
        return Component.translatable(path,
                Component.translatable(enabled ? "options.on" : "options.off"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float partialTick) {
        extractor.centeredText(font, title, width / 2, height / 2 - 50, 0xFFFFFF);
        super.extractRenderState(extractor, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        assert this.minecraft != null;
        this.minecraft.setScreenAndShow(parent);
    }
}
