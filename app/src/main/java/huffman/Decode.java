package huffman;

import java.util.Arrays;

public class Decode {
    public static String decode(byte[] bytes) {
        String stringified = new String(bytes);
        DeserializeResult result = Serialize.deserializeWithIndex(stringified);
        Node tree = result.node;
        int index = result.endIndex;

        int length = Integer.parseInt(stringified.charAt(index + 1) + "");

        byte[] encoded = Arrays.copyOfRange(bytes, index + 2, bytes.length);
        return decodeData(encoded, tree, length);
    }

    public static String decodeData(byte[] bytes, Node tree, int removeAmount) {
        String decoded = "";
        Node currentNode = tree;

        boolean[] bits = Arrays.copyOfRange(Utils.getBooleanArray(bytes), 0, bytes.length * 8 - removeAmount);
        
        for (boolean bit : bits) {
            if (bit) {
                currentNode = currentNode.right;
            } else {
                currentNode = currentNode.left;
            }

            if (currentNode.value != null) {
                decoded += currentNode.value;
                currentNode = tree;
            }
        }
        return decoded;
    }
}
