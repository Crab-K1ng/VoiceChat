package io.github.CrabK1ng.Proximity.networking;

import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.util.logging.Logger;

public class AudioClientManager {
    public static int port = 9000;
    public static AudioClientSender audioClientSender;
    public static AudioClientReceiver audioClientReceiver;

    static Thread audioClientSenderThread;
    static Thread audioClientReceiverThread;

    public static boolean isConnected() {
        return audioClientSender != null && audioClientReceiver != null;
    }

    public static void connectToServer(String address) {
        Logger.info("Connecting to server at " + address);
        String s;
//        int port;
        if (address.contains(":")) {
            String[] astring = address.split(":");
            s = astring[0];
//            port = Integer.parseInt(astring[1]);
        } else {
            s = address;
//            port = 47137;
        }

        if (isConnected()) {
            try {
                //TODO close the connected if connecting to a differnt server.
            } catch (Exception exception1) {
                exception1.printStackTrace();
            }
        }

        try {
            audioClientSenderThread = Threads.createThread("audioClientSenderThread", () -> {
                try {
                    audioClientSender = new AudioClientSender(s, port);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            audioClientSenderThread.start();

            audioClientReceiverThread = Threads.createThread("audioClientReceiverThread", () -> {
                try {
                    audioClientReceiver = new AudioClientReceiver();
                    audioClientReceiver.start(port);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            audioClientReceiverThread.start();
        } catch (Exception exception) {
            Logger.error("Failed to connect to server");
            throw new RuntimeException(exception);
        }
    }

    public static void sendAudio(String username, float x, float y, float z, byte[] audio) {
        audioClientSender.sendAudio(username, x, y, z, audio);
    }
}

