package io.github.CrabK1ng.Proximity;

import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.ModInitializer;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.settings.ServerSettings;
import io.github.CrabK1ng.Proximity.networking.Client;
import io.github.CrabK1ng.Proximity.networking.Server;
import io.github.CrabK1ng.Proximity.networking.packets.ProxPacket;

public class ProximityInit implements ModInitializer {

    @Override
    public void onInit() {
        ProxPacket.register();

        if (com.github.puzzle.core.Constants.SIDE == EnvType.SERVER) {
            Thread thread = new Thread(() -> {
                try {
                    Server.start(47138);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, "Proximity-Server-Thread");
            thread.setDaemon(true);
            thread.start();
        }
        Constants.LOGGER.info("Proximity init");
    }
}
