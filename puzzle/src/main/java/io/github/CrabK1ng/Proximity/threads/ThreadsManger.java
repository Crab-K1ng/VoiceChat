package io.github.CrabK1ng.Proximity.threads;

import finalforeach.cosmicreach.Threads;
import io.github.CrabK1ng.Proximity.threads.MicrophoneThread;
import io.github.CrabK1ng.Proximity.threads.SpeakersThread;

public class ThreadsManger {
    public static Thread speakersThread;
    public static SpeakersThread speakersRunnable;
    public static Thread microphoneThread;
    public static MicrophoneThread microphoneRunnable;

    public static void initSpeakersThread(){
        speakersRunnable = new SpeakersThread();
        speakersThread = Threads.createThread("speakersThread", speakersRunnable);
    }

    public static void initMicrophoneThread(){
        microphoneRunnable = new MicrophoneThread();
        microphoneThread = Threads.createThread("microphoneThread", microphoneRunnable);
    }
}
