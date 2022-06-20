package huffman.bitreader;

import org.apache.commons.codec.DecoderException;

import huffman.Node;
import huffman.Bits.Bits;

public class StringDecodeReader implements BitReader<String> {
    String result = null;

    @Override
    public boolean readBit(boolean bit, Bits bits) throws DecoderException {
        bits.pushStart(bit);
        Node tree = bits.applyBitReader(new TreeReader());
        result = bits.applyBitReader(new HuffmanReader(tree));
        return false;
    }

    @Override
    public String output() {
        return result;
    }

    @Override
    public String end() throws DecoderException {
        throw new DecoderException("Can't decode empty bits");
    }
}
