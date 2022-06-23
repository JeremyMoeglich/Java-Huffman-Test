package dev.moeglich.huffmanapi.bits;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class BitUtils {
    public static boolean[] getBooleanArray(byte b) {
        return getBooleanArray(new byte[] { b });
    }

    public static boolean[] getBooleanArray(byte[] bytes) {
        BitSet bits = BitSet.valueOf(bytes);
        boolean[] bools = new boolean[bytes.length * 8];
        for (int i = bits.nextSetBit(0); i != -1; i = bits.nextSetBit(i + 1)) {
            bools[i] = true;
        }
        return bools;
    }

    public static byte[] toByteArray(boolean[] bools) {
        BitSet bits = new BitSet(bools.length);
        for (int i = 0; i < bools.length; i++) {
            if (bools[i]) {
                bits.set(i);
            }
        }

        byte[] bytes = bits.toByteArray();
        
        if (bytes.length * 8 >= bools.length) {
            return bytes;
        } else {
            return Arrays.copyOf(bytes, bools.length / 8 + (bools.length % 8 == 0 ? 0 : 1));
        }
    }

    public static byte reverseBitsByte(byte x) {
        byte y = 0;
        for (int i = 0; i < 8; i++) {
            y = (byte) ((y << 1) | (x & 1));
            x = (byte) (x >> 1);
        }
        return y;
    }

    public static byte[] toByteArray(Boolean[] bools) {
        return toByteArray(ArrayUtils.toPrimitive(bools));
    }

    public static boolean[] toPrimitiveArray(final List<Boolean> booleanList) {
        final boolean[] primitives = new boolean[booleanList.size()];
        int index = 0;
        for (Boolean object : booleanList) {
            primitives[index++] = object;
        }
        return primitives;
    }
}
