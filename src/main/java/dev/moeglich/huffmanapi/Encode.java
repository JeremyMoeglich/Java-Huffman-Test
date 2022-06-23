package dev.moeglich.huffmanapi;

import java.util.HashMap;

import dev.moeglich.huffmanapi.bits.Bits;

public class Encode {
    public static Bits mapByTree(Node tree, String string) {
        if (tree.value == null) {
            HashMap<Character, Bits> mapping = Mapping.create(tree);
            Bits encoded = new Bits();
            char[] chars = string.toCharArray();
            for (char c : chars) {
                encoded.push(mapping.get(c));
            }
            return encoded;
        } else {
            return new Bits(string.length());
        }

    }

    public static Bits encodeByTree(String str, Node tree) {
        Bits bits = mapByTree(tree, str);

        return Bits.merge(
                tree.serialize(),
                bits);
    }

    
    public static Bits encode(String str) {
        if (str == null || str.isEmpty()) {
            return new Bits();
        }

        Node tree = TreeCreator.create(str);
        return encodeByTree(str, tree);
    }
}
