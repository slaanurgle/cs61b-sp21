package deque;

import java.util.Iterator;
public class ArrayDeque<T> implements Iterable<T>, Deque<T>{
    private int size;
    private int front;
    private int back;
    private T[] items;

    private int FACTOR = 2;
    private int STARTSIZE = 16;
    private double RATIO = 0.25;


    public ArrayDeque(){
        size = 0;
        front = 4;
        back = 3;
        items = (T []) new Object[8];
    }

    /** Change front to the x position front of the current position.
     *  x cannot be larger than size - 1, otherwise there may be an exception. */
    private void changeFront(int x) {
        int l = items.length;
        front = (front - x + l) % l;
    }
    /** Change back to the x position back of the current position.
     *  x cannot be larger than items.length - 1, otherwise there may be an exception. */
    private void changeBack(int x) {
        int l = items.length;
        back = (back + x + l) % l;
    }

    private void resize(int capacity) {
        int l = items.length;
        T[] a = (T []) new Object[capacity];
        int i = front;
        int insertIndex = 0;
        while (i != back) {
            a[insertIndex] = items[i];
            insertIndex += 1;
            if (i != l - 1) {
                i += 1;
            } else {
                i = i + 1 - l;
            }
        }
        a[insertIndex] = items[i];
        items = a;
        front = 0;
        back = size - 1;
    }
    public void addFirst(T item) {

        int l = items.length;
        if (size + 1 > l) {
            resize(l * FACTOR);
        }
        changeFront(1);
        items[front] = item;

        size += 1;
    }

    public void addLast(T item) {

        int l = items.length;
        if (size + 1 > l) {
            resize(l * FACTOR);
        }
        changeBack(1);
        items[back] = item;

        size += 1;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int i = front;
        int l = items.length;
        while (i != back) {
            System.out.print(items[i] + " ");
            if(i != l - 1) {
                i += 1;
            } else {
                i = i + 1 - l;
            }
        }
        System.out.println(items[i]);
    }

    /** Check whether the items array is too long */
    private void checkLowUsage(){
        int l = items.length;
        if (l >= 16 && size <= (int) Math.round(l * RATIO) ) {
            resize(l / 2);
        }
    }
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T removedItem = items[front];
        changeFront(-1);
        checkLowUsage();

        return removedItem;
    }

    public T removeLast() {
        if (size ==  0) {
            return null;
        }
        size -= 1;
        T removedItem = items[back];
        changeBack(-1);
        checkLowUsage();
        return removedItem;
    }

    public T get(int index) {
        int getIndex = (front + index + items.length) % items.length;
        return items[getIndex];
    }


    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    public class ArrayDequeIterator implements Iterator<T> {
        private int wizpos = front;
        private int cnt = 0;
        public boolean hasNext() {
            return cnt < size;
        }

        public T next() {
            T returnItem = items[wizpos];
            wizpos = (wizpos + items.length + 1) % items.length;
            cnt += 1;
            return returnItem;
        }
    }

}
