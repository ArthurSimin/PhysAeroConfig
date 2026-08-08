package net.physaero.config;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.physaero.config.client.screen.YaclConfigScreen;
import net.physaero.config.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(PhysAeroConfig.MOD_ID)
public class PhysAeroConfig {
    public static final String MOD_ID = "physaeroconfig";
    public static final Logger LOGGER = LoggerFactory.getLogger(PhysAeroConfig.class);

    public PhysAeroConfig(ModContainer container) {
        LOGGER.info("[PhysAeroConfig] Initializing PhysAero Config Mod...");

        // Register client configuration spec
        container.registerConfig(Type.CLIENT, ModConfig.CLIENT_SPEC);

        // Register config screen factory for client side (YACL if available, fallback to Wizard)
        if (FMLEnvironment.dist == Dist.CLIENT) {
            container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, parentScreen) -> {
                if (net.neoforged.fml.ModList.get().isLoaded("yet_another_config_lib_v3") || 
                    net.neoforged.fml.ModList.get().isLoaded("yacl")) {
                    try {
                        return YaclConfigScreen.createScreen(parentScreen);
                    } catch (Throwable t) {
                        LOGGER.warn("[PhysAeroConfig] YACL screen failed to instantiate: {}", t.getMessage());
                    }
                }
                // Fallback if YACL is not installed in the modpack
                return new net.physaero.config.client.screen.WizardIntroScreen(parentScreen);
            });
        }
    }
}
