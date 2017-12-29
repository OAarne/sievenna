/**
 * The class implements a node used to create a trie for allocating Huffman codes to each possible byte.
 */

public class HuffNode implements Comparable<HuffNode> {
    private HuffNode left, right;
    private int freq, key; // the sum of the frequencies of the 256 leaves is the length of the file

    public HuffNode(int key, int freq, HuffNode left, HuffNode right) {
        this.key = key;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }

    /**
     * Returns a new node with this node as the left child and the param as the right.
     * @param right
     * @return
     */

    public HuffNode join(HuffNode right) {
        return new HuffNode(-1, this.freq + right.getFreq(), this, right);
    }

    @Override
    public int compareTo(HuffNode other) {
        // this is reversed so I could use the standard PriorityQueue implementation.
        return other.getFreq() - this.freq;
    }

    public int getKey() {
        return key;
    }

    public HuffNode getLeft() {
        return left;
    }

    public HuffNode getRight() {
        return right;
    }

    public int getFreq() {
        return freq;
    }
}
