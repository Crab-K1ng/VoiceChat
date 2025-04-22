package io.github.CrabK1ng.Proximity;

import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.PreModInitializer;

public class ProximityPreInit implements PreModInitializer {

    @Override
    public void onPreInit() {
        Constants.LOGGER.info("Proximity pre-init");
        AudioSetting.init();
    }
}
