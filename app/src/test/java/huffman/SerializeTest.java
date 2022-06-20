package huffman;

import org.junit.Test;

import huffman.Bits.Bits;
import huffman.bitreader.TreeReader;

import static org.junit.Assert.*;

import org.apache.commons.codec.DecoderException;

public class SerializeTest {
    @Test
    public void serializeIsIdentical() throws DecoderException {
        Node tree = TreeCreator.create("Hello World");
        Bits serial = tree.serialize();
        Node tree2 = serial.applyBitReader(new TreeReader());
        assertTrue(tree2.equals(tree));
    }
}
