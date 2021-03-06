import java.io.*;
import java.util.logging.Logger;

/**
 * This class houses the functinoality for encoding and decoding files.
 */

public class HuffmanCoder {

    private final static Logger LOGGER = Logger.getLogger((HuffmanCoder.class.getName()));
    /**
     * Writes the file in the first path to second path in a huffman coded form
     * @param input
     * @param output
     */

    public static void encode(String input, String output) {

        // read file ("src/main/resources/nightshot_iso_100.ppm")
        long startTime = System.nanoTime();
        MinimalistTimer timer = new MinimalistTimer();
        FileInputStream inputStream;
        byte[] file = null;
        try {
            inputStream = new FileInputStream(input);
            file = new byte[inputStream.available()];
            inputStream.read(file);
        } catch (IOException e) {
            LOGGER.info("File could not be read.");
            e.printStackTrace();
        }

        LOGGER.info(file.length + " byte file was read in " + timer.time()/1000000.0 + " ms");

        // count byte frequencies
        int[] count = new int[256];
        for (int i = 0; i < file.length; i++) {
            count[Byte.toUnsignedInt(file[i])]++;
        }

        LOGGER.info("Model built in " + timer.time()/1000000.0 + " ms");

        HuffNode trieRoot = buildTrie(count);
        LOGGER.info("Constructed huffman trie with total " + trieRoot.size() + " nodes in " + timer.time()/1000000.0 + " ms");

        // build code table
        String[] huffmanTable = new String[256];
        buildHuffTable(trieRoot, "", huffmanTable);

        // Initialize output
        BinaryFileOutput out = null;
        try {
            out = new BinaryFileOutput(new FileOutputStream(output));
        } catch (FileNotFoundException e) {
            LOGGER.info("Could not write to output path.");
            e.printStackTrace();
        }

        try {
            writeHuffmanTrie(trieRoot, out);
            LOGGER.info("The huffman trie was written in " + out.report() + " bits in " + timer.time()/1000000.0 + " ms");

            // write 32-bit int to indicate number of coded bytes to follow
            int before = out.report();
            out.write32bitInt(file.length);
            assert out.report() - before == 32;

            // Write the encoded data into the output file
            for (int i = 0; i < file.length; i++) {
                int key = Byte.toUnsignedInt(file[i]);
                assert (key >= 0 && key <= 255);
                out.writeBinaryString(huffmanTable[key]);
            }


            LOGGER.info("File body written in " + timer.time()/1000000.0 + " ms");

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOGGER.info("Compression took " + (System.nanoTime() - startTime)/1000000.0 + " ms");
        LOGGER.info("The final file was " + (out.report() / 8) + " bytes");
        double reduction = (out.report() / 8.0) / file.length;
        reduction = 1.0 - reduction;
        LOGGER.info("File size was reduced by " + reduction + " percent");
        LOGGER.info("Compression ratio was " + file.length/(out.report() /8.0)) ;

    }

    /**
     * Reads a Huffman coded file from the first path and writes a decoded file to the output path.
     * @param inputPath
     * @param outputPath
     */

    public static void decode(String inputPath, String outputPath) {
        // initialize input
        BinaryFileInput binput = null;
        int available = -1;
        try {
            FileInputStream inputStream = new FileInputStream(inputPath);
            binput = new BinaryFileInput(inputStream);
            LOGGER.info("File " + inputPath + " was read successfully");
            available = binput.available();
            LOGGER.info("File has " + available + " bytes.");
        } catch (IOException e) {
            LOGGER.info("File could not be read.");
            e.printStackTrace();
        }

        HuffNode trieRoot = null;

        try {
            trieRoot = readHuffmanTrie(binput);
            LOGGER.info("Trie was successfully read");
        } catch (IOException e) {
            LOGGER.info("Trie could not be constructed.");
            e.printStackTrace();
        }

        int byteCount = 0;

        try {
            byteCount = binput.read32bitInt();
        } catch (IOException e) {
            LOGGER.info("Loller.");
            e.printStackTrace();
        }

        assert byteCount >= 0;
        LOGGER.info("Beginning to decode " + byteCount + " bytes.");

        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputPath));
            for (int i = 0; i < byteCount; i++) {
                HuffNode node = trieRoot;
                trieDecodeLoop:
                while (true) {
                    if (!binput.readBit()) {
                        node = node.getRight();
                    } else {
                        node = node.getLeft();
                    }
                    if (node.getKey() != -1) {
                        outputStream.write(node.getKey());
                        break trieDecodeLoop;
                    }
                }
                if (available < binput.report()/8) throw new IllegalStateException();
            }
            outputStream.flush();
            outputStream.close();
            binput.close();
            LOGGER.info("File was successfully decoded and written.");

        } catch (IOException e) {
            LOGGER.info("File body could not be read.");
            e.printStackTrace();
        }
    }

    /**
     * Returns the root node of a full trie for allocating the Huffman codes based on array of byte frequencies.
     * @param count
     * @return Trie root
     */

    public static HuffNode buildTrie(int[] count) {

        MinHeap<HuffNode> minHeap = new MinHeap();

        for (int i = 0; i < count.length; i++) {
            minHeap.add(new HuffNode(i, count[i], null, null));
        }

        while (minHeap.size() > 1) {
            HuffNode left = minHeap.delMin();
            HuffNode right = minHeap.delMin();
            minHeap.add(left.join(right));
        }

        return minHeap.min();
    }

    /**
     * Returns a String array containing the huffman codes of each byte,
     * allocated according to the trie given as parameter.
     * @param node
     * @param prefix
     * @param codes
     */

    public static void buildHuffTable(HuffNode node, String prefix, String[] codes) {
        if (node.getKey() == -1) {
            buildHuffTable(node.getLeft(), prefix + '1', codes);
            buildHuffTable(node.getRight(), prefix + '0', codes);
        } else if (node.getKey() < 256 && node.getKey() >= 0){
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

    public static void writeHuffmanTrie(HuffNode node, BinaryFileOutput out) throws IOException {
        if (node.getKey() == -1) {
            out.writeBit(false);
            writeHuffmanTrie(node.getLeft(), out);
            writeHuffmanTrie(node.getRight(), out);
        } else {
            out.writeBit(true);
            out.write8bitInt(node.getKey());
        }
    }

    /**
     * Reads a preorder encoded Huffman trie from input
     * @param binput
     * @return Root node of Huffman trie
     */

    public static HuffNode readHuffmanTrie(BinaryFileInput binput) throws IOException {
        if (binput.readBit()) {
            int key = binput.read8bitInt();
            return new HuffNode(key, 0, null, null);
        } else {
            HuffNode left = readHuffmanTrie(binput);
            HuffNode right = readHuffmanTrie(binput);
            return left.join(right);
        }
    }
}
