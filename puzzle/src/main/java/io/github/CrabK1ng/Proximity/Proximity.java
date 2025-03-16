package io.github.CrabK1ng.Proximity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import javax.sound.sampled.*;

public class Proximity {
    public static SourceDataLine speakers;
    public static TargetDataLine microphone;
    public static boolean lineOpen = true;
    //spicy or lemon lol prefer spicy tho ok spicy ++ <- thats my thumbs up ++ lol // check out the mic icon lol // i thought final added a way for the client to know if the server is modded i dont't know // i dont have a mail ..., so i can report him to the police, his fingerprints will be all over the bomb lol // have you seen the mic on and off icons resources.assets.proximity can't loaded it, uh weird
    public static void toggleMic() {
        lineOpen = !lineOpen;
    }

    public static void initText() {
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, 0);
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
