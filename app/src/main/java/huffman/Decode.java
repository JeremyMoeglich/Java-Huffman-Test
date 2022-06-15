package huffman;

import java.util.Arrays;

import org.apache.commons.codec.DecoderException;

import huffman.bits.Bits;
import huffman.bits.bitreader.TreeReader;

public class Decode {
    public static String decode(Bits bits) throws DecoderException {
        Node tree = bits.applyBitReader(new TreeReader());
        return decodeData(bits, tree);
    }

    public static String decodeData(Bits bits, Node tree) {
        String decoded = "";
        Node currentNode = tree;
        
        for (boolean bit : bits) {
            if (bit) {
                currentNode = currentNode.right;
            } else {
                currentNode = currentNode.left;
            }

            if (currentNode.value != null) {
                decoded += currentNode.value;
                currentNode = tree;
            }
        }
        return decoded;
    }
}
