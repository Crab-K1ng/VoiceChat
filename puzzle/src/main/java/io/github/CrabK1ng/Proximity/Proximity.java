package io.github.CrabK1ng.Proximity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import finalforeach.cosmicreach.settings.INumberSetting;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Proximity {
    public static SourceDataLine speakers;
    public static TargetDataLine microphone;

    public static float micLevel = 0f;

    private static float micVolumeFloat = 0f;
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

    public static boolean lineOpen = true;                       //spicy or lemon lol prefer spicy tho ok spicy ++ <- thats my thumbs up ++ lol // check out the mic icon lol // i thought final added a way for the client to know if the server is modded i dont't know // i dont have a mail ..., so i can report him to the police, his fingerprints will be all over the bomb lol // have you seen the mic on and off icons resources.assets.proximity can't loaded it, uh weird
    public static void toggleMic() {lineOpen = !lineOpen;}

    public static boolean menuOpen = false;
     public static void toggleMenu() {
        menuOpen = !menuOpen;
    }

    public static boolean drawIcon = true;
    public static void toggleIcon() {drawIcon = !drawIcon;}

    public static void initText() {
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, 0);
    }

    public static byte[] shortToByteArray(short[] shortArray) {
        ByteBuffer buffer = ByteBuffer.allocate(shortArray.length * 2);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // Ensure correct byte order
        for (short s : shortArray) {
            buffer.putShort(s);
        }
        return buffer.array();
    }
    public static void openMicrophone(){
            try {
                AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 1, 2, 48000, false);
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
                AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 1, 2, 48000, false);
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

    public static int calculateVolumePercent(byte[] audioData, int length) {
        long sum = 0;
        for (int i = 0; i < length - 1; i += 2) {
            int sample = (audioData[i] << 8) | (audioData[i + 1] & 0xFF);
            sum += (long) sample * sample;
        }

        double rms = Math.sqrt(sum / (length / 2.0));

        // Normalize: assuming 32767 (max value for 16-bit signed PCM) as 100%
        int volume = (int) ((rms / 32767) * 100);

        // Clamp between 0 and 100
        return Math.min(100, Math.max(0, volume));
    }
}
