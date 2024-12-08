package io.github.CrabK1ng.VoiceChat;

import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.PauseableThread;
import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientModInitializer;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import io.github.CrabK1ng.VoiceChat.networking.VoiceChatPacket;

import java.nio.ShortBuffer;

import static io.github.CrabK1ng.VoiceChat.VoiceChat.microphone;

public class ClientInitializer implements ClientModInitializer {

    @Override
    public void onInit() {
        Constants.LOGGER.info("Hello From INIT");
        if (GameSingletons.isClient){
            VoiceChat.openMicrophone();
            VoiceChat.openSpeaker();
        }

    }

    public static void captureAudio() {
        PauseableThread AudioCapture = Threads.createPauseableThread("AudioCaptureThread", () -> {
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int bytesRead = microphone.read(buffer, 0, buffer.length);
                    if (bytesRead > 0) {
                        VoiceChatPacket VoiceChatPacket = new VoiceChatPacket(buffer.clone());
                        ClientNetworkManager.sendAsClient(VoiceChatPacket);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        AudioCapture.start();
    }

}
