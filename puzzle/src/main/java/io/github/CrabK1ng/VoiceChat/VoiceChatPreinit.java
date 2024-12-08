package io.github.CrabK1ng.VoiceChat;


import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.PreModInitializer;

public class VoiceChatPreinit implements PreModInitializer {

    @Override
    public void onPreInit() {
        Constants.LOGGER.info("Hello From PRE-INIT");
    }
}
