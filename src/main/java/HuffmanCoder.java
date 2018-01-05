import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.PriorityQueue;

public class HuffmanCoder {
    public void encode(String input, String output) {
        try {
            // read file
            FileInputStream inputStream = new FileInputStream(input); // "src/main/resources/nightshot_iso_100.ppm"
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

            BinaryFileOutput out = new BinaryFileOutput(new FileOutputStream(output));


            writeHuffmanTrie(trieRoot, out);
            System.out.println("Bits written so far: " + out.report());

            // writeBit the encoded data into the output file.
            for (int i = 0; i < file.length; i++) {
                out.writeBinaryString(huffmanTable[Byte.toUnsignedInt(file[i])]);
            }

//            out.flush();

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

    /**
     * Traverses the trie in preorder and writes a 0 for each not-leaf node,
     * and for each leaf a 1 followed by
     * the byte to be encoded as an 8-bit unsigned integer.
     * @param node
     * @param out
     */

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
