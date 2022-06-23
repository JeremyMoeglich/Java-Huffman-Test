package dev.moeglich.huffmanapi;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import dev.moeglich.huffmanapi.bitreader.TreeReader;
import dev.moeglich.huffmanapi.bits.Bits;

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
