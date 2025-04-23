package io.github.CrabK1ng.Proximity;

import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.ModInitializer;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.settings.ServerSettings;

public class ProximityInit implements ModInitializer {

    @Override
    public void onInit() {
        Constants.LOGGER.info("Proximity init");
    }
}
