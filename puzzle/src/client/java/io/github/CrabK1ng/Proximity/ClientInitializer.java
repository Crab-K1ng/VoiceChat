package io.github.CrabK1ng.Proximity;

import com.badlogic.gdx.utils.PauseableThread;
import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientModInitializer;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import io.github.CrabK1ng.Proximity.networking.ProximityPacket;
import io.github.CrabK1ng.Proximity.opus.OpusCodec;

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
        if (ClientNetworkManager.isConnected()) {
            OpusCodec codec = OpusCodec.createDefault();
            PauseableThread AudioCapture = Threads.createPauseableThread("AudioCaptureThread", () -> {
                try {
                    while (Proximity.lineOpen) {
                        byte[] buffer = new byte[codec.getChannels() * codec.getFrameSize() * 2];
                        microphone.read(buffer, 0, buffer.length);
                        byte[] encoded = codec.encodeFrame(buffer);
                        byte[] decoded = codec.decodeFrame(encoded);
                        Proximity.speakers.write(decoded, 0, buffer.length);
                        //if (encoded.length > 0) {
                            ProximityPacket ProximityPacket = new ProximityPacket(encoded.clone(),InGame.getLocalPlayer().getPosition(),InGame.getLocalPlayer().getUsername());
                            ClientNetworkManager.sendAsClient(ProximityPacket);
                        //}
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            AudioCapture.start();
        }
    }
}
