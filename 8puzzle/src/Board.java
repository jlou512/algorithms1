import java.util.Arrays;
import java.util.Iterator;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public final class Board
{
    private int[][] tiles;
    private int dimension;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles)
    {
        if (tiles == null || tiles.length < 2 || tiles.length >= 128)
        {
            throw new IllegalArgumentException();
        }

        dimension = tiles.length;

        // validate each number in a tile
        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                final int tile = tiles[i][j];

                if (tile < 0 || tile > (dimension * dimension - 1))
                {
                    throw new IllegalArgumentException();
                }
            }
        }

        this.tiles = copyMatrix(tiles);
    }

    // string representation of this board
    public String toString()
    {
        final StringBuilder buf = new StringBuilder();

        buf.append(dimension);
        buf.append("\n");

        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                buf.append(String.format("%2d ", tiles[i][j]));
            }

            buf.append("\n");
        }

        return buf.toString();
    }

    // board dimension n
    public int dimension()
    {
        return dimension;
    }

    // number of tiles out of place
    public int hamming()
    {
        int outOfPlace = 0;

        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                final int tile = tiles[i][j];
                final int target = i * dimension + j + 1;

                if (target == dimension * dimension)
                {
                    continue;
                }

                if (tile - target != 0)
                {
                    outOfPlace++;
                }
            }
        }

        return outOfPlace;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {
        int sum = 0;

        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                final int tile = tiles[i][j];

                if (tile == 0)
                {
                    continue;
                }

                final int iRow = (tile - 1) / dimension;
                final int iCol = (tile - 1) % dimension;

                sum += Math.abs(iRow - i) + Math.abs(iCol - j);
            }
        }

        return sum;
    }

    // is this board the goal board?
    public boolean isGoal()
    {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y)
    {
        if (y == this)
        {
            return true;
        }

        if (y == null || !(y instanceof Board))
        {
            return false;
        }

        final Board other = (Board) y;

        return Arrays.deepEquals(this.tiles, other.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        int zeroi = 0;
        int zeroj = 0;
        final Bag<Board> neighbors = new Bag<>();

        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                final int tile = tiles[i][j];

                if (tile == 0)
                {
                    zeroi = i;
                    zeroj = j;
                    i = dimension;
                    j = dimension;
                }
            }
        }

        addNeighbors(neighbors, zeroi, zeroj);

        return neighbors;
    }

    /**
     * Add a neighbor depending on where the empty square is located in the puzzle.
     * 
     * @param neighbors
     * @param zeroi
     * @param zeroj
     */
    private void addNeighbors(Bag<Board> neighbors, int zeroi, int zeroj)
    {
        final int last = dimension - 1;

        if (zeroi > 0 && zeroj > 0 && zeroi < last && zeroj < last)
        {// ok
            addNeighbor(neighbors, zeroi, zeroj, -1, 0);
            addNeighbor(neighbors, zeroi, zeroj, 1, 0);
            addNeighbor(neighbors, zeroi, zeroj, 0, -1);
            addNeighbor(neighbors, zeroi, zeroj, 0, 1);
        }
        else if (zeroi == 0 && zeroj == 0)
        {// ok
            addNeighbor(neighbors, zeroi, zeroj, 1, 0);
            addNeighbor(neighbors, zeroi, zeroj, 0, 1);
        }
        else if (zeroi == 0 && zeroj == last)
        {// ok
            addNeighbor(neighbors, zeroi, zeroj, 0, -1);
            addNeighbor(neighbors, zeroi, zeroj, 1, 0);
        }
        else if (zeroi == last && zeroj == 0)
        {// ok
            addNeighbor(neighbors, zeroi, zeroj, 0, 1);
            addNeighbor(neighbors, zeroi, zeroj, -1, 0);
        }
        else if (zeroi == last && zeroj == last)
        { // ok
            addNeighbor(neighbors, zeroi, zeroj, 0, -1);
            addNeighbor(neighbors, zeroi, zeroj, -1, 0);
        }
        else if (zeroi == 0)
        { // ok
            addNeighbor(neighbors, zeroi, zeroj, 1, 0);
            addNeighbor(neighbors, zeroi, zeroj, 0, -1);
            addNeighbor(neighbors, zeroi, zeroj, 0, 1);
        }
        else if (zeroj == 0)
        { // ok
            addNeighbor(neighbors, zeroi, zeroj, -1, 0);
            addNeighbor(neighbors, zeroi, zeroj, 1, 0);
            addNeighbor(neighbors, zeroi, zeroj, 0, 1);
        }
        else if (zeroi == last)
        { // ok
            addNeighbor(neighbors, zeroi, zeroj, -1, 0);
            addNeighbor(neighbors, zeroi, zeroj, 0, -1);
            addNeighbor(neighbors, zeroi, zeroj, 0, 1);
        }
        else
        {// ok
            addNeighbor(neighbors, zeroi, zeroj, -1, 0);
            addNeighbor(neighbors, zeroi, zeroj, 1, 0);
            addNeighbor(neighbors, zeroi, zeroj, 0, -1);
        }
    }

    private void addNeighbor(Bag<Board> neighbors, int zeroX, int zeroY, int offsetX, int offsetY)
    {
        final int[][] copy = copyMatrix(tiles);
        swap(copy, zeroX, zeroY, zeroX + offsetX, zeroY + offsetY);
        neighbors.add(new Board(copy));
    }

    private int[][] copyMatrix(int[][] matrix)
    {
        int[][] copy = new int[dimension][dimension];

        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                copy[i][j] = matrix[i][j];
            }
        }

        return copy;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin()
    {
        int[][] copy = new int[dimension][dimension];
        int pos1 = -1, pos2 = -1, pos3 = -1, pos4 = -1;

        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                copy[i][j] = tiles[i][j];

                if (pos1 < 0 && copy[i][j] != 0)
                {
                    pos1 = i;
                    pos2 = j;
                }
                else if (pos3 < 0 && copy[i][j] != 0)
                {
                    pos3 = i;
                    pos4 = j;
                }
            }
        }

        int temp = copy[pos1][pos2];
        copy[pos1][pos2] = copy[pos3][pos4];
        copy[pos3][pos4] = temp;

        return new Board(copy);
    }

    private void swap(int[][] arr, int i, int j, int k, int l)
    {
        final int temp = arr[i][j];
        arr[i][j] = arr[k][l];
        arr[k][l] = temp;
    }

    // unit testing (not graded)
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        Board initial2 = new Board(tiles);

        StdOut.println(initial);
        StdOut.println("Hamming: " + initial.hamming());
        StdOut.println("Manhattan : " + initial.manhattan());
        StdOut.println("Twin : " + initial.twin());
        Iterable<Board> neighbors = initial.neighbors();

        Iterator<Board> iterator = neighbors.iterator();

        while (iterator.hasNext())
        {
            Board neighbor = iterator.next();
            StdOut.println("Neighbor : " + neighbor);
        }

        StdOut.println(initial.equals(initial2));
    }
}