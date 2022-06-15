package huffman.bits;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding.DecodingException;

import huffman.Utils;
import huffman.bits.bitreader.BitReader;
import huffman.bits.bitreader.BitSlicer;

public class Bits implements Iterable<Boolean> {
    private ArrayList<Byte> bytes;
    private int start = 0;
    private int end;

    public Bits(byte[] bytes) {
        this.bytes = new ArrayList<>();
        for (byte b : bytes) {
            this.bytes.add(b);
        }
        end = bytes.length * 8;
    }

    public Bits() {
        this.bytes = new ArrayList<>();
        end = 0;
    }

    public Bits(String string) {
        byte[] big_endian = StringUtils.getBytesUtf8(string);
        byte[] little_endian = littleBigEndianConvert(big_endian);
        this.bytes = new ArrayList<>();
        for (byte b : little_endian) {
            this.bytes.add(b);
        }
        end = little_endian.length * 8;
    }

    public Bits(boolean[] booleans) {
        byte[] bytes = Utils.toByteArray(booleans);
        this.bytes = new ArrayList<>();
        for (byte b : bytes) {
            this.bytes.add(b);
        }
        end = booleans.length;
    }

    int offsetIndex(int index) {
        return start + index;
    }

    public static Bits fromZeroOneSequence(String sequence) {
        Bits bits = new Bits();
        for (char c : sequence.trim().replace(" ", "").replace("_", "").toCharArray()) {
            if (c == '0') {
                bits.push(false);
            } else if (c == '1') {
                bits.push(true);
            } else {
                throw new IllegalArgumentException("Invalid character in sequence: " + c);
            }
        }
        return bits;
    }

    public boolean get(int index) {
        if (!indexContained(index)) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        index = offsetIndex(index);

        byte b = bytes.get(index / 8);
        boolean[] booleans = Utils.getBooleanArray(b);
        return booleans[index % 8];
    }

    public int length() {
        return end - start;
    }

    static int startByteRound(int index) {
        return index / 8;
    }

    static int endByteRound(int index) {
        if (index % 8 == 0) {
            return index / 8;
        }
        return index / 8 + 1;
    }

    int endSpaceRemaining() {
        return bytes.size() * 8 - end;
    }

    public void cleanup() {
        if (endSpaceRemaining() == 0 && start == 0) {
            return;
        }
        ArrayList<Byte> newBytes = new ArrayList<>();

        if (end - start < 8) {
            boolean[] booleans = new boolean[8];
            for (int i = 0; i < end - start; i++) {
                booleans[i] = get(i);
            }
            newBytes.add(Utils.toByteArray(booleans)[0]);
        } else {
            int startOffset = 8 - start % 8;
            int endOffset = end % 8;
            ArrayList<Boolean> currentChunk = new ArrayList<>();
            for (int publicBitIndex = 0; publicBitIndex < startOffset; publicBitIndex++) {
                currentChunk.add(get(publicBitIndex));
            }
            for (int internalByteIndex = endByteRound(start); internalByteIndex < startByteRound(
                    end); internalByteIndex++) {
                byte b = bytes.get(internalByteIndex);
                boolean[] booleans = Utils.getBooleanArray(b);
                for (boolean b1 : booleans) {
                    currentChunk.add(b1);
                    if (currentChunk.size() == 8) {
                        newBytes.add(Utils.toByteArray(Utils.toPrimitiveArray(currentChunk))[0]);
                        currentChunk = new ArrayList<>();
                    }
                }
            }
            for (int publicBitIndex = length() - endOffset; publicBitIndex < length(); publicBitIndex++) {
                currentChunk.add(get(publicBitIndex));
                if (currentChunk.size() == 8) {
                    newBytes.add(Utils.toByteArray(Utils.toPrimitiveArray(currentChunk))[0]);
                    currentChunk = new ArrayList<>();
                }
            }
            if (currentChunk.size() > 0) {
                newBytes.add(Utils.toByteArray(Utils.toPrimitiveArray(currentChunk))[0]);
            }
        }

        bytes = newBytes;
        end = length();
        start = 0;
    }

    public byte[] getBytes() {
        cleanup();
        byte[] bytes = new byte[this.bytes.size()];
        for (int i = 0; i < this.bytes.size(); i++) {
            bytes[i] = this.bytes.get(i);
        }
        return bytes;
    }

