package dev.moeglich.huffmanapi.bitreader;

import org.apache.commons.codec.DecoderException;

import dev.moeglich.huffmanapi.Node;
import dev.moeglich.huffmanapi.bits.Bits;


public class HuffmanReader implements BitReader<String> {
    Node tree;
    Node currentNode;
    String decoded = "";
    Bits current = new Bits();

    public HuffmanReader(Node tree) {
        this.tree = tree;
        this.currentNode = tree;
    }

    @Override
    public boolean readBit(boolean bit, Bits bits) throws DecoderException {
        if (bit) {
            currentNode = currentNode.right;
        } else {
            currentNode = currentNode.left;
        }

        if (currentNode.value != null) {
            decoded += currentNode.value;
            currentNode = tree;
        }

        return true;
    }

    @Override
    public String output() {
        return decoded;
    }

    @Override
    public String end() throws DecoderException {
        if (current.length() > 0) {
            throw new DecoderException("Huffman bits ended early");
        }
        return decoded;
    }
}
