package dev.moeglich.huffmanapi.bitreader;

import dev.moeglich.huffmanapi.bits.Bits;

public class ZeroBitSlicer implements BitSlicer {
    @Override
    public boolean readBit(boolean bit, Bits bits) {
        return !bit;
    }
}
