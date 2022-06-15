package huffman;

import org.junit.Test;
import static org.junit.Assert.*;

public class SerializeTest {
    @Test public void serializeIsIdentical() {
        Node tree = TreeCreator.create("Hello World");
        String serial = tree.serialize();
        Node tree2 = Serialize.deserialize(serial);
        assertTrue(tree2.equals(tree));
    }
}
