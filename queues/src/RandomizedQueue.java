import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item>
{
    private static final int INIT_CAPACITY = 8;
    private Item[] items;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue()
    {
        items = (Item[]) new Object[INIT_CAPACITY];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty()
    {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size()
    {
        return size;
    }

    // add the item
    public void enqueue(Item item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException();
        }

        if (size == items.length)
        {
            resizeArray(items.length * 2);
        }

        items[size++] = item;
    }

    private void resizeArray(int newSize)
    {
        Item[] copy = (Item[]) new Object[newSize];

        for (int i = 0; i < size; i++)
        {
            copy[i] = items[i];
        }

        items = copy;
    }

    // remove and return a random item
    public Item dequeue()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException();
        }

        if (size == 1)
        {
            try
            {
                return items[0];
            }
            finally
            {
                items[0] = null;
                size--;
            }
        }

        final int randomIndex = StdRandom.uniformInt(size);
        final Item removed = items[randomIndex];
        items[randomIndex] = items[size - 1];
        items[size - 1] = null;
        size--;

        // shrink size of array if necessary
        if (size > 0 && size == items.length / 4)
        {
            resizeArray(items.length / 2);
        }

        return removed;
    }

    // return a random item (but do not remove it)
    public Item sample()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException();
        }

        final int randomIndex = StdRandom.uniformInt(size);
        return items[randomIndex];
    }

    private final class RQIterator implements Iterator<Item>
    {
        private int ctr = 0;
        private Item[] copy;

        public RQIterator()
        {
            copy = (Item[]) new Object[size()];

            for (int i = 0; i < size(); i++)
            {
                copy[i] = items[i];
            }

            StdRandom.shuffle(copy, 0, size());
        }

        @Override
        public boolean hasNext()
        {
            return ctr < size;
        }

        @Override
        public Item next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }

            return copy[ctr++];
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator()
    {
        return new RQIterator();
    }

    // unit testing (required)
    public static void main(String[] args)
    {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        int n = 20;
        for (int i = 0; i < n; i++)
        {
            queue.enqueue(i);
        }

        for (int a : queue)
        {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }

        StdOut.println("size: " + queue.size());
        StdOut.println();
        StdOut.println(queue.dequeue());
        queue.enqueue(10);
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.sample());
    }
}