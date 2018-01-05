import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.PriorityQueue;

public class HuffmanCoder {
    public void encode(String input, String output) {
        try {
            // read file ("src/main/resources/nightshot_iso_100.ppm")
            FileInputStream inputStream = new FileInputStream(input);
            byte[] file = new byte[inputStream.available()];
            inputStream.read(file);
            System.out.println("File was read successfully");
            System.out.println("Input file was " + file.length + " bytes.");

            int[] count = new int[256];
            for (int i = 0; i < file.length; i++) {
                count[Byte.toUnsignedInt(file[i])]++;
            }

            HuffNode trieRoot = buildTrie(count);

            String[] huffmanTable = new String[256];
            buildHuffTable(trieRoot, "", huffmanTable);

            for (int i = 0; i < huffmanTable.length; i++) {
                System.out.println(huffmanTable[i]);
            }

            BinaryFileOutput out = new BinaryFileOutput(new FileOutputStream(output));


            writeHuffmanTrie(trieRoot, out);
            System.out.println("The huffman trie was written in " + out.report() + " bits");

            // writeBit the encoded data into the output file.
            for (int i = 0; i < file.length; i++) {
                out.writeBinaryString(huffmanTable[Byte.toUnsignedInt(file[i])]);
            }
            System.out.println("The final file was " + out.report() + " bits");
            System.out.println("that is " + (out.report() / 8) + " bytes");
            double reduction = (out.report() / 8.0) / file.length;
            reduction = 1.0 - reduction;
            System.out.println("File size was reduced by " + reduction + " percent");

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

        while (nodes.size() > 1) {
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
        if (node.getKey() == -1) {
            buildHuffTable(node.getLeft(), prefix + '1', codes);
            buildHuffTable(node.getRight(), prefix + '0', codes);
        } else if (node.getKey() < 256 && node.getKey() >= 0){
            // TODO: remove these checks to optimize
            assert (node.getLeft() == null);
            assert (node.getRight() == null);
            codes[node.getKey()] = prefix;
        } else {
            throw new IllegalArgumentException();
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
