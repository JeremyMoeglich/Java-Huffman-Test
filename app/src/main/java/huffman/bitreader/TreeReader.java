package huffman.bitreader;

import org.apache.commons.codec.DecoderException;

import huffman.Node;
import huffman.Bits.Bits;


public class TreeReader implements BitReader<Node> {
    private Node node = null;

    @Override
    public Node end() throws DecoderException {
        throw new DecoderException("Tree bits ended early");
    }

    @Override
    public boolean readBit(boolean bit, Bits bits) throws DecoderException {
        if (bit) {
            Node left = bits.applyBitReader(new TreeReader());
            Node right = bits.applyBitReader(new TreeReader());
            node = new Node(left, right);
            return false;
        } else {
            Character s = bits.applyBitReader(new UTF8Reader());
            node = new Node(s);
            return false;
        }
    }

    @Override
    public Node output() {
        return node;
    }
}
