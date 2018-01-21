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
     * @return New parent node
     */

    public HuffNode join(HuffNode right) {
        return new HuffNode(-1, this.freq + right.getFreq(), this, right);
    }

    /**
     * Prints the keys of this trie.
     */

    public int size() {
        if (this.left == null) return 1;
        else return left.size() + right.size() + 1;
    }

    public void print() {
        if (this.left != null) left.print();
        if (key != -1) System.out.println(key);
        if (this.right != null) right.print();
    }

    @Override
    public int compareTo(HuffNode other) {
        // this is reversed so I could use the standard PriorityQueue implementation.
        return this.freq - other.getFreq();
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
