package io.github.CrabK1ng.Proximity.threads;

import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.GameSingletons;
import io.github.CrabK1ng.Proximity.AudioDevices.AudioDeviceManager;
import io.github.CrabK1ng.Proximity.Constants;
import io.github.CrabK1ng.Proximity.audioFormat.AudioFormat;
import io.github.CrabK1ng.Proximity.networking.Client;
import io.github.CrabK1ng.Proximity.networking.packets.AudioPacket;
import io.github.CrabK1ng.Proximity.opus.OpusEncoderHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentSkipListMap;

public class MicrophoneThread implements Runnable {
    byte[] byteBuffer = new byte[AudioFormat.getSamplesPerBuffer() * AudioFormat.getChannels()];
    OpusEncoderHandler encoder;
    public boolean isRunning;

    public static float micLevel = 0;

    public MicrophoneThread() {
    }

    @Override
    public void run() {
        Constants.LOGGER.info("Microphone thread started");
        try{
            while (AudioDeviceManager.isMicrophoneOn()){
                Constants.LOGGER.info("Mic is on");
                isRunning = true;
                if (encoder == null){
                    try {
                        Constants.LOGGER.info("Starting Opus decoder");
                        encoder = new OpusEncoderHandler(AudioFormat.getSampleRate(), AudioFormat.getChannels());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                // stop infinite loop
                int i = 0;
                while (i < 10000){
                    Constants.LOGGER.info("Reading mic data");
                    AudioDeviceManager.getMicrophone().read(byteBuffer, 0, byteBuffer.length);
                    Constants.LOGGER.info("Applying volume");
                    AudioDeviceManager.applyVolume(byteBuffer, AudioDeviceManager.micVolume.getValueAsFloat());
                    Constants.LOGGER.info("Computing mic level");
                    micLevel = AudioDeviceManager.computeMicLevel(byteBuffer);

                    Constants.LOGGER.info("Encoding data");
                    byte[] opusBuffer = encoder.encode(byteBuffer);
                    if (Client.context != null){
                        try {
                            Constants.LOGGER.info("Sending packet");
                            Client.send(new AudioPacket(GameSingletons.client().getAccount().getUniqueId(), opusBuffer.clone(), new Vector3()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    i++;
                }
                i = 0;
                isRunning = false;
                Thread.sleep(0, 10000);
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
