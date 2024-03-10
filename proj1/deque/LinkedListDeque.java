package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private class Node {
        Node prev;
        T item;
        Node next;

        public Node(Node p, T i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private Node sentinel;
    private int size;

    /* constructor */
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }



    public void addFirst(T item) {
        size += 1;

        Node newNode = new Node(sentinel, item, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
    }

    public void addLast(T item) {
        size += 1;

        Node back = new Node(sentinel.prev, item, sentinel);
        sentinel.prev.next = back;
        sentinel.prev = back;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for(Node p = sentinel.next; p != sentinel; p = p.next) {
            System.out.print(p.item + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Node first = sentinel.next;
        first.prev.next = first.next;
        first.next.prev = first.prev;
        return first.item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Node last = sentinel.prev;
        last.next.prev = last.prev;
        last.prev.next = last.next;
        return last.item;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        Node p = sentinel;
        for (int i = 0; i <= index; i += 1) {
            p = p.next;
        }
        return p.item;
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    public class LinkedListDequeIterator implements Iterator<T> {
        private Node p = sentinel.next;
        public boolean hasNext() {
            return (p.next != sentinel);
        }

        public T next() {
            T returnItem = p.item;
            p = p.next;
            return returnItem;
        }
    }

    public boolean equals(Object o) {
        LinkedListDeque<T> lld = (LinkedListDeque<T>) o;
        if (this == o) {
            return true;
        }
        if (lld == null) {
            return false;
        }
        if (!(lld instanceof LinkedListDeque)) {
            return false;
        }
        if (lld.size() != this.size()) {
            return false;
        }
        for (Node p1 = sentinel.next, p2 = lld.sentinel.next; p1 != sentinel; p1 = p1.next, p2 = p2.next) {
            if (p1.item != p2.item) {
                return false;
            }
        }
        return true;
    }

    public T getRecursive(int index) {
        if (index == 0) {
            return sentinel.next.item;
        } else {
            T temp = removeFirst();
            T lld = getRecursive(index - 1);
            addFirst(temp);
            return lld;
        }
    }
}
