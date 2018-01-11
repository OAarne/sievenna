import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MinHeapTest {
    @Test
    public void heapTest() {
        MinHeap<Integer> heap = new MinHeap();
        Assert.assertEquals(0, heap.size());
        heap.add(1);
        Assert.assertEquals(1, heap.size());

        heap.add(-1);
        heap.add(2);

        Assert.assertEquals( -1, (int) heap.min());
        Assert.assertEquals(-1, (int) heap.delMin());
        Assert.assertEquals(2, heap.size());
    }
}