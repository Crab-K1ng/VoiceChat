package io.github.CrabK1ng.Proximity;

import com.badlogic.gdx.utils.PauseableThread;
import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientModInitializer;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import io.github.CrabK1ng.Proximity.Utils.Utils;
import io.github.CrabK1ng.Proximity.networking.AudioClientManager;
import io.github.CrabK1ng.Proximity.opus.OpusEncoderHandler;

import static finalforeach.cosmicreach.gamestates.GameState.currentGameState;
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
                OpusEncoderHandler encoder;
                try {
                    encoder = new OpusEncoderHandler(AudioSetting.getSampleRate(), AudioSetting.getChannels());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {


                    while (Proximity.lineOpen && ClientNetworkManager.isConnected()) {
                        byte[] byteBuffer = new byte[AudioSetting.getSamplesPerBuffer() * AudioSetting.getChannels()]; // 2 channels, 960 samples per channel, 2 bytes per sample
                        int bytesRead = microphone.read(byteBuffer, 0, byteBuffer.length);
                        Proximity.micLevel = Proximity.calculateVolumePercent(byteBuffer,bytesRead);
                        short[] buffer = Utils.bytesToShorts(byteBuffer);

                        byte[] encoded = encoder.encode(buffer);

                        Player player = InGame.getLocalPlayer();
                        if (encoded.length > 0 && currentGameState instanceof InGame && player != null) {
                            //ProximityPacket ProximityPacket = new ProximityPacket(encoded.clone(), player.getPosition(),player.getUsername());
                            //ClientNetworkManager.sendAsClient(ProximityPacket);
                            // Start sender
                            float x = player.getPosition().x;
                            float y = player.getPosition().y;
                            float z = player.getPosition().z;
                            AudioClientManager.sendAudio(player.getUsername(), x, y, z, encoded.clone());
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
