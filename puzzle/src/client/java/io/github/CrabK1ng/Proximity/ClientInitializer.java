package io.github.CrabK1ng.Proximity;

import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientModInitializer;
import io.github.CrabK1ng.Proximity.networking.AudioClientManager;

import static io.github.CrabK1ng.Proximity.Proximity.microphone;


public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInit() {
        Constants.LOGGER.info("Proximity initialised");
    }

}
