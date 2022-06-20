package huffman.bitreader;

import org.apache.commons.codec.DecoderException;

import huffman.Bits.Bits;

public interface BitSlicer {
    boolean readBit(boolean bit, Bits bits) throws DecoderException;
}
