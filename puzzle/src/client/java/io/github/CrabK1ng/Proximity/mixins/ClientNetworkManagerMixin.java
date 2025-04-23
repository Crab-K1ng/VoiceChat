package io.github.CrabK1ng.Proximity.mixins;

import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import finalforeach.cosmicreach.networking.client.netty.NettyClient;
import finalforeach.cosmicreach.util.logging.Logger;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ClientNetworkManager.class)
public class ClientNetworkManagerMixin {

    @Inject(method = "connectToServer", at = @At("TAIL"))
    private static void connectToServer(String address, Runnable onConnect, Consumer<Throwable> onFailure, CallbackInfo ci) {
//        AudioClientManager.connectToServer(address);
    }
}
