package dev.moeglich.huffmanapi.bitreader;

import org.apache.commons.codec.DecoderException;

import dev.moeglich.huffmanapi.bits.Bits;


public interface BitSlicer {
    boolean readBit(boolean bit, Bits bits) throws DecoderException;
}
