package dev.moeglich.huffmanlib;

public class WeightedNode extends Node implements Comparable<WeightedNode> {
    final public Integer weight;

    public WeightedNode(Integer weight, Character value) {
        super(value);
        this.weight = weight;
    }

    public WeightedNode(Integer weight, Node left, Node right) {
        super(left, right);
        this.weight = weight;
    }

    @Override
    public int compareTo(WeightedNode other) {
        return this.weight.compareTo(other.weight);
    }
}
