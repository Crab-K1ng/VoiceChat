package io.github.CrabK1ng.Proximity.audioFormat;

public class AudioFormat {

    private static javax.sound.sampled.AudioFormat.Encoding encoding = javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
    private static int sampleRate = 8000; //48000
    private static int sampleSizeInBits = 16;
    private static int channels = 2; // Mono (1) or Stereo (2)
    private static int frameRate = sampleRate;
    private static boolean bigEndian = false;

    // use this for java AudioFormat
    private static int bytesPerFrame = 2;

    // use this of Buffers
    private static int samplesPerBuffer = 160;

    private static javax.sound.sampled.AudioFormat format;

    public static void init(){
        int bytesPerSample = sampleSizeInBits / 8;
        bytesPerFrame = bytesPerSample * channels;

        format = new javax.sound.sampled.AudioFormat(javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 1, 2, 48000, false);
    }

    public static javax.sound.sampled.AudioFormat.Encoding getEncoding() {
        return encoding;
    }

    public static void setEncoding(javax.sound.sampled.AudioFormat.Encoding encoding) {
        AudioFormat.encoding = encoding;
    }

    public static int getSampleRate() {
        return sampleRate;
    }

    public static void setSampleRate(int sampleRate) {
        AudioFormat.sampleRate = sampleRate;
    }

    public static int getSampleSizeInBits() {
        return sampleSizeInBits;
    }

    public static void setSampleSizeInBits(int sampleSizeInBits) {
        AudioFormat.sampleSizeInBits = sampleSizeInBits;
    }

    public static int getChannels() {
        return channels;
    }

    public static void setChannels(int channels) {
        AudioFormat.channels = channels;
    }

    public static int getFrameRate() {
        return frameRate;
    }

    public static void setFrameRate(int frameRate) {
        AudioFormat.frameRate = frameRate;
    }

    public static boolean isBigEndian() {
        return bigEndian;
    }

    public static void setBigEndian(boolean bigEndian) {
        AudioFormat.bigEndian = bigEndian;
    }

    public static int getBytesPerFrame() {
        return bytesPerFrame;
    }

    public static void setBytesPerFrame(int bytesPerFrame) {
        AudioFormat.bytesPerFrame = bytesPerFrame;
    }

    public static int getSamplesPerBuffer() {
        return samplesPerBuffer;
    }

    public static void setSamplesPerBuffer(int samplesPerBuffer) {
        AudioFormat.samplesPerBuffer = samplesPerBuffer;
    }

    public static javax.sound.sampled.AudioFormat getFormat() {
        return format;
    }

    public static void setFormat(javax.sound.sampled.AudioFormat format) {
        AudioFormat.format = format;
    }
}
