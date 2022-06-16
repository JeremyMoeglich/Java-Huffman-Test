package huffman;

import org.junit.Test;

import huffman.bits.Bits;
import huffman.bits.bitreader.HuffmanReader;
import huffman.bits.bitreader.StringDecodeReader;

import static org.junit.Assert.*;

import org.apache.commons.codec.DecoderException;

public class DecodeTest {
    @Test
    public void simpleDecode() throws DecoderException {
        Node tree = new Node(new Node('5'), new Node(new Node('3'), new Node('4')));
        Bits bits = Bits.fromBits("011100");
        String decoded = bits.applyBitReader(new HuffmanReader(tree));
        assertEquals("5435", decoded);
    }

    @Test
    public void simpleEncode() {
        Node tree = new Node(new Node('5'), new Node(new Node('3'), new Node('4')));
        Bits encoded = Encode.encodeByTree("5345", tree);
        assertEquals(Bits.fromBits("10001101011000110011000110100 010110"), encoded);
    }

    @Test
    public void decodeCorrect() throws DecoderException {
        String text = "Hello World";
        Bits encoded = Encode.encode(text);
        String decoded = encoded.applyBitReader(new StringDecodeReader());
        assertEquals(text, decoded);
    }

    @Test
    public void unicodeDecode() throws DecoderException {
        String text = "abc\r\n αση∘λβξπ′";
        Bits encoded = Encode.encode(text);
        String decoded = encoded.applyBitReader(new StringDecodeReader());
        assertEquals(text, decoded);
    }
}
