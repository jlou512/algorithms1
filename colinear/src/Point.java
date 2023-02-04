
/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point>
{

    private final int x; // x-coordinate of this point
    private final int y; // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y)
    {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw()
    {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point to standard
     * draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that)
    {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point. Formally, if
     * the two points are (x0, y0) and (x1, y1), then the slope is (y1 - y0) / (x1 -
     * x0). For completeness, the slope is defined to be +0.0 if the line segment
     * connecting the two points is horizontal; Double.POSITIVE_INFINITY if the line
     * segment is vertical; and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1)
     * are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that)
    {
        /* YOUR CODE HERE */
        if (this.x == that.x)
        {
            if (this.y == that.y)
            {
                return Double.NEGATIVE_INFINITY;
            }

            return Double.POSITIVE_INFINITY;
        }

        if (this.y == that.y)
        {
            return 0.0;
        }

        return (double) (that.y - this.y) / (that.x - this.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate. Formally,
     * the invoking point (x0, y0) is less than the argument point (x1, y1) if and
     * only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument point (x0
     *         = x1 and y0 = y1); a negative integer if this point is less than the
     *         argument point; and a positive integer if this point is greater than
     *         the argument point
     */
    public int compareTo(Point that)
    {
        /* YOUR CODE HERE */
        if (that.x == this.x && that.y == this.y)
        {
            return 0;
        }

        if (that.y == this.y && that.x < this.x)
        {
            return 1;
        }

        if (that.y == this.y && that.x > this.x)
        {
            return -1;
        }

        return this.y < that.y ? -1 : 1;
    }

    /**
     * Compares two points by the slope they make with this point. The slope is
     * defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder()
    {
        return new Comparator<Point>()
        {
            @Override
            public int compare(Point o1, Point o2)
            {
                if (o1 == null || o2 == null)
                {
                    throw new NullPointerException();
                }
                
                final double m1 = slopeTo(o1);
                final double m2 = slopeTo(o2);

                return Double.compare(m1, m2);
            }
        };
    }

    /**
     * Returns a string representation of this point. This method is provide for
     * debugging; your program should not rely on the format of the string
     * representation.
     *
     * @return a string representation of this point
     */
    public String toString()
    {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args)
    {
        /* YOUR CODE HERE */

        Point p1 = new Point(5, 4);
        Point p2 = new Point(2, 5);
        Point p3 = new Point(6, 6);
        Point p4 = new Point(6, 6);
        Point p5 = new Point(2, 4);
        
        Point p     = new Point(8, 3);
        Point q =                       new Point(3, 3);
        Point   r                         = new Point(6, 3);
        
        System.out.println(p.slopeOrder().compare(q, r));
        System.out.println(p2.compareTo(p1));
        System.out.println(p1.compareTo(p3));
        

        System.out.println(p1.compareTo(p2));
        System.out.println(p2.compareTo(p1));
        System.out.println(p1.compareTo(p3));
        System.out.println(p3.compareTo(p4));
        System.out.println(p1.compareTo(p5));
        System.out.println(p5.compareTo(p1));

    }
}
