package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K ,V> {
    private BSTNode root;

    private class BSTNode {
        public BSTNode left;
        public BSTNode right;
        public K key;
        public V value;
        public int size;
        public BSTNode(K k, V v) {
            left = null;
            right = null;
            key = k;
            value = v;
            size = 1;
        }

    }

    private class BSTMapIterator implements Iterator<K> {
        private int index = 1; // start from 1 because the size is at least 1.
        private int curr;
        public boolean hasNext() {
            return index <= size();
        }

        public K next() {
            curr = index;
            return getNext(root);
        }

        private K getNext(BSTNode node) {
            if (node.left == null) {
                if (curr == 1) {
                    index += 1;
                    return node.key;
                } else {
                    curr = curr - 1;
                    return getNext(node.right);
                }
            } else {
                if (curr <= size(node.left)) {
                    return getNext(node.left);
                } else if (curr == size(node.left) + 1) {
                    index += 1;
                    return node.key;
                } else {
                    curr = curr - size(node.left) - 1;
                    return getNext(node.right);
                }
            }
        }
    }
    public BSTMap() {
    }
    public void clear() {
        root = null;
    }

    public boolean containsKey(K key) {
        return containsKey(root, key);
    }

    private boolean containsKey(BSTNode node, K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (node == null) {
            return false;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return containsKey(node.left, key);
        } else if (cmp > 0) {
            return containsKey(node.right, key);
        } else {
            return true;
        }
    }
    /* return the value of the specified key
     * throw IllegalArgumentException if key is null
     * return null if the key does not exist.
     */
    public V get(K key) {
        return get(root, key);
    }

    /* Helper method of get(K key) */
    private V get(BSTNode node, K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }

    public int size() {
        return size(root);
    }

    public int size(BSTNode node) {
        if (node == null) {
            return 0;
        }
        return node.size;
    }
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    /* Helper method. Return a new tree(node) after put */
    private BSTNode put(BSTNode node, K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with a null key");
        }
        if (node == null) {
            node = new BSTNode(key, value);
            return node;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        }
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    @Override
    public String toString() {
        return treeToString(root);
    }

    private String treeToString(BSTNode node) {
        if (node == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(treeToString(node.left));
        sb.append(node.key.toString());
        sb.append(" ");
        sb.append(treeToString(node.right));
        return sb.toString();
    }
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }


    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        V val = get(key);
        root = remove(root, key);
        return val;
    }

    /* return the tree after remove */
    private BSTNode remove(BSTNode node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            node.right = remove(node.right, key);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            BSTNode temp = node;
            node = min(temp.right);
            node.right = removeMin(temp.right);
            node.left = temp.left;
        }
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    /* return the tree where the min element is */
    private BSTNode min(BSTNode node) {
        if (node.left == null) {
            return node;
        }
        return min(node.left);
    }
    /* return the tree after remove. */
    private BSTNode removeMin(BSTNode node) {
        if (node == null) {
            return null;
        }
        if (node.left == null) {
            return node.right;
        }
        node.left = removeMin(node.left);
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }
    public V remove(K key, V value) {
        if (!containsKey(key)) {
            return null;
        }
        V val = get(key);
        if (val != value) {
            return null;
        }
        root = remove(root, key);
        return val;
    }

    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }
}
