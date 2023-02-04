import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints
{
    private List<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) // finds all line segments containing 4 points\
    {
        if (points == null)
        {
            throw new IllegalArgumentException();
        }

        final int len = points.length;

        segments = new ArrayList<>();

        for (int i = 0; i < len; i++)
        {
            if (points[i] == null)
            {
                throw new IllegalArgumentException();
            }
        }

        final Point[] pointsCopy = Arrays.copyOf(points, len);

        Arrays.sort(pointsCopy);

        for (int i = 0; i < len; i++)
        {
            final Point p1 = pointsCopy[i];

            Point min = null;
            Point max = null;

            for (int j = i + 1; j < len; j++)
            {
                final Point p2 = pointsCopy[j];

                if (p1.compareTo(p2) == 0)
                {
                    throw new IllegalArgumentException();
                }

                final double m1 = p1.slopeTo(p2);

                for (int k = j + 1; k < len; k++)
                {
                    final Point p3 = pointsCopy[k];

                    if (p1.compareTo(p3) == 0 || p2.compareTo(p3) == 0)
                    {
                        throw new IllegalArgumentException();
                    }

                    final double m2 = p2.slopeTo(p3);

                    for (int l = k + 1; l < len; l++)
                    {
                        final Point p4 = pointsCopy[l];

                        if (p1.compareTo(p4) == 0 || p2.compareTo(p4) == 0 || p3.compareTo(p4) == 0)
                        {
                            throw new IllegalArgumentException();
                        }

                        final double m3 = p3.slopeTo(p4);

                        if (Double.compare(m1, m2) == 0 && Double.compare(m1, m3) == 0)
                        {
                            final int cmpMin = min == null ? -1 : p1.compareTo(min);
                            final int cmpMax = max == null ? 1 : p4.compareTo(max);

                            if (cmpMin < 0 || cmpMax > 0)
                            {
                                segments.add(new LineSegment(p1, p4));
                            }

                            if (cmpMin < 0)
                            {
                                min = p1;
                            }

                            if (cmpMax > 0)
                            {
                                max = p4;
                            }
                        }
                    }
                }
            }
        }

    }

    public int numberOfSegments() // the number of line segments
    {
        return segments.size();
    }

    public LineSegment[] segments() // the line segments
    {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    public static void main(String[] args)
    {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++)
        {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points)
        {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        StdOut.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments())
        {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
