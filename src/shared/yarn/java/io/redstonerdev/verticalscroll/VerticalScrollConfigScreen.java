package io.redstonerdev.verticalscroll;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class VerticalScrollConfigScreen extends Screen {

    private final Screen parent;

    public VerticalScrollConfigScreen(Screen parent) {
        super(Text.translatable("config.verticalscroll.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        VerticalScrollConfig cfg = VerticalScrollConfig.get();

        addDrawableChild(ButtonWidget.builder(gapText(cfg.gapEnabled), btn -> {
            cfg.gapEnabled = !cfg.gapEnabled;
            btn.setMessage(gapText(cfg.gapEnabled));
            VerticalScrollConfig.save();
        }).dimensions(width / 2 - 100, height / 2 - 20, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.done"),
                btn -> close()
        ).dimensions(width / 2 - 100, height / 2 + 8, 200, 20).build());
    }

    private static Text gapText(boolean enabled) {
        return Text.translatable("config.verticalscroll.gap",
                Text.translatable(enabled ? "options.on" : "options.off"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 50, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }
}