    static String toString(byte b) {
        boolean[] booleans = Utils.getBooleanArray(b);
        StringBuilder sb = new StringBuilder();
        for (boolean b1 : booleans) {
            sb.append(b1 ? "1" : "0");
        }
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            sb.append(get(i) ? "1" : "0");
        }
        return sb.toString();
    }

    public void set(int index, boolean value) {
        if (!indexContained(index)) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        index = offsetIndex(index);

        byte b = bytes.get(index / 8);
        boolean[] booleans = Utils.getBooleanArray(b);
        booleans[index % 8] = value;
        bytes.set(index / 8, Utils.toByteArray(booleans)[0]);
    }

    public void push(boolean value) {
        if (endSpaceRemaining() == 0) {
            bytes.add((byte) 0);
        }
        end++;
        set(length() - 1, value);
    }

    public void push(boolean[] values) {
        for (boolean value : values) {
            push(value);
        }
    }

    public void push(Bits bits) {
        for (int i = 0; i < bits.length(); i++) {
            push(bits.get(i));
        }
    }

    public void push(byte b) {
        boolean[] booleans = Utils.getBooleanArray(b);
        for (boolean value : booleans) {
            push(value);
        }
    }

    public void push(byte[] bytes) {
        for (byte b : bytes) {
            push(b);
        }
    }

    public void push(String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        for (byte b : bytes) {
            push(b);
        }
    }

    public void push(int value, int bit_amount) {
        for (int i = 0; i < bit_amount; i++) {
            push((value & (1 << i)) != 0);
        }
    }

    public void pushStart(boolean value) {
        if (start == 0) {
            bytes.add(0, (byte) 0);
            end += 8;
            start += 8;
        }

        start--;
        set(0, value);
    }

    public void pushStart(boolean[] values) {
        boolean[] reversedValues = new boolean[values.length];
        for (int i = 0; i < values.length; i++) {
            reversedValues[i] = values[values.length - i - 1];
        }
        for (boolean value : reversedValues) {
            pushStart(value);
        }
    }

    public void pushStart(Bits bits) {
        for (boolean bit : bits.reverse()) {
            pushStart(bit);
        }
    }

    public void pushStart(byte b) {
        boolean[] booleans = Utils.getBooleanArray(b);
        pushStart(booleans);
    }

    public void pushStart(byte[] bytes) {
        byte[] reversedBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            reversedBytes[i] = bytes[bytes.length - i - 1];
        }
        for (byte b : reversedBytes) {
            pushStart(b);
        }
    }

    public boolean indexContained(int index) {
        return offsetIndex(index) < end;
    }

    public boolean pop() {
        if (end == start) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        boolean value = get(length() - 1);
        end--;
        return value;
    }

    public void clear() {
        bytes = new ArrayList<>();
        start = 0;
        end = 0;
    }

    void setToBits(Bits bits) {
        clear();
        push(bits);
    }

    public boolean pop(int index) {
        if (!indexContained(index)) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        boolean value = get(index);
        if (index == 0) {
            start++;
        } else {
            Bits endBits = getSlice(index, length());
            slice(0, index - 1);
            push(endBits);
        }

        return value;
    }

    static byte[] littleBigEndianConvert(byte[] bytes) {
        byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            newBytes[i] = Utils.reverseBitsByte(bytes[i]);
        }
        return newBytes;
    }

    public String asString() {
        byte[] byteArray = littleBigEndianConvert(getBytes());
        return new String(byteArray, StandardCharsets.UTF_8);
    }

    public Bits slice(int startIndex, int endIndex) {
        if (!indexContained(startIndex) || !indexContained(endIndex - 1)) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        end = offsetIndex(endIndex);
        start = offsetIndex(startIndex);

        return this;
    }

    public Bits slice(int startIndex) {
        return slice(startIndex, length());
    }

    public Bits getSlice(int startIndex, int endIndex) {
        return this.clone().slice(startIndex, endIndex);
    }

    public Bits getSlice(int startIndex) {
        return getSlice(startIndex, length());
    }

    public Iterator<Boolean> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < end;
            }

            @Override
            public Boolean next() {
                return get(index++);
            }
        };
    }

    public Bits clone() {
        Bits bits = new Bits();
        for (int i = 0; i < length(); i++) {
            bits.push(get(i));
        }
        return bits;
    }

    public Bits reverse() {
        Bits bits = new Bits();
        for (int i = length() - 1; i >= 0; i--) {
            bits.push(get(i));
        }
        return bits;
    }

    public static Bits merge(Bits[]... bits) {
        Bits result = new Bits();
        for (Bits[] b : bits) {
            for (Bits b1 : b) {
                result.push(b1);
            }
        }
        return result;
    }

    public static Bits merge(Bits... bits) {
        Bits result = new Bits();
        for (Bits b : bits) {
            result.push(b);
        }
        return result;
    }

    public int toInt() {
        int result = 0;
        for (int i = 0; i < length(); i++) {
            if (get(i)) {
                result |= 1 << i;
            }
        }
        return result;
    }

    public <T> T applyBitReader(BitReader<T> bitReader) throws DecoderException {
        while (true) {
            if (length() == 0) {
                return bitReader.end();
            }
            boolean bit = get(0);
            if (!bitReader.readBit(bit, this)) {
                return bitReader.output();
            }
            start++;
        }
    }

    public void applyBitSlicer(BitSlicer bitSlicer) throws DecoderException {
        while (true) {
            if (length() == 0) {
                return;
            }
            boolean bit = get(0);
            if (!bitSlicer.readBit(bit, this)) {
                return;
            }
            start++;
        }
    }
}