package huffman.bitreader;

import huffman.Bits.Bits;

public class ZeroBitSlicer implements BitSlicer {
    @Override
    public boolean readBit(boolean bit, Bits bits) {
        return !bit;
    }
}
