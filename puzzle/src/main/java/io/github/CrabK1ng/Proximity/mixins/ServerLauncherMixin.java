package io.github.CrabK1ng.Proximity.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.TickRunner;
import finalforeach.cosmicreach.ZoneLoaders;
import finalforeach.cosmicreach.io.ChunkLoader;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.networking.netty.NettyServer;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import finalforeach.cosmicreach.server.ServerLauncher;
import finalforeach.cosmicreach.server.StartupHelper;
import finalforeach.cosmicreach.util.logging.Logger;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.worldgen.ZoneGenerator;
import io.github.CrabK1ng.Proximity.AudioRelayServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(ServerLauncher.class)
public class ServerLauncherMixin {

    @Inject(method = "main", at = @At("TAIL"))
    private static void main(String[] args, CallbackInfo ci) throws Exception {
        if (!StartupHelper.startNewJvmIfRequired()) {

            new Thread(() -> {
                try {
                    new AudioRelayServer(9000).run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
