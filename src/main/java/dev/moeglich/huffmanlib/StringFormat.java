package dev.moeglich.huffmanlib;

import java.util.ArrayList;
import java.util.Arrays;

public class StringFormat {
    public static String indentString(String str, int indent) {
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(str.split("\n")));
        if (lines.size() == 1) {
            return indentLine(lines.get(0), indent);
        }
        return lines.stream().map(line -> indentString(line, indent)).reduce((a, b) -> a + "\n" + b).get();
    }

    public static String indentString(String str) {
        return indentString(str, 1);
    }

    static String indentLine(String str, int indent) {
        String indentStr = "";
        for (int i = 0; i < indent; i++) {
            indentStr += "\t";
        }
        return indentStr + str;
    }
}
