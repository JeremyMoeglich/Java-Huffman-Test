package dev.moeglich.huffmanlib.bitreader;

import org.apache.commons.codec.DecoderException;

import dev.moeglich.bitslib.Bits;


public interface BitSlicer {
    boolean readBit(boolean bit, Bits bits) throws DecoderException;
}
