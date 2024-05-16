package dev.moeglich.huffmanlib;

import java.util.Base64;

import org.apache.commons.codec.DecoderException;

import dev.moeglich.bitslib.Bits;
import dev.moeglich.huffmanlib.bitreader.StringDecodeReader;

public class Huffman {
    public static Bits encode_to_bits(String input) {
        return Encode.encode(input);
    }

    public static byte[] encode_to_bytes(String input) {
        return encode_to_bits(input).encode();
    }

    public static String encode_to_base64(String input) {
        return Base64.getEncoder().encodeToString(encode_to_bytes(input));
    }

    public static String decode_from_base64(String input) throws DecoderException {
        return decode_from_bytes(Base64.getDecoder().decode(input));
    }

    public static String decode_from_bytes(byte[] input) throws DecoderException {
        return decode_from_bits(Bits.decode(input));
    }

    public static String decode_from_bits(Bits input) throws DecoderException {
        return input.applyBitReader(new StringDecodeReader());
    }

}
