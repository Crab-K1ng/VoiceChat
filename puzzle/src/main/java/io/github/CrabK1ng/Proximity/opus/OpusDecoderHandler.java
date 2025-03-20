package io.github.CrabK1ng.Proximity.opus;

import de.maxhenkel.opus4j.OpusDecoder;

public class OpusDecoderHandler {
    private OpusDecoder decoder;

    public OpusDecoderHandler(int sampleRate, int channels) throws Exception {
        decoder = new OpusDecoder(sampleRate, channels);
    }

    public short[] decode(byte[] opusData) throws Exception {
        return decoder.decode(opusData);
    }

    public void close() {
        decoder.close();
    }
}
