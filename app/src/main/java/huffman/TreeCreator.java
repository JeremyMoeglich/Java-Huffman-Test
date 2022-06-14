package huffman;

import java.util.HashMap;
import java.util.PriorityQueue;

public class TreeCreator {
    static HashMap<Character, Integer> getCharAmount(String str) {
        HashMap<Character, Integer> charAmount = new HashMap<>();
        for (char c : str.toCharArray()) {
            if (charAmount.containsKey(c)) {
                charAmount.put(c, charAmount.get(c) + 1);
            } else {
                charAmount.put(c, 1);
            }
        }
        return charAmount;
    }

    public static Node create(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("String cannot be null or empty");
        }
        HashMap<Character, Integer> charAmount = getCharAmount(str);

        PriorityQueue<WeightedNode> queue = new PriorityQueue<>();

        for (char c : charAmount.keySet()) {
            queue.add(new WeightedNode(charAmount.get(c), c));
        }

        while (queue.size() != 1) {
            WeightedNode left = queue.poll();
            WeightedNode right = queue.poll();
            WeightedNode newNode = new WeightedNode(left.weight + right.weight, left, right);
            queue.add(newNode);
        }

        return queue.poll();
    }
}
