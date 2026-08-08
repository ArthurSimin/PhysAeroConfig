package net.physaero.config.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;

public class NotificationOverlay {

    public static void showRestartToast() {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.getToasts() != null) {
            SystemToast.add(
                    mc.getToasts(),
                    SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
                    Component.translatable("notification.physaeroconfig.restart_title"),
                    Component.translatable("notification.physaeroconfig.restart_desc")
            );
        }
    }
}
