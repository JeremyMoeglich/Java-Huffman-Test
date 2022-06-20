package huffman;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import huffman.Bits.BitUtils;
import huffman.Bits.Bits;
import huffman.bitreader.UTF8Reader;
import huffman.bitreader.ZeroBitSlicer;

import static org.junit.Assert.*;

public class BitsTest {
    @Test
    public void readWrite() {
        Bits bits = new Bits();
        bits.push(true);
        bits.push(false);
        bits.push(true);
        bits.push(false);
        bits.push(false);
        assertEquals("10100", bits.toString());
        assertEquals(bits.get(2), true);
        assertEquals(bits.get(3), false);
    }

    @Test
    public void Initialize() {
        Bits bits = new Bits();
        assertEquals("", bits.toString());
        bits = new Bits(new boolean[] { true, false, true, false, false });
        assertEquals("10100", bits.toString());
        bits = new Bits(new boolean[] { true, false, true, false, false, true });
        assertEquals("101001", bits.toString());
        bits = new Bits("ok");
        assertEquals("0110111101101011", bits.toString());
    }

    @Test
    public void slice() {
        Bits bits = new Bits(new boolean[] { true, false, true, false, false, true });
        bits.slice(2);
        assertEquals("1001", bits.toString());
        Bits bits2 = bits.getSlice(2);
        assertEquals("01", bits2.toString());
        assertEquals("1001", bits.toString());
        bits.push(true);
        assertEquals("10011", bits.toString());
        bits.push(false);
        assertEquals("100110", bits.toString());
        assertArrayEquals(BitUtils.toByteArray(new boolean[] { true, false, false, true, true, false }),
                bits.getBytes());
        assertEquals(false, bits.pop());
        assertEquals("10011", bits.toString());
        assertEquals(false, bits.pop(2));
        assertEquals("1011", bits.toString());
        bits.cleanup();
        assertEquals("1011", bits.toString());
        bits.clear();
        assertEquals("", bits.toString());
        assertArrayEquals(new byte[] {}, bits.getBytes());
    }

    @Test
    public void longCleanup() {
        Bits bits = new Bits();
        for (int i = 0; i < 20; i++) {
            bits.push(true);
        }
        for (int i = 0; i < 5; i++) {
            bits.push(false);
        }
        for (int i = 0; i < 20; i++) {
            bits.push(true);
        }

        bits.slice(18, 28);
        assertEquals("1100000111", bits.toString());
        bits.cleanup();
        assertEquals("1100000111", bits.toString());
    }

    @Test
    public void byteCleanup() {
        Bits bits = new Bits(new boolean[] { true, false, true, false, false, true, true, false, true, false });
        bits.slice(1, 9);
        assertEquals("01001101", bits.toString());
        bits.cleanup();
        assertEquals("01001101", bits.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void set() {
        Bits bits = new Bits();
        bits.set(0, true);
    }

    @Test
    public void multiSplice() {
        Bits bits = new Bits();
        bits.push(true);
        bits.push(false);
        bits.push(true);
        bits.push(false);
        bits.push(false);
        bits.slice(2);
        bits.slice(2);
        assertEquals("0", bits.toString());
        bits.push(true);
        bits.push(false);
        bits.push(true);
        bits.slice(0, 2);
        assertEquals("01", bits.toString());
        bits.slice(1, 1);
        assertEquals("", bits.toString());
    }

    @Test
    public void pushStart() {
        Bits bits = new Bits();
        bits.push(true);
        bits.push(true);
        bits.push(true);
        bits.push(false);
        bits.pushStart(true);
        bits.pushStart(false);
        bits.pushStart(true);
        bits.pushStart(false);
        bits.pushStart(false);
        assertEquals("001011110", bits.toString());
        bits.pushStart(
                BitUtils.toByteArray(
                        new boolean[] { true, false, true, false, false, true, true, false, true, false }));
        assertEquals("1010011010000000001011110", bits.toString());
    }

    @Test
    public void stringEncodeTest() {
        Bits bits = new Bits("∘");
        assertEquals("∘", bits.asString());
        assertEquals("111000101000100010011000", bits.toString());
    }

    @Test
    public void bitSlicerTest() throws DecoderException {
        Bits bits = Bits.fromBits("00011001");
        bits.applyBitSlicer(new ZeroBitSlicer());
        assertEquals("11001", bits.toString());
    }

    @Test
    public void bitReaderTest() throws DecoderException {
        Bits bits = Bits.fromBits("11100010_10001000_10011000 10101");
        Character character = bits.applyBitReader(new UTF8Reader());
        assertEquals("10101", bits.toString());
        assertEquals('∘', character.charValue());

        bits = Bits.fromBits("01101111 1101");
        character = bits.applyBitReader(new UTF8Reader());
        assertEquals("1101", bits.toString());
        assertEquals('o', character.charValue());
    }

    @Test(expected = DecoderException.class)
    public void bitReaderTest2() throws DecoderException {
        Bits bits = Bits.fromBits("11100010_10001000_10");
        bits.applyBitReader(new UTF8Reader());
    }

    @Test
    public void bitsEncoding() {
        Bits bits = Bits.fromBits("10111011");
        byte[] encoded = bits.encode();
        Bits bits2 = Bits.decode(encoded);
        assertEquals("10111011", bits2.toString());
    }

}
