package io.github.CrabK1ng.Proximity.threads;

import finalforeach.cosmicreach.GameSingletons;
import io.github.CrabK1ng.Proximity.AudioDevices.AudioDeviceManager;
import io.github.CrabK1ng.Proximity.audioFormat.AudioFormat;
import io.github.CrabK1ng.Proximity.networking.Client;
import io.github.CrabK1ng.Proximity.networking.packets.AudioPacket;
import io.github.CrabK1ng.Proximity.opus.OpusDecoderHandler;
import io.github.CrabK1ng.Proximity.opus.OpusEncoderHandler;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MicrophoneThread implements Runnable {
    byte[] byteBuffer = new byte[AudioFormat.getSamplesPerBuffer() * AudioFormat.getChannels()];
    OpusEncoderHandler encoder;
    public boolean isRunning;

    public MicrophoneThread() {
    }

    @Override
    public void run() {
        try{
            while (AudioDeviceManager.isMicrophoneOn()){
                isRunning = true;
                if (encoder == null){
                    try {
                        encoder = new OpusEncoderHandler(AudioFormat.getSampleRate(), AudioFormat.getChannels());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                // stop infinite loop
                int i = 0;
                while (i < 10000){
                    AudioDeviceManager.getMicrophone().read(byteBuffer, 0, byteBuffer.length);

                    byte[] opusBuffer = encoder.encode(byteBuffer);
                    if (Client.context != null){
                        try {
                            Client.send(new AudioPacket(GameSingletons.client().getAccount().getUniqueId(), opusBuffer, GameSingletons.client().getLocalPlayer().getPosition()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Thread.sleep(10);
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
