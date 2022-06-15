package huffman.bits.bitreader;

import org.apache.commons.codec.DecoderException;

import huffman.bits.Bits;

public interface BitSlicer {
    boolean readBit(boolean bit, Bits bits) throws DecoderException;
}
