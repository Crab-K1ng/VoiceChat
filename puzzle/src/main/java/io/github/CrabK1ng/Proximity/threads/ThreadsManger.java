package io.github.CrabK1ng.Proximity.threads;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadsManger {
    public static SpeakersThread speakersThread;
    public static MicrophoneThread microphoneThread;

    public static void initSpeakersThread(){
        speakersThread = new SpeakersThread();
        Thread thread = new Thread(speakersThread);
        thread.start();
    }

    public static void initMicrophoneThread(){
        microphoneThread = new MicrophoneThread();
        Thread thread = new Thread(microphoneThread);
        thread.start();
    }
}
