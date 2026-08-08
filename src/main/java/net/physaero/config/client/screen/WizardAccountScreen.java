package net.physaero.config.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.physaero.config.config.AccountSystem;
import net.physaero.config.config.ModConfig;
import net.physaero.config.handler.AccountSystemHandler;
import net.physaero.config.handler.FancyMenuHandler;

public class WizardAccountScreen extends Screen {
    private final Screen parentScreen;
    private final boolean keepIntro;
    private AccountSystem selectedSystem = AccountSystem.E4ALL;

    private Button e4allButton;
    private Button essentialsButton;

    public WizardAccountScreen(Screen parentScreen, boolean keepIntro) {
        super(Component.translatable("title.physaeroconfig.account"));
        this.parentScreen = parentScreen;
        this.keepIntro = keepIntro;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 2 - 20;

        int optionWidth = 280;
        int optionHeight = 24;

        this.e4allButton = this.addRenderableWidget(
                Button.builder(getOptionText(AccountSystem.E4ALL), button -> selectSystem(AccountSystem.E4ALL))
                        .bounds(centerX - optionWidth / 2, startY, optionWidth, optionHeight)
                        .build()
        );

        this.essentialsButton = this.addRenderableWidget(
                Button.builder(getOptionText(AccountSystem.ESSENTIALS), button -> selectSystem(AccountSystem.ESSENTIALS))
                        .bounds(centerX - optionWidth / 2, startY + 30, optionWidth, optionHeight)
                        .build()
        );

        this.addRenderableWidget(
                Button.builder(Component.translatable("button.physaeroconfig.confirm"), button -> finishSetup())
                        .bounds(centerX - 100, startY + 75, 200, 20)
                        .build()
        );

        updateButtonStates();
    }

    private void selectSystem(AccountSystem system) {
        this.selectedSystem = system;
        updateButtonStates();
    }

    private void updateButtonStates() {
        if (e4allButton != null && essentialsButton != null) {
            e4allButton.active = (selectedSystem != AccountSystem.E4ALL);
            essentialsButton.active = (selectedSystem != AccountSystem.ESSENTIALS);
        }
    }

    private Component getOptionText(AccountSystem system) {
        String key = system == AccountSystem.E4ALL ? "option.physaeroconfig.e4all" : "option.physaeroconfig.essentials";
        String prefix = selectedSystem == system ? "✓ " : "";
        return Component.literal(prefix).append(Component.translatable(key));
    }

    private void finishSetup() {
        // 1. Apply FancyMenu intro setting
        FancyMenuHandler.applyIntroSetting(this.keepIntro);

        // 2. Apply Account System mod file rename
        AccountSystemHandler.applyAccountSystem(this.selectedSystem);
        AccountSystemHandler.setRestartRequired(true);
        net.physaero.config.client.NotificationOverlay.showRestartToast();

        // 3. Save mod config with setup_completed = true
        ModConfig.saveAll(true, this.keepIntro, this.selectedSystem);

        // 4. Return to title screen
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parentScreen);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        guiGraphics.drawCenteredString(this.font, this.title, centerX, centerY - 75, 0xFFFFFF);
        guiGraphics.drawCenteredString(
                this.font,
                Component.translatable("question.physaeroconfig.account"),
                centerX,
                centerY - 50,
                0xAAAAAA
        );
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
