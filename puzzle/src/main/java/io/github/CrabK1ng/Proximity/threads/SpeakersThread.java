package io.github.CrabK1ng.Proximity.threads;

import io.github.CrabK1ng.Proximity.AudioDevices.AudioDeviceManager;
import io.github.CrabK1ng.Proximity.audioFormat.AudioFormat;
import io.github.CrabK1ng.Proximity.opus.OpusDecoderHandler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SpeakersThread implements Runnable {
    private BlockingQueue<byte[]> audioQueue;
    OpusDecoderHandler decoder;
    public boolean isRunning;
    public static float spkLevel = 0;

    public SpeakersThread() {
        this.audioQueue = new ArrayBlockingQueue<>(20);
    }

    public SpeakersThread(BlockingQueue<byte[]> audioQueue) {
        this.audioQueue = audioQueue;
    }

    @Override
    public void run() {
        try{
            while (AudioDeviceManager.isSpeakerOn()){
                isRunning = true;
                if (decoder == null){
                    try {
                        decoder = new OpusDecoderHandler(AudioFormat.getSampleRate(), AudioFormat.getChannels());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                // stop infinite loop
                int i = 0;
                while (!audioQueue.isEmpty() && i < 10000){
                    byte[] buffer = audioQueue.take();

                    byte[] opusBuffer = decoder.decode(buffer);

                    AudioDeviceManager.applyVolume(opusBuffer, AudioDeviceManager.spkVolume.getValueAsFloat());
                    spkLevel = AudioDeviceManager.computeLevel(opusBuffer);
                    AudioDeviceManager.getSpeakers().write(opusBuffer, 0, opusBuffer.length);
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

    public void add(byte[] buffer){
        audioQueue.add(buffer);
    }
}
