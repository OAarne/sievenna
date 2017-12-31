import org.junit.Test;

import static org.junit.Assert.*;

public class sievennaTest {
    @Test
    public static void main(String[] args) {
        assertEquals("hello", "hello");
    }

    @Test
    public void buildTrie() {
        int[] count = new int[256];
        for (int i = 0; i < count.length; i++) {
            count[i] = i;
        }

        HuffNode trieRoot = sievenna.buildTrie(count);

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

        HuffNode trieRoot = sievenna.buildTrie(count);

        // initialize the String array which will hold the codes
        String[] codes = new String[256];

        sievenna.buildHuffTable(trieRoot, "", codes);

        for (int i = 0; i < codes.length; i++) {
            assertNotEquals("", codes[i]);
            assertNotEquals(null, codes[i]);
        }
    }
}