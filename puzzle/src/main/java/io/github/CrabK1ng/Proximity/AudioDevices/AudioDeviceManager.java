package io.github.CrabK1ng.Proximity.AudioDevices;

import finalforeach.cosmicreach.settings.INumberSetting;
import io.github.CrabK1ng.Proximity.audioFormat.AudioFormat;
import io.github.CrabK1ng.Proximity.threads.ThreadsManger;

import javax.sound.sampled.*;

public class AudioDeviceManager {
    private static float micVolumeFloat = 1f;
    public static INumberSetting micVolume = new INumberSetting() {
        @Override
        public float getValueAsFloat() {
            return micVolumeFloat;
        }

        @Override
        public void setValue(float v) {
            micVolumeFloat = v;
        }
    };

    private static float spkVolumeFloat = 1f;
    public static INumberSetting spkVolume = new INumberSetting() {
        @Override
        public float getValueAsFloat() {
            return spkVolumeFloat;
        }

        @Override
        public void setValue(float v) {
            spkVolumeFloat = v;
        }
    };

    public static void applyVolume(byte[] audio, float volume) {
        for (int i = 0; i < audio.length; i += 2) {
            short sample = (short) ((audio[i + 1] << 8) | (audio[i] & 0xFF));
            int scaledSample = (int) (sample * volume);

            // Clamp to 16-bit range to avoid overflow distortion
            if (scaledSample > Short.MAX_VALUE) scaledSample = Short.MAX_VALUE;
            if (scaledSample < Short.MIN_VALUE) scaledSample = Short.MIN_VALUE;

            audio[i] = (byte) (scaledSample & 0xFF);
            audio[i + 1] = (byte) ((scaledSample >> 8) & 0xFF);
        }
    }

    public static float computeLevel(byte[] pcmBytes) {
        int sampleCount = pcmBytes.length / 2;
        double sum = 0.0;

        for (int i = 0; i < pcmBytes.length - 1; i += 2) {
            // Little-endian: LSB first
            short sample = (short)((pcmBytes[i + 1] << 8) | (pcmBytes[i] & 0xFF));
            sum += sample * sample;
        }

        float rms = (float)Math.sqrt(sum / sampleCount);
        float db = 20f * (float)Math.log10(rms / 32768f + 1e-6f); // dBFS with epsilon
        db = Math.max(-60f, Math.min(0f, db)); // clamp between -60dB and 0dB

        float normalized = (db + 60f) / 60f; // normalize to 0.0 - 1.0
        return (float)Math.pow(normalized, 1.5); // perceptual curve
    }

    private static SourceDataLine speakers;
    private static TargetDataLine microphone;
    private static boolean isMicrophoneOn;
    private static boolean isSpeakerOn;

    public static void init(){
        openMicrophone();
        openSpeaker();
        ThreadsManger.initMicrophoneThread();
        ThreadsManger.initSpeakersThread();
    }

    public static void toggleMic() {
        isMicrophoneOn = !isMicrophoneOn;
        if (isMicrophoneOn) microphone.start();
        if (!isMicrophoneOn) microphone.stop();
    }

    public static void toggleSpeaker() {
        isSpeakerOn = !isSpeakerOn;
        if (isSpeakerOn) speakers.start();
        if (!isSpeakerOn) speakers.stop();
    }

    public static void openMicrophone(){
        try {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, AudioFormat.getFormat());

            if (!AudioSystem.isLineSupported(info)) {
                throw new LineUnavailableException("Line not supported");
            }

            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(AudioFormat.getFormat());
            microphone.start();
            isMicrophoneOn = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openSpeaker(){
        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, AudioFormat.getFormat());

            if (!AudioSystem.isLineSupported(info)) {
                throw new LineUnavailableException("Line not supported");
            }

            speakers = (SourceDataLine) AudioSystem.getLine(info);
            speakers.open(AudioFormat.getFormat());
            speakers.start();
            isSpeakerOn = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SourceDataLine getSpeakers() {
        return speakers;
    }

    public static TargetDataLine getMicrophone() {
        return microphone;
    }

    public static boolean isMicrophoneOn() {
        return isMicrophoneOn;
    }

    public static boolean isSpeakerOn() {
        return isSpeakerOn;
    }
}
