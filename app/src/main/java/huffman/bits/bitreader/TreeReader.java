package huffman.bits.bitreader;

import org.apache.commons.codec.DecoderException;

import huffman.Node;
import huffman.bits.Bits;

// static DeserializeResult deserializeWithIndex(String str) {
//     switch (str.charAt(0)) {
//         case 'n':
//             Integer i = 1;
//             DeserializeResult left_result = deserializeWithIndex(str.substring(i));
//             i += left_result.endIndex + 1;
//             DeserializeResult right_result = deserializeWithIndex(str.substring(i));
//             i += right_result.endIndex;
//             return new DeserializeResult(new Node(left_result.node, right_result.node), i);
//         case 'c':
//             return new DeserializeResult(new Node(str.charAt(1)), 1);
//         default:
//             throw new IllegalArgumentException("Invalid state");
//     }
//     
// }

enum TreeReaderState {
    IDEN,
    NODE,
    CHAR
}

public class TreeReader implements BitReader<Node> {
    private TreeReaderState state = TreeReaderState.IDEN;
    private Node node = null;

    @Override
    public Node end() throws DecoderException {
        throw new DecoderException("Tree bits ended early");
    }

    @Override
    public boolean readBit(boolean bit, Bits bits) throws DecoderException {
        switch (state) {
            case IDEN:
                if (bit) {
                    state = TreeReaderState.NODE;
                } else {
                    state = TreeReaderState.CHAR;
                }
                return true;
            case NODE:
                Node left = bits.applyBitReader(new TreeReader());
                Node right = bits.applyBitReader(new TreeReader());
                node = new Node(left, right);
                return false;
            case CHAR:
                Character s = bits.applyBitReader(new UTF8Reader());
                node = new Node(s);
                return false;
            default:
                throw new IllegalArgumentException("Invalid state");
        }
    }

    @Override
    public Node output() {
        return node;
    }
}
