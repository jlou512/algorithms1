import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation
{
    public static void main(String[] args)
    {
        if (args.length < 1)
        {
            throw new IllegalArgumentException();
        }

        Integer k = Integer.valueOf(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();

        while (!StdIn.isEmpty())
        {
            String item = StdIn.readString();
            queue.enqueue(item);
        }

        for (int i = 0; i < k; i++)
        {
            String item = queue.dequeue();
            StdOut.println(item);
        }
    }
}
