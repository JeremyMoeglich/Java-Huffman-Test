package huffman;

import java.util.ArrayList;
import java.util.HashMap;

public class Encode {
    static boolean[] mapByTree(Node tree, String string) {
        HashMap<Character, boolean[]> mapping = Mapping.create(tree);
        char[] chars = string.toCharArray();
        ArrayList<boolean[]> encoded = new ArrayList<>();
        for (char c : chars) {
            encoded.add(mapping.get(c));
        }
        return flatten(encoded);
    }

    public static byte[] encode(String str) {
        if (str == null || str.isEmpty()) {
            return new byte[0];
        }

        Node tree = TreeCreator.create(str);
        boolean[] flattened = mapByTree(tree, str);

        int removeAmount = 8 - (((flattened.length - 1) % 8) + 1);
        
        return mergeArrays(
                tree.serialize().getBytes(),
                String.valueOf(removeAmount).getBytes(),
                Utils.toByteArray(flattened));
    }

    static byte[] mergeArrays(byte[]... arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }
        byte[] result = new byte[length];
        int index = 0;
        for (byte[] array : arrays) {
            for (byte b : array) {
                result[index] = b;
                index++;
            }
        }
        return result;
    }

    static boolean[] flatten(ArrayList<boolean[]> arr) {
        ArrayList<Boolean> flattened = new ArrayList<>();
        for (boolean[] b : arr) {
            for (boolean b1 : b) {
                flattened.add(b1);
            }
        }
        boolean[] result = new boolean[flattened.size()];
        for (int i = 0; i < flattened.size(); i++) {
            result[i] = flattened.get(i);
        }
        return result;
    }
}
