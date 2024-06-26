package dev.moeglich.huffmanlib.bitreader;

import org.apache.commons.codec.DecoderException;

import dev.moeglich.bitslib.Bits;
import dev.moeglich.huffmanlib.Node;


public class StringDecodeReader implements BitReader<String> {
    String result = null;

    @Override
    public boolean readBit(boolean bit, Bits bits) throws DecoderException {
        bits.pushStart(bit);
        Node tree = bits.applyBitReader(new TreeReader());
        if (tree.value == null) {
            result = bits.applyBitReader(new HuffmanReader(tree));
        } else {
            result = "";
            for (int i = 0; i < bits.toInt(); i++) {
                result += tree.value;
            }
        }
        return false;
    }

    @Override
    public String output() {
        return result;
    }

    @Override
    public String end() throws DecoderException {
        return "";
    }
}
