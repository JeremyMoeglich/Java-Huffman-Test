package dev.moeglich.huffmanlib.bitreader;

import dev.moeglich.bitslib.Bits;

public class ZeroBitSlicer implements BitSlicer {
    @Override
    public boolean readBit(boolean bit, Bits bits) {
        return !bit;
    }
}
