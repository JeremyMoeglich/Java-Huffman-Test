package huffman;

import java.util.HashMap;

import huffman.Bits.Bits;

public class Mapping {
    public static HashMap<Character, Bits> create(Node tree) {
        HashMap<Character, Bits> mapping = new HashMap<>();
        if (tree.value != null) {
            mapping.put(tree.value, new Bits());
            return mapping;
        }

        HashMap<Character, Bits> left = pushOntoStart(create(tree.left), false);
        HashMap<Character, Bits> right = pushOntoStart(create(tree.right), true);

        return merge(left, right);
    }

    static HashMap<Character, Bits> merge(HashMap<Character, Bits> left, HashMap<Character, Bits> right) {
        HashMap<Character, Bits> result = new HashMap<>();
        for (Character key : left.keySet()) {
            result.put(key, left.get(key));
        }
        for (Character key : right.keySet()) {
            result.put(key, right.get(key));
        }
        return result;
    }

    static HashMap<Character, Bits> pushOntoStart(HashMap<Character, Bits> mapping, boolean value) {
        for (Character key : mapping.keySet()) {
            mapping.get(key).pushStart(value);
        }
        return mapping;
    }
}
