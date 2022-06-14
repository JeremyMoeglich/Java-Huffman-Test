package huffman;

class DeserializeResult {
    final public Node node;
    final public Integer endIndex;

    public DeserializeResult(Node node, Integer endIndex) {
        this.node = node;
        this.endIndex = endIndex;
    }
}

public class Serialize {
    public static String serialize(Node node) {
        if (node.value == null) {
            return "n" + serialize(node.left) + serialize(node.right);
        } else {
            return "c" + node.value;
        }
    }

    public static Node deserialize(String str) {
        return deserializeWithIndex(str).node;
    }

    static DeserializeResult deserializeWithIndex(String str) {
        switch (str.charAt(0)) {
            case 'n':
                Integer i = 1;
                DeserializeResult left_result = deserializeWithIndex(str.substring(i));
                i += left_result.endIndex + 1;
                DeserializeResult right_result = deserializeWithIndex(str.substring(i));
                i += right_result.endIndex;
                return new DeserializeResult(new Node(left_result.node, right_result.node), i);
            case 'c':
                return new DeserializeResult(new Node(str.charAt(1)), 1);
            default:
                throw new IllegalArgumentException("Invalid state");
        }
        
    }
}
