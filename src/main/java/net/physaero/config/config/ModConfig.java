package net.physaero.config.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfig {
    public static final ModConfigSpec CLIENT_SPEC;

    public static final ModConfigSpec.BooleanValue SETUP_COMPLETED;
    public static final ModConfigSpec.BooleanValue KEEP_ANIMATED_INTRO;
    public static final ModConfigSpec.EnumValue<AccountSystem> ACCOUNT_SYSTEM;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("PhysAero Modpack Configuration Settings").push("general");

        SETUP_COMPLETED = builder
                .comment("Whether the first-launch setup wizard has been completed.")
                .define("setup_completed", false);

        KEEP_ANIMATED_INTRO = builder
                .comment("Whether to keep FancyMenu's animated intro (true = keep, false = skip intro).")
                .define("keep_animated_intro", true);

        ACCOUNT_SYSTEM = builder
                .comment("The selected account system mod (E4ALL or ESSENTIALS). Default is E4ALL.")
                .defineEnum("account_system", AccountSystem.E4ALL);

        builder.pop();
        CLIENT_SPEC = builder.build();
    }

    public static boolean isSetupCompleted() {
        return SETUP_COMPLETED.get();
    }

    public static boolean shouldKeepAnimatedIntro() {
        return KEEP_ANIMATED_INTRO.get();
    }

    public static AccountSystem getAccountSystem() {
        return ACCOUNT_SYSTEM.get();
    }

    public static void saveAll(boolean setupCompleted, boolean keepIntro, AccountSystem system) {
        SETUP_COMPLETED.set(setupCompleted);
        KEEP_ANIMATED_INTRO.set(keepIntro);
        ACCOUNT_SYSTEM.set(system);
        CLIENT_SPEC.save();
    }
}
