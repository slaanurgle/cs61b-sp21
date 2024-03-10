package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> madComparator;
    public MaxArrayDeque(Comparator<T> c) {
        madComparator = c;
    }

    public T max() {
        T maxItem = null;
        for (T item : this) {
            if (maxItem == null || madComparator.compare(item, maxItem) > 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }

    public T max(Comparator<T> c) {
        /* null */
        T maxItem = null;
        for (T item : this) {
            if (maxItem == null || c.compare(item, maxItem) > 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }



}
