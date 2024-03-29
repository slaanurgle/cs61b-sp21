package hashmap;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size; // holds the num of non-empty buckets
    private int tableSize; // holds the length of the buckets
    private double loadFactor;
    private static final int DEFAULT_INIT_SIZE = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private final double RESIZE_FACTOR = 2;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(DEFAULT_INIT_SIZE, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, DEFAULT_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize); // the buckets will be full of null initially
        loadFactor = maxLoad;
        size = 0;
        tableSize = initialSize;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        /* in this hashMap, we use LinkedList to implement. */
        return new LinkedList<Node>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection[] newTable =  new Collection[tableSize];
        for (int i = 0; i < tableSize; i += 1) {
            newTable[i] = createBucket();
        }
        return newTable;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    private int getHash(K key) {
        return Math.abs(key.hashCode()) % tableSize;
    }
    public void clear() {
        buckets = createTable(DEFAULT_INIT_SIZE);
        size = 0;
        tableSize = DEFAULT_INIT_SIZE;
    }

    public boolean containsKey(K key) {
        int hash = getHash(key);
        Collection<Node> bucket = buckets[hash];
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public V get(K key) {
        int hash = getHash(key);
        Collection<Node> bucket = buckets[hash];
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    public void put(K key, V value) {
        //check whether key exists, if exists, update it
        int hash = getHash(key);
        for (Node node : buckets[hash]) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }
        // check whether the bucket is beyond load factor
        if (size + 1 > tableSize * loadFactor) {
            resize((int) Math.round(tableSize * RESIZE_FACTOR));
        }
        // add the Node into the buckets
        Node newNode = createNode(key, value);
        hash = getHash(key);
        buckets[hash].add(newNode);
        size += 1;
    }

    private void resize(int newSize) {
        Collection[] newTable = createTable(newSize);
        tableSize = newSize;
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                int hash = getHash(node.key);
                newTable[hash].add(node);
            }
        }
        buckets = newTable;
    }

    public Set<K> keySet() {
        Set<K> returnset = new HashSet<>();
        for (K key : this) {
            returnset.add(key);
        }
        return returnset;
    }

    public V remove(K key) {
        int hash = getHash(key);
        for (Node node : buckets[hash]) {
            if (node.key == key) {
                V val = node.value;
                buckets[hash].remove(node);
                return val;
            }
        }
        return null;
    }

    public V remove(K key, V value) {
        int hash = getHash(key);
        for (Node node : buckets[hash]) {
            if (node.key == key && node.value == value) {
                buckets[hash].remove(node);
                return value;
            }
        }
        return null;
    }

    public Iterator<K> iterator() {
        return new myHashMapIterator();
    }

    public class myHashMapIterator implements Iterator<K> {
        private int pos = 0; // holds the number of elements that has been visited
        private int index = 0; // holds the current index of bucket
        private Iterator<Node> bucketIterator = buckets[0].iterator();
        public boolean hasNext() {
            return pos < size;
        }

        public K next() {
            Collection<Node> curr;
            while (!bucketIterator.hasNext()) {
                index += 1;
                curr = buckets[index];
                bucketIterator = curr.iterator();
            }
            pos += 1;
            return bucketIterator.next().key;
        }
    }

}
