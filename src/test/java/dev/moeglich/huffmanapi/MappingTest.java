package dev.moeglich.huffmanapi;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import dev.moeglich.bitslib.Bits;
import dev.moeglich.huffmanlib.Encode;
import dev.moeglich.huffmanlib.Mapping;
import dev.moeglich.huffmanlib.Node;

import java.util.HashMap;

public class MappingTest {
    @Test 
    public void mappingCorrect() {
        Node tree = new Node(new Node('5'), new Node(new Node('3'), new Node('4')));
        HashMap<Character, Bits> mapping = Mapping.create(tree);
        HashMap<Character, Bits> expected = new HashMap<>();
        expected.put('5', Bits.fromBits("0"));
        expected.put('3', Bits.fromBits("10"));
        expected.put('4', Bits.fromBits("11"));
        assertEquals(expected.get('5'), mapping.get('5'));
        assertEquals(expected.get('3'), mapping.get('3'));
        assertEquals(expected.get('4'), mapping.get('4'));
    }

    @Test
    public void mappingResult() {
        Node tree = new Node(new Node('5'), new Node(new Node('3'), new Node('4')));
        Bits mapped = Encode.mapByTree(tree, "5435");
        assertEquals(Bits.fromBits("011100"), mapped);
    }
}
