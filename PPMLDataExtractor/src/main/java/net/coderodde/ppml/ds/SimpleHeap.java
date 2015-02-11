package net.coderodde.ppml.ds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * This class wraps <code>java.util.PriorityQueue</code> into a small nifty API.
 * 
 * @param <E> the type of actual elements.
 * @param <P> the type of priority keys, must be <code>Comparable</code>.
 */
public class SimpleHeap<E, P extends Comparable<? super P>> {

    private static class HeapEntry<E, P extends Comparable<? super P>> 
    implements Comparable<HeapEntry<E, P>>{
        E element;
        P priority;
        
        HeapEntry(final E element, final P priority) {
            this.element = element;
            this.priority = priority;
        }

        @Override
        public int compareTo(final HeapEntry<E, P> o) {
            return this.priority.compareTo(o.priority);
        }
    }
    
    private final Queue<HeapEntry> heap;
    
    /**
     * Constructs a new empty heap.
     */
    public SimpleHeap() {
        this.heap = new PriorityQueue<>();
    }
    
    /**
     * Removes from this heap the element with largest priority (lowest priority
     * key).
     * 
     * @return the head of this heap.
     * 
     * @throws NoSuchElementException if this heap is empty.
     */
    public E extractMinimum() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Extracting from an empty heap.");
        }
        
        return (E) heap.poll().element;
    }
    
    /**
     * Inserts an element into this heap.
     * 
     * @param element  the actual element to insert.
     * @param priority the priority key of the element.
     */
    public void insert(final E element, final P priority) {
        heap.add(new HeapEntry<>(element, priority));
    }
    
    /**
     * Returns the size of this heap.
     * 
     * @return the size of this heap.
     */
    public int size() {
        return heap.size();
    }
    
    /**
     * Returns the list containing the elements in the ascending order by 
     * priority keys.
     * 
     * @return the contents of this heap.
     */
    public List<E> snapshot() {
        final List<HeapEntry<E, P>> entryList = new ArrayList<>(size());
        
        for (final HeapEntry<E, P> entry : heap) {
            entryList.add(entry);
        }
        
        Collections.sort(entryList);
        
        final List<E> ret = new ArrayList<>(size());
        
        for (final HeapEntry<E, P> entry : entryList) {
            ret.add(entry.element);
        }
        
        return ret;
    }
    
    public void clear() {
        heap.clear();
    }
}
