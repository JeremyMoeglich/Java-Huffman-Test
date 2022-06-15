package huffman;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.codec.DecoderException;

import huffman.bits.Bits;

public class App {
    public static void main(String[] args) throws Exception {
        String input = Files.readString(Paths.get("input.txt"));
        com.google.common.io.Files.write(Encode.encode(input).encode(), new File("test.txt"));

        byte[] input2 = Files.readAllBytes(Paths.get("test.txt"));
        String output = Decode.decode(Bits.decode(input2));
        PrintWriter writer = new PrintWriter("test2.txt");
        writer.println(output);
        writer.close();
    }

}
