package io.redstonerdev.verticalscroll;

import net.minecraft.client.gui.GuiGraphics;
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

        addRenderableWidget(Button.builder(invertScrollText(cfg.invertedScroll), btn -> {
            cfg.invertedScroll = !cfg.invertedScroll;
            btn.setMessage(invertScrollText(cfg.invertedScroll));
            VerticalScrollConfig.save();
        }).bounds(width / 2 - 100, height / 2 - 48, 200, 20).build());

        addRenderableWidget(Button.builder(gapText(cfg.gapEnabled), btn -> {
            cfg.gapEnabled = !cfg.gapEnabled;
            btn.setMessage(gapText(cfg.gapEnabled));
            VerticalScrollConfig.save();
        }).bounds(width / 2 - 100, height / 2 - 20, 200, 20).build());

        addRenderableWidget(Button.builder(
                Component.translatable("gui.done"),
                btn -> onClose()
        ).bounds(width / 2 - 100, height / 2 + 8, 200, 20).build());
    }

    private static Component invertScrollText(boolean enabled) {
        return Component.translatable("config.verticalscroll.invert_scroll",
                Component.translatable(enabled ? "options.on" : "options.off"));
    }

    private static Component gapText(boolean enabled) {
        return Component.translatable("config.verticalscroll.gap",
                Component.translatable(enabled ? "options.on" : "options.off"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.drawCenteredString(font, title, width / 2, height / 2 - 50, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        assert this.minecraft != null;
        this.minecraft.setScreen(parent);
    }
}
