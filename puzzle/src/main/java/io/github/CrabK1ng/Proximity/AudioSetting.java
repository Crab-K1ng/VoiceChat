package io.github.CrabK1ng.Proximity;

import javax.sound.sampled.AudioFormat;

public class AudioSetting {

    private static AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
    private static int sampleRate = 8000; //48000
    private static int sampleSizeInBits = 16;
    private static int channels = 1; // Mono (1) or Stereo (2)
    private static int frameRate = sampleRate;
    private static boolean bigEndian = false;

    // use this for java AudioFormat
    private static int bytesPerFrame = 2;

    // use this of Buffers
    private static int samplesPerBuffer = 160;

    private static AudioFormat format;

    public static void init(){
        int bytesPerSample = sampleSizeInBits / 8;
        bytesPerFrame = bytesPerSample * channels;

        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 1, 2, 48000, false);
    }

    public static AudioFormat.Encoding getEncoding() {
        return encoding;
    }

    public static void setEncoding(AudioFormat.Encoding encoding) {
        AudioSetting.encoding = encoding;
    }

    public static int getSampleRate() {
        return sampleRate;
    }

    public static void setSampleRate(int sampleRate) {
        AudioSetting.sampleRate = sampleRate;
    }

    public static int getSampleSizeInBits() {
        return sampleSizeInBits;
    }

    public static void setSampleSizeInBits(int sampleSizeInBits) {
        AudioSetting.sampleSizeInBits = sampleSizeInBits;
    }

    public static int getChannels() {
        return channels;
    }

    public static void setChannels(int channels) {
        AudioSetting.channels = channels;
    }

    public static int getFrameRate() {
        return frameRate;
    }

    public static void setFrameRate(int frameRate) {
        AudioSetting.frameRate = frameRate;
    }

    public static boolean isBigEndian() {
        return bigEndian;
    }

    public static void setBigEndian(boolean bigEndian) {
        AudioSetting.bigEndian = bigEndian;
    }

    public static int getBytesPerFrame() {
        return bytesPerFrame;
    }

    public static void setBytesPerFrame(int bytesPerFrame) {
        AudioSetting.bytesPerFrame = bytesPerFrame;
    }

    public static int getSamplesPerBuffer() {
        return samplesPerBuffer;
    }

    public static void setSamplesPerBuffer(int samplesPerBuffer) {
        AudioSetting.samplesPerBuffer = samplesPerBuffer;
    }

    public static AudioFormat getFormat() {
        return format;
    }

    public static void setFormat(AudioFormat format) {
        AudioSetting.format = format;
    }
}
