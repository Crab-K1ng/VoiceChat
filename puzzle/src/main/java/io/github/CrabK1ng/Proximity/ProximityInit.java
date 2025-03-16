package io.github.CrabK1ng.Proximity;

import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.ModInitializer;
import finalforeach.cosmicreach.networking.GamePacket;
import io.github.CrabK1ng.Proximity.networking.ProximityPacket;

public class ProximityInit implements ModInitializer {

    @Override
    public void onInit() {
        Constants.LOGGER.info("Hello From INIT");
        GamePacket.registerPacket(ProximityPacket.class);
    }
}
