package huffman;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

public class MappingTest {
    @Test public void mappingCorrect() {
        Node tree = new Node(new Node('5'), new Node(new Node('3'), new Node('4')));
        HashMap<Character, boolean[]> mapping = Mapping.create(tree);
        HashMap<Character, boolean[]> expected = new HashMap<>();
        expected.put('5', new boolean[] {false});
        expected.put('3', new boolean[] {true, false});
        expected.put('4', new boolean[] {true, true});
        assertArrayEquals(expected.get('5'), mapping.get('5'));
        assertArrayEquals(expected.get('3'), mapping.get('3'));
        assertArrayEquals(expected.get('4'), mapping.get('4'));
    }
}
