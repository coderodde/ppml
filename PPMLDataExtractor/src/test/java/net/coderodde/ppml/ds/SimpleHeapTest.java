package net.coderodde.ppml.ds;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class SimpleHeapTest {

    private SimpleHeap<Integer, Integer> heap = new SimpleHeap<>();
    
    @Before
    public void before() {
        heap.clear();
    }
    
    @Test
    public void test1() {
        final int N = 10;
        assertEquals(0, heap.size());
        
        for (int i = 0; i < N; ++i) {
            heap.insert(i, i);
        }
        
        assertEquals(N, heap.size());
        
        for (int i = 0; heap.size() > 0; ++i) {
            assertEquals(i, (int) heap.extractMinimum());
        }
        
        assertEquals(0, heap.size());
        
        for (int i = 9; i >= 0; --i) {
            heap.insert(i, i);
        }
        
        assertEquals(N, heap.size());
        
        for (int i = 0; heap.size() > 0; ++i) {
            assertEquals(i, (int) heap.extractMinimum());
        }
        
        assertEquals(0, heap.size());
        
        heap.insert(3, 3);
        heap.insert(4, 4);
        heap.insert(5, 5);
        heap.insert(2, 2);
        heap.insert(1, 1);
        
        assertEquals(5, heap.size());
        
        final List<Integer> list = heap.snapshot();
        
        for (int i = 1; i <= 5; ++i) {
            assertEquals(i, (int) heap.extractMinimum());
        }
        
        for (int i = 0; i < list.size(); ++i) {
            assertEquals(i + 1, (int) list.get(i));
        }
        
        assertEquals(0, heap.size());
        
        heap.insert(1, 1);
        heap.insert(2, 2);
        
        assertEquals(2, heap.size());
        
        heap.clear();
        
        assertEquals(0, heap.size());
    }
}
