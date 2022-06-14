package huffman;

public class NodeCreator {
    public static Node fromString(String str) {
        return TreeCreator.create(str);
    }
}
