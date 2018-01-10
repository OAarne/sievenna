import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

public class HuffmanCoderTest {

    @Test
    public void encodeDecodeImageTest() throws IOException {
        HuffmanCoder.encode("src/main/resources/nightshot_iso_100.ppm", "src/main/resources/output.sie");
        HuffmanCoder.decode("src/main/resources/output.sie", "src/main/resources/output.ppm");

        FileInputStream expected = new FileInputStream(new File("src/main/resources/nightshot_iso_100.ppm"));
        FileInputStream actual = new FileInputStream(new File("src/main/resources/output.ppm"));

//        Assert.assertEquals(expected.available(), actual.available());

        int expectedByte = expected.read();
        int actualByte = actual.read();

        while (expectedByte != -1) {
            Assert.assertEquals(expectedByte, actualByte);
            expectedByte = expected.read();
            actualByte = actual.read();
        }
    }

    @Test
    public void buildTrie() {
        int[] count = new int[256];
        for (int i = 0; i < count.length; i++) {
            count[i] = i;
        }

        HuffNode trieRoot = HuffmanCoder.buildTrie(count);

        assertTrue(trieRoot.getLeft() != null);
        assertTrue(trieRoot.getRight() != null);
        assertEquals(-1, trieRoot.getKey());

    }

    @Test
    public void buildHuffTable() {
        // generate trie to create HuffTable based on.
        int[] count = new int[256];
        for (int i = 0; i < count.length; i++) {
            count[i] = i;
        }

        HuffNode trieRoot = HuffmanCoder.buildTrie(count);

        // initialize the String array which will hold the codes
        String[] codes = new String[256];

        HuffmanCoder.buildHuffTable(trieRoot, "", codes);

        for (int i = 0; i < codes.length; i++) {
            assertNotEquals("", codes[i]);
            assertNotEquals(null, codes[i]);
        }
    }
}