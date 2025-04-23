package io.github.CrabK1ng.Proximity.AudioDevices;

import io.github.CrabK1ng.Proximity.audioFormat.AudioFormat;
import io.github.CrabK1ng.Proximity.threads.ThreadsManger;

import javax.sound.sampled.*;

public class AudioDeviceManager {
    private static SourceDataLine speakers;
    private static TargetDataLine microphone;
    private static boolean isMicrophoneOn;
    private static boolean isSpeakerOn;

    public static void init(){
        openMicrophone();
        openSpeaker();
        ThreadsManger.initSpeakersThread();
        ThreadsManger.initMicrophoneThread();
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

    public static boolean isIsMicrophoneOn() {
        return isMicrophoneOn;
    }

    public static boolean isIsSpeakerOn() {
        return isSpeakerOn;
    }
}
