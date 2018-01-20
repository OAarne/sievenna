import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class HuffNodeTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private HuffNode testNodeLeft;
    private HuffNode testNodeRight;
    private HuffNode joinTestNode;

    @Before
    public void setUpStream() {
        System.setOut(new PrintStream(outContent));
        testNodeLeft = new HuffNode(0, 1, null, null);
        testNodeRight = new HuffNode(1, 3, null, null);
        joinTestNode = testNodeLeft.join(testNodeRight);
    }

    @Test
    public void join() {
        assertEquals(-1, joinTestNode.getKey());
        assertEquals(joinTestNode.getFreq(), testNodeLeft.getFreq() + testNodeRight.getFreq());
        assertEquals(testNodeLeft, joinTestNode.getLeft());
        assertEquals(testNodeRight, joinTestNode.getRight());
    }

    @Test
    public void compareTo() {
        HuffNode lowerPriorityNode = new HuffNode(1, 1, null, null);
        HuffNode higherPriorityNode = new HuffNode(2, 4, null, null);
        assertTrue(higherPriorityNode.compareTo(lowerPriorityNode) > 0);
    }

    @Test
    public void printTest() {
        joinTestNode.print();
        assertEquals("0\n1\n", outContent.toString());
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
    }
}