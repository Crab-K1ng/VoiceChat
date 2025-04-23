package io.github.CrabK1ng.Proximity.opus;

import de.maxhenkel.opus4j.OpusDecoder;
import io.github.CrabK1ng.Proximity.Utils.BytesUtils;

public class OpusDecoderHandler {
    private OpusDecoder decoder;

    public OpusDecoderHandler(int sampleRate, int channels) throws Exception {
        decoder = new OpusDecoder(sampleRate, channels);
    }

    public byte[] decode(byte[] opusData) {
        return BytesUtils.shortsToBytes(decoder.decode(opusData));
    }

    public void close() {
        decoder.close();
    }
}
