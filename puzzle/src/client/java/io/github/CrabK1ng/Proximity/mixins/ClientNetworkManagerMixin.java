package io.github.CrabK1ng.Proximity.mixins;

import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import io.github.CrabK1ng.Proximity.networking.Client;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ClientNetworkManager.class)
public class ClientNetworkManagerMixin {

    @Inject(method = "connectToServer", at = @At("TAIL"))
    private static void connectToServer(String address, Runnable onConnect, Consumer<Throwable> onFailure, CallbackInfo ci) {
        String ip;
        int port;
        if (address.contains(":")) {
            String[] parts = address.split(":");
            ip = parts[0];
            port = Integer.parseInt(parts[1]) + 1;
        } else {
            ip = address;
            port = 47138;
        }

        try {
            Client.connect(ip, port);
        } catch (InterruptedException e) {
        }
    }
}
