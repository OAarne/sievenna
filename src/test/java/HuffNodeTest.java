import org.junit.Test;

import static org.junit.Assert.*;

public class HuffNodeTest {

    @Test
    public void join() {
        HuffNode testNodeLeft = new HuffNode(0, 1, null, null);
        HuffNode testNodeRight = new HuffNode(1, 3, null, null);
        HuffNode joinTestNode = testNodeLeft.join(testNodeRight);
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
    public void getKey() {
    }

    @Test
    public void getLeft() {
    }

    @Test
    public void getRight() {
    }

    @Test
    public void getFreq() {
    }

}