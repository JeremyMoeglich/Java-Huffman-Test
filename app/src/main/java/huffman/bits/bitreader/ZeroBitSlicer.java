package huffman.bits.bitreader;

import huffman.bits.Bits;

public class ZeroBitSlicer implements BitSlicer {
    @Override
    public boolean readBit(boolean bit, Bits bits) {
        return !bit;
    }
}
