package net.physaero.config.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "de.keksuccino.fancymenu.customization.gameintro.GameIntroOverlay", remap = false)
public abstract class GameIntroOverlayMixin {
    @Unique
    private static final SoundEvent PHYSAEROCONFIG$INTRO_EVENT = SoundEvent.createVariableRangeEvent(
            ResourceLocation.fromNamespaceAndPath("physaeroconfig", "reimagined_intro"));

    @Unique
    private SimpleSoundInstance physaeroconfig$introSound;

    @Inject(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lde/keksuccino/fancymenu/util/resource/PlayableResource;play()V",
                    shift = At.Shift.BEFORE,
                    remap = false))
    private void physaeroconfig$playIntroSound(CallbackInfo ci) {
        if (physaeroconfig$introSound != null) {
            return;
        }

        physaeroconfig$introSound = SimpleSoundInstance.forUI(PHYSAEROCONFIG$INTRO_EVENT, 1.0F);
        Minecraft.getInstance().getSoundManager().play(physaeroconfig$introSound);
    }

    @Inject(method = "close()V", at = @At("HEAD"), remap = false)
    private void physaeroconfig$stopSoundOnClose(CallbackInfo ci) {
        physaeroconfig$stopIntroSound();
    }

    @Inject(method = "onReplaced()V", at = @At("HEAD"), remap = false)
    private void physaeroconfig$stopSoundOnReplacement(CallbackInfo ci) {
        physaeroconfig$stopIntroSound();
    }

    @Unique
    private void physaeroconfig$stopIntroSound() {
        if (physaeroconfig$introSound != null) {
            Minecraft.getInstance().getSoundManager().stop(physaeroconfig$introSound);
            physaeroconfig$introSound = null;
        }
    }
}
