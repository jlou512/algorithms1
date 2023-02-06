
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item>
{
    private class Node
    {
        private Node next;
        private Node prev;
        private Item item;
    }

    private Node head;
    private Node tail;
    private int numOfItems;

    // construct an empty deque
    public Deque()
    {
        head = null;
        tail = null;
        numOfItems = 0;
    }

    // is the deque empty?
    public boolean isEmpty()
    {
        return head == null;
    }

    // return the number of items on the deque
    public int size()
    {
        return numOfItems;
    }

    // add the item to the front
    public void addFirst(Item item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException();
        }

        Node node = new Node();
        node.item = item;
        numOfItems++;

        if (head == null)
        {
            head = node;
            tail = head;
        }
        else
        {
            node.next = head;
            head.prev = node;
            head = node;
        }
    }

    // add the item to the back
    public void addLast(Item item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException();
        }

        Node node = new Node();
        node.item = item;

        if (head == null)
        {
            head = node;
            tail = head;
        }
        else
        {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }

        numOfItems++;
    }

    // remove and return the item from the front
    public Item removeFirst()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException();
        }

        final Node removed = head;
        numOfItems--;

        if (head == null)
        {
            head = null;
            tail = null;
        }
        else
        {
            head = head.next;

            if (head != null)
            {
                head.prev = null;
            }
            else
            {
                tail = null;
            }
        }

        final Item removedItem = removed.item;
        removed.prev = null;
        removed.next = null;
        removed.item = null;

        return removedItem;

    }

    // remove and return the item from the back
    public Item removeLast()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException();
        }

        final Node removed = tail;
        numOfItems--;

        if (tail == null)
        {
            head = null;
            tail = null;
        }
        else
        {
            tail = tail.prev;

            if (tail != null)
            {
                tail.next = null;
            }
            else
            {
                head = null;
            }
        }

        final Item removedItem = removed.item;
        removed.prev = null;
        removed.next = null;
        removed.item = null;

        return removedItem;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator()
    {
        return new Iterator<Item>()
        {
            private Node current = head;

            @Override
            public boolean hasNext()
            {
                return current != null;
            }

            @Override
            public Item next()
            {
                if (current == null)
                {
                    throw new NoSuchElementException();
                }

                final Node node = current;
                current = current.next;
                return node.item;
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    // unit testing (required)
    public static void main(String[] args)
    {
        Deque<Integer> numbers = new Deque<>();

        numbers.addLast(5);
        StdOut.println("removefirst: " + numbers.removeFirst());
        numbers.addFirst(6);
        StdOut.println("removelast: " + numbers.removeLast());
        numbers.addLast(4);
        numbers.addFirst(7);
        numbers.addLast(3);
        StdOut.println("removelast: " + numbers.removeLast());
        numbers.addLast(2);
        numbers.addFirst(8);
        numbers.addLast(1);
        numbers.addFirst(9);
        StdOut.println("removelast: " + numbers.removeLast());

        for (Integer num : numbers)
        {
            StdOut.println(num);
        }

        StdOut.println("size: " + numbers.size());
    }
}