package io.github.CrabK1ng.Proximity;

import com.badlogic.gdx.utils.PauseableThread;
import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientModInitializer;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import io.github.CrabK1ng.Proximity.networking.ProximityPacket;

import static io.github.CrabK1ng.Proximity.Proximity.microphone;


public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInit() {
        Constants.LOGGER.info("Proximity initialised");
        if (GameSingletons.isClient){
           Proximity.openMicrophone();
           Proximity.openSpeaker();
        }

    }

    public static void captureAudio() {
        if (ClientNetworkManager.isConnected() && Proximity.lineOpen) {
            PauseableThread AudioCapture = Threads.createPauseableThread("AudioCaptureThread", () -> {
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int bytesRead = microphone.read(buffer, 0, buffer.length);
                        if (bytesRead > 0) {
                            ProximityPacket ProximityPacket = new ProximityPacket(buffer.clone());
                            ClientNetworkManager.sendAsClient(ProximityPacket);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            AudioCapture.start();
        }
    }
}
