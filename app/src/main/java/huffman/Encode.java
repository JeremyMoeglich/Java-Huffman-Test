package huffman;

import java.util.HashMap;

public class Encode {
    public static String encode(String str) {
        Node tree = TreeCreator.create(str);
        HashMap<Character, boolean[]> mapping = Mapping.create(tree);

        
        
    }
}
