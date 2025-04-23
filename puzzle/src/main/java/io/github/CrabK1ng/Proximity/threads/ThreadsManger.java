package io.github.CrabK1ng.Proximity.threads;

public class ThreadsManger {
    public static SpeakersThread speakersThread;
    public static MicrophoneThread microphoneThread;

    public static void initSpeakersThread(){
        speakersThread = new SpeakersThread();
        speakersThread.run();
    }

    public static void initMicrophoneThread(){
        microphoneThread = new MicrophoneThread();
        microphoneThread.run();
    }
}
