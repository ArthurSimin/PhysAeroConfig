package net.physaero.config.client;

import net.minecraft.client.gui.screens.TitleScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.physaero.config.config.ModConfig;
import net.physaero.config.client.screen.WizardIntroScreen;

@EventBusSubscriber(modid = "physaeroconfig", value = Dist.CLIENT)
public class ClientSetupEvents {

    @SubscribeEvent
    public static void onScreenOpening(ScreenEvent.Opening event) {
        if (event.getScreen() instanceof TitleScreen && !ModConfig.isSetupCompleted()) {
            event.setNewScreen(new WizardIntroScreen(event.getScreen()));
        }
    }
}
