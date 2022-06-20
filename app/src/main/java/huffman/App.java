package huffman;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import huffman.Bits.Bits;
import huffman.bitreader.StringDecodeReader;

public class App {
    public static void main(String[] args) throws Exception {
        String input = Files.readString(Paths.get("input.txt"));
        com.google.common.io.Files.write(Encode.encode(input).encode(), new File("test.txt"));


        byte[] input2 = Files.readAllBytes(Paths.get("test.txt"));
        String output = Bits.decode(input2).applyBitReader(new StringDecodeReader());
        PrintWriter writer = new PrintWriter("test2.txt");
        writer.print(output);
        writer.close();
    }

}
