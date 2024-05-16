package dev.moeglich.huffmanlib;

import dev.moeglich.bitslib.Bits;

public class Node {
    final public Node left;
    final public Node right;
    final public Character value;

    public Node(Character value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public Node(Node left, Node right) {
        this.left = left;
        this.right = right;
        this.value = null;
    }

    public String toString() {
        if (this.value != null) {
            return this.value.toString();
        } else {
            return String.format("""
                    left: {
                    %s
                    }
                    right: {
                    %s
                    }
                        """, StringFormat.indentString(this.left.toString()),
                    StringFormat.indentString(this.right.toString()));
        }
    }

    public boolean equals(Node other) {
        if (this.value != null) {
            return this.value.equals(other.value);
        } else {
            return this.left.equals(other.left) && this.right.equals(other.right);
        }
    }

    public Bits serialize() {
        return Serialize.serialize(this);
    }
}
