package io.github.CrabK1ng.Proximity;

import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.ModInitializer;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.networking.GamePacket;
import io.github.CrabK1ng.Proximity.networking.ProximityPacket;

public class ProximityInit implements ModInitializer {

    @Override
    public void onInit() {
        Constants.LOGGER.info("Proximity init");
        GamePacket.registerPacket(ProximityPacket.class);
        if (GameSingletons.isHost) {
            new Thread(() -> {
                try {
                    new AudioRelayServer(9001).run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
