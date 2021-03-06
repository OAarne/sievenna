/**
 * A perfectly run of the mill minimum heap for any Comparable objects.
 * @param <T>
 */

public class MinHeap<T extends Comparable<T>> {
    private T[] array;
    private int size;

    public MinHeap() {
        this.array = (T[]) new Comparable[256];
        this.size = 0;
    }

    public void add(T input) {
        size++;
        if (size > array.length) grow();
        int i = size-1;
        while (i > 0 && array[parent(i)].compareTo(input) > 0) { //array[parent(i)] != null
            array[i] = array[parent(i)];
            i = parent(i);
        }
        array[i] = input;
    }

    /**
     * @return The smallest element in the heap
     */

    public T min() {
        if (size == 0) return null;
        return array[0];
    }

    /**
     * Returns and removes the smallest element in the heap.
     * @return The smallest element in the heap
     */
    public T delMin() {
        if (size == 0) return null;
        T min = array[0];
        array[0] = array[size-1];
        size--;
        heapify(0);
        return min;
    }

    public int size() {
        return this.size;
    }

    private int parent(int i) {
        return (int) Math.floor(i/2.0);
    }

    private int left(int i) {
        return 2 * i;
    }

    private int right(int i) {
        return 2 * i + 1;
    }

    private void heapify(int i) {
        int l = left(i);
        int r = right(i);
        int min;
        if (r <= size()) {
            if (array[r].compareTo(array[l]) > 0) min = l;
            else min = r;
            if (array[i].compareTo(array[min]) > 0) {
                swap(i, min);
                heapify(min);
            }
        } else if (l == size() && array[i].compareTo(array[l]) > 0) swap(i, l);
    }

    private void swap(int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private void grow() {
        T[] newArray = (T[]) new Comparable[array.length*2];
        System.arraycopy(array, 0, newArray, 0 , array.length);
        this.array = newArray;
    }
}
