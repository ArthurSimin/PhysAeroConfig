package net.physaero.config.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WizardIntroScreen extends Screen {
    private final Screen parentScreen;

    public WizardIntroScreen(Screen parentScreen) {
        super(Component.translatable("title.physaeroconfig.intro"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 2;

        int buttonWidth = 120;
        int buttonHeight = 20;

        this.addRenderableWidget(
                Button.builder(Component.translatable("button.physaeroconfig.yes"), button -> onSelected(true))
                        .bounds(centerX - buttonWidth - 10, startY + 10, buttonWidth, buttonHeight)
                        .build()
        );

        this.addRenderableWidget(
                Button.builder(Component.translatable("button.physaeroconfig.no"), button -> onSelected(false))
                        .bounds(centerX + 10, startY + 10, buttonWidth, buttonHeight)
                        .build()
        );
    }

    private void onSelected(boolean keepIntro) {
        if (this.minecraft != null) {
            this.minecraft.setScreen(new WizardAccountScreen(this.parentScreen, keepIntro));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        guiGraphics.drawCenteredString(this.font, this.title, centerX, centerY - 60, 0xFFFFFF);
        guiGraphics.drawCenteredString(
                this.font,
                Component.translatable("question.physaeroconfig.intro"),
                centerX,
                centerY - 30,
                0xAAAAAA
        );
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
