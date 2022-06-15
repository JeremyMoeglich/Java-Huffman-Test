package huffman.bits.bitreader;

import org.apache.commons.codec.DecoderException;

import huffman.bits.Bits;

public class UTF8Reader implements BitReader<Character> {
    private int index = 0;
    private Bits stringBits = new Bits();
    private int bit_amount = 0;

    @Override
    public boolean readBit(boolean bit, Bits bits) {
        if (index == 0) {
            if (bit) {
                bit_amount = 16;
            } else {
                bit_amount = 8;
            }
        } else if (index == 2 && bit_amount == 16 && bit) {
            bit_amount = 24;
        } else if (index == 3 && bit_amount == 24 && bit) {
            bit_amount = 32;
        }

        if (index == bit_amount) {
            return false;
        }

        stringBits.push(bit);
        index++;
        return true;
    }

    @Override
    public Character end() throws DecoderException {
        throw new DecoderException("UTF-8 bits ended early");
    }

    @Override
    public Character output() {
        String s = stringBits.asString();
        Character c = s.charAt(0);
        return c;
    }
}
