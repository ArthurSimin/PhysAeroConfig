package net.physaero.config.client.screen;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.physaero.config.config.AccountSystem;
import net.physaero.config.config.ModConfig;
import net.physaero.config.handler.AccountSystemHandler;
import net.physaero.config.handler.FancyMenuHandler;

public class YaclConfigScreen {

    public static Screen createScreen(Screen parent) {
        boolean currentKeepIntro = ModConfig.shouldKeepAnimatedIntro();
        AccountSystem currentAccountSystem = ModConfig.getAccountSystem();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("yacl.physaeroconfig.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("yacl.physaeroconfig.category.general"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("yacl.physaeroconfig.keep_intro"))
                                .description(OptionDescription.of(Component.translatable("yacl.physaeroconfig.keep_intro.desc")))
                                .binding(
                                        true,
                                        ModConfig::shouldKeepAnimatedIntro,
                                        value -> ModConfig.saveAll(true, value, ModConfig.getAccountSystem())
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<AccountSystem>createBuilder()
                                .name(Component.translatable("yacl.physaeroconfig.account_system"))
                                .description(OptionDescription.of(Component.translatable("yacl.physaeroconfig.account_system.desc")))
                                .binding(
                                        AccountSystem.E4ALL,
                                        ModConfig::getAccountSystem,
                                        value -> ModConfig.saveAll(ModConfig.isSetupCompleted(), ModConfig.shouldKeepAnimatedIntro(), value)
                                )
                                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(AccountSystem.class))
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("yacl.physaeroconfig.rerun_wizard"))
                                .description(OptionDescription.of(Component.translatable("yacl.physaeroconfig.rerun_wizard.desc")))
                                .binding(
                                        false,
                                        () -> !ModConfig.isSetupCompleted(),
                                        rerun -> {
                                            if (rerun) {
                                                ModConfig.saveAll(false, ModConfig.shouldKeepAnimatedIntro(), ModConfig.getAccountSystem());
                                            }
                                        }
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
                .save(() -> {
                    boolean newKeepIntro = ModConfig.shouldKeepAnimatedIntro();
                    AccountSystem newAccountSystem = ModConfig.getAccountSystem();

                    FancyMenuHandler.applyIntroSetting(newKeepIntro);
                    AccountSystemHandler.applyAccountSystem(newAccountSystem);

                    if (AccountSystemHandler.isRestartRequired()) {
                        net.physaero.config.client.NotificationOverlay.showRestartToast();
                    }
                })
                .build()
                .generateScreen(parent);
    }
}
