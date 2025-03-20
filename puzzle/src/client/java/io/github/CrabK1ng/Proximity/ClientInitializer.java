package io.github.CrabK1ng.Proximity;

import com.badlogic.gdx.utils.PauseableThread;
import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientModInitializer;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import io.github.CrabK1ng.Proximity.networking.ProximityPacket;
import io.github.CrabK1ng.Proximity.opus.OpusEncoderHandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
            PauseableThread AudioCapture = Threads.createPauseableThread("AudioCaptureThread", () -> {
                int sampleRate = 8000; // Opus supports 8000, 12000, 16000, 24000, and 48000
                int channels = 1;       // Mono (1) or Stereo (2)
                int frameSize = 160;    // Typical frame size for 20ms at 48kHz
                OpusEncoderHandler encoder = null;
                try {
                    encoder = new OpusEncoderHandler(sampleRate, channels);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    while (Proximity.lineOpen && ClientNetworkManager.isConnected()) {
                        byte[] byteBuffer = new byte[frameSize * 2]; // 2 channels, 960 samples per channel, 2 bytes per sample
                        int bytesRead = microphone.read(byteBuffer, 0, byteBuffer.length);
                        short[] buffer = new short[bytesRead / 2]; // Each short takes 2 bytes
                        ByteBuffer.wrap(byteBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(buffer);

                        byte[] encoded = encoder.encode(buffer);
                        if (encoded.length > 0) {
                            ProximityPacket ProximityPacket = new ProximityPacket(encoded.clone(), InGame.getLocalPlayer().getPosition(),InGame.getLocalPlayer().getUsername());
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
