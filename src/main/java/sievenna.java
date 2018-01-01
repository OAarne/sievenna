import java.io.*;
import java.util.PriorityQueue;

/**
 * The tool takes an input file path and an output file path as its arguments.
 * A Huffman coded version of the input file will be written to the output file path.
 */

public class sievenna {

    public static void main(String[] args) {
        // TODO: remove this when unneeded.
        // this code exists for testing purposes only
        Integer testNum = 200;
        byte testByte = testNum.byteValue();
        System.out.println("Value: " + testNum);
        System.out.println("Default: " + testByte);
        System.out.println("toUnsigned: " + Byte.toUnsignedInt(testByte));
        System.out.println(Integer.toBinaryString(testNum));
        System.out.println("three in binary is " + Integer.toBinaryString(3));

        try {
            FileInputStream inputStream = new FileInputStream(args[0]); // "src/main/resources/nightshot_iso_100.ppm"
            byte[] file = new byte[inputStream.available()];
            inputStream.read(file);
            System.out.println("File was read successfully");

            int[] count = new int[256];
            for (int i = 0; i < file.length; i++) {
                count[Byte.toUnsignedInt(file[i])]++;
            }

            HuffNode trieRoot = buildTrie(count);

            String[] huffmanTable = new String[256];
            buildHuffTable(trieRoot, "", huffmanTable);

            BinaryFileOutput out = new BinaryFileOutput(new FileOutputStream(args[1]));


            writeHuffmanTrie(trieRoot, out);

            // writeBit the encoded data into the output file.
            for (int i = 0; i < file.length; i++) {
                out.writeBinaryString(huffmanTable[Byte.toUnsignedInt(file[i])]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the root node of a full trie for allocating the Huffman codes.
     * @param count
     * @return
     */

    public static HuffNode buildTrie(int[] count) {

        // TODO: replace this PriorityQueue BS with my own implementation

        PriorityQueue<HuffNode> nodes = new PriorityQueue<>();

        for (int i = 0; i < count.length; i++) {
            nodes.add(new HuffNode(i, count[i], null, null));
        }

        while (nodes.size() != 1) {
            HuffNode left = nodes.poll();
            HuffNode right = nodes.poll();
            nodes.add(left.join(right));
        }
        return nodes.poll();
    }

    /**
     * Returns a String array containing the huffman codes of each byte,
     * allocated according to the trie given as parameter.
     * @param node
     * @param prefix
     * @param codes
     * @return huffmanTable
     */

    public static void buildHuffTable(HuffNode node, String prefix, String[] codes) {
        if (node.getKey() != -1) {
            codes[node.getKey()] = prefix;
        } else {
            buildHuffTable(node.getLeft(), prefix + '1', codes);
            buildHuffTable(node.getRight(), prefix + '0', codes);
        }
    }

    public static void writeHuffmanTrie(HuffNode node, BinaryFileOutput out) {
        if (node.getKey() == -1) {
            out.writeBit(false);
            writeHuffmanTrie(node.getLeft(), out);
            writeHuffmanTrie(node.getRight(), out);
        } else {
            out.writeBit(true);
            out.write8bitInt(node.getKey());
        }
    }
}
