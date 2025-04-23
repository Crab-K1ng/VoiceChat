package io.github.CrabK1ng.Proximity.threads;

import finalforeach.cosmicreach.Threads;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadsManger {
    public static SpeakersThread speakersThread;
    public static MicrophoneThread microphoneThread;

    public static void initSpeakersThread(){
        speakersThread = new SpeakersThread();
        Threads.createThread("speakersThread", speakersThread);
    }

    public static void initMicrophoneThread(){
        microphoneThread = new MicrophoneThread();
        Threads.createThread("microphoneThread", microphoneThread);
    }
}
