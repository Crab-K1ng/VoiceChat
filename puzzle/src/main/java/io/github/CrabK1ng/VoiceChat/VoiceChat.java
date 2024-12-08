package io.github.CrabK1ng.VoiceChat;

import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.ModInitializer;
import finalforeach.cosmicreach.networking.GamePacket;
import io.github.CrabK1ng.VoiceChat.networking.VoiceChatPacket;

import javax.sound.sampled.*;

public class VoiceChat implements ModInitializer {
    public static SourceDataLine speakers;
    public static TargetDataLine microphone;


    @Override
    public void onInit() {
        Constants.LOGGER.info("Hello From INIT");
        GamePacket.registerPacket(VoiceChatPacket.class);
    }

    public static void openMicrophone(){
        try {
            AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                throw new LineUnavailableException("Line not supported");
            }

            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openSpeaker(){
        try {
            AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                throw new LineUnavailableException("Line not supported");
            }

            speakers = (SourceDataLine) AudioSystem.getLine(info);
            speakers.open(format);
            speakers.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
