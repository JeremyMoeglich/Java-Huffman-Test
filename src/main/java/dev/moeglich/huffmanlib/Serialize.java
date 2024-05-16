package dev.moeglich.huffmanlib;

import dev.moeglich.bitslib.Bits;

class DeserializeResult {
    final public Node node;
    final public Integer endIndex;

    public DeserializeResult(Node node, Integer endIndex) {
        this.node = node;
        this.endIndex = endIndex;
    }
}

public class Serialize {
    public static Bits serialize(Node node) {
        if (node.value == null) {
            Bits left = serialize(node.left);
            Bits right = serialize(node.right);
            Bits merged = Bits.merge(left, right);
            merged.pushStart(true);
            return merged;
        } else {
            Bits bits = new Bits(node.value.toString());
            bits.pushStart(false);
            return bits;
        }
    }
}
