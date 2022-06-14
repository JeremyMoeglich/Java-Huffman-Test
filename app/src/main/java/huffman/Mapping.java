package huffman;

import java.util.HashMap;

public class Mapping {
    public static HashMap<Character, boolean[]> create(Node tree) {
        HashMap<Character, boolean[]> mapping = new HashMap<>();
        if (tree.value != null) {
            mapping.put(tree.value, new boolean[] {});
        }

        HashMap<Character, boolean[]> left = pushOntoStart(create(tree.left), false);
        HashMap<Character, boolean[]> right = pushOntoStart(create(tree.left), true);

        return merge(left, right);
    }

    static HashMap<Character, boolean[]> merge(HashMap<Character, boolean[]> left, HashMap<Character, boolean[]> right) {
        HashMap<Character, boolean[]> result = new HashMap<>();
        for (Character key : left.keySet()) {
            result.put(key, left.get(key));
        }
        for (Character key : right.keySet()) {
            result.put(key, right.get(key));
        }
        return result;
    }

    static HashMap<Character, boolean[]> pushOntoStart(HashMap<Character, boolean[]> mapping, boolean value) {
        HashMap<Character, boolean[]> newMapping = new HashMap<>();
        for (Character key : mapping.keySet()) {
            newMapping.put(key, pushOntoStart(mapping.get(key), value));
        }
        return newMapping;
    }

    static boolean[] pushOntoStart(boolean[] arr, boolean val) {
        boolean[] newArr = new boolean[arr.length + 1];
        newArr[0] = val;
        for (int i = 0; i < arr.length; i++) {
            newArr[i + 1] = arr[i];
        }
        return newArr;
    }
}
