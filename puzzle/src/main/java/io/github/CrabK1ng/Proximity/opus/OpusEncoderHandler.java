package io.github.CrabK1ng.Proximity.opus;

import de.maxhenkel.opus4j.OpusEncoder;
import io.github.CrabK1ng.Proximity.Utils.BytesUtils;

public class OpusEncoderHandler {
    private OpusEncoder encoder;

    public OpusEncoderHandler(int sampleRate, int channels) throws Exception {
        encoder = new OpusEncoder(sampleRate, channels, OpusEncoder.Application.VOIP);
    }

    public byte[] encode(byte[] pcmData) {
        encoder.resetState();
        return encoder.encode(BytesUtils.bytesToShorts(pcmData));
    }

    public void close() {
        encoder.close();
    }
}
