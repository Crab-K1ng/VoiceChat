package io.github.CrabK1ng.Proximity;

import com.badlogic.gdx.utils.PauseableThread;
import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientModInitializer;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import io.github.CrabK1ng.Proximity.Utils.Utils;
import io.github.CrabK1ng.Proximity.networking.AudioClientReceiver;
import io.github.CrabK1ng.Proximity.networking.AudioClientSender;
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
            // Start receiver
            new Thread(() -> {
                try {
                    new AudioClientReceiver().start(9000); // Can be any free port
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();



            PauseableThread AudioCapture = Threads.createPauseableThread("AudioCaptureThread", () -> {


                int sampleRate = 8000; // Opus supports 8000, 12000, 16000, 24000, and 48000
                int channels = 1;       // Mono (1) or Stereo (2)
                int frameSize = 160;    // Typical frame size for 20ms at 48kHz
                OpusEncoderHandler encoder;
                try {
                    encoder = new OpusEncoderHandler(sampleRate, channels);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {


                    while (Proximity.lineOpen && ClientNetworkManager.isConnected()) {
                        byte[] byteBuffer = new byte[frameSize * 2]; // 2 channels, 960 samples per channel, 2 bytes per sample
                        int bytesRead = microphone.read(byteBuffer, 0, byteBuffer.length);
                        Proximity.micLevel = Proximity.calculateVolumePercent(byteBuffer,bytesRead);
//                        short[] buffer = new short[bytesRead / 2]; // Each short takes 2 bytes
//                        ByteBuffer.wrap(byteBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(buffer);

                        ///////////////////////////////////////////////
                        short[] buffer = Utils.bytesToShorts(byteBuffer);
                        ///////////////////////////////////////////////

                        byte[] encoded = encoder.encode(buffer);

                        Player player = InGame.getLocalPlayer();
                        if (encoded.length > 0 && currentGameState instanceof InGame && player != null) {
                            //ProximityPacket ProximityPacket = new ProximityPacket(encoded.clone(), player.getPosition(),player.getUsername());
                            //ClientNetworkManager.sendAsClient(ProximityPacket);
                            // Start sender
                            float x = player.getPosition().x;
                            float y = player.getPosition().y;
                            float z = player.getPosition().z;
                            AudioClientSender sender = new AudioClientSender("127.0.0.1", 9000); // Replace with server IP
                            sender.sendAudio(player.getUsername(), x, y, z, encoded.clone());
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
