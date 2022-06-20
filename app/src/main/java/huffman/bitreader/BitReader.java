package huffman.bitreader;

import org.apache.commons.codec.DecoderException;

public interface BitReader<T> extends BitSlicer {
    T output();

    T end() throws DecoderException;
}
