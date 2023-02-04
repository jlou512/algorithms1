import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints
{
    private List<LineSegment> segments;

    public FastCollinearPoints(Point[] points) // finds all line segments containing 4 or more points
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

        for (int i = 0; i < len; i++)
        {
            Arrays.sort(pointsCopy);
            final Point p0 = pointsCopy[i];

            if (p0 == null)
            {
                throw new IllegalArgumentException();
            }

            Arrays.sort(pointsCopy, p0.slopeOrder());

            addLineSegments(pointsCopy, p0);
        }
    }

    private void addLineSegments(Point[] points, Point p0)
    {
        final int len = points.length;
        int ctr = 0;

        Bag<Point> segmentPoints = new Bag<>();

        for (int j = 1; j < len; j++)
        {
            final Point p1 = points[j];

            if (p1 == null || p0.compareTo(p1) == 0)
            {
                throw new IllegalArgumentException();
            }

            final double m = p0.slopeTo(p1);

            int next = j + 1;

            if (next < len)
            {
                Point nextPoint = points[next];

                if (nextPoint == null)
                {
                    throw new IllegalArgumentException();
                }

                double nextSlope = p0.slopeTo(nextPoint);

                if (Double.compare(m, nextSlope) == 0)
                {
                    ctr++;
                    segmentPoints.add(p1);
                    segmentPoints.add(nextPoint);
                }
                else if (ctr >= 2)
                {
                    addSegment(p0, p1, segmentPoints);
                    ctr = 0;
                    segmentPoints = new Bag<>();
                }
                else
                {
                    ctr = 0;
                    segmentPoints = new Bag<>();
                }
            }
            else if (ctr >= 2)
            {
                addSegment(p0, p1, segmentPoints);
                ctr = 0;
                segmentPoints = new Bag<>();
            }
        }
    }

    private void addSegment(Point p0, Point p1, Bag<Point> segmentPoints)
    {
        final Iterator<Point> iter = segmentPoints.iterator();
        boolean addSegment = true;

        while (iter.hasNext())
        {
            Point nextPoint = iter.next();

            if (p0.compareTo(nextPoint) > 0)
            {
                addSegment = false;
                break;
            }
        }

        if (addSegment)
        {
            segments.add(new LineSegment(p0, p1));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdOut.println(collinear.numberOfSegments());

        for (LineSegment segment : collinear.segments())
        {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
