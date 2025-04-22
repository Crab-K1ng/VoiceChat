package io.github.CrabK1ng.Proximity.opus;

import de.maxhenkel.opus4j.OpusEncoder;

public class OpusEncoderHandler {
    private OpusEncoder encoder;

    public OpusEncoderHandler(int sampleRate, int channels) throws Exception {
        encoder = new OpusEncoder(sampleRate, channels, OpusEncoder.Application.VOIP);
    }

    public byte[] encode(short[] pcmData) throws Exception {
        encoder.resetState();
        return encoder.encode(pcmData);
    }

    public void close() {
        encoder.close();
    }
}
