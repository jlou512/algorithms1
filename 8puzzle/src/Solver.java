import java.util.Comparator;
import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver
{
    private SearchNode candidateNode = null;

    private final class ManhattanPriorityComparator implements Comparator<SearchNode>
    {
        @Override
        public int compare(Solver.SearchNode searchNode1, Solver.SearchNode searchNode2)
        {
            if (searchNode1 == null || searchNode2 == null)
            {
                throw new NullPointerException();
            }

            final int mp1 = searchNode1.manhattan + searchNode1.movesToCurrent;
            final int mp2 = searchNode2.manhattan + searchNode2.movesToCurrent;

            return Integer.compare(mp1, mp2);
        }
    }

    private final class SearchNode
    {
        private Board currentBoard;
        private SearchNode previousNode;
        private int movesToCurrent;
        private int manhattan;

        public SearchNode(Board current, SearchNode previous, int movesToCurrent)
        {
            this.currentBoard = current;
            this.previousNode = previous;
            this.movesToCurrent = movesToCurrent;
            this.manhattan = currentBoard.manhattan();
        }

        public boolean equals(Object other)
        {
            if (other == null)
            {
                return false;
            }

            if (other == null || !(other instanceof SearchNode))
            {
                return false;
            }

            final SearchNode otherNode = (SearchNode) other;

            return currentBoard.equals(otherNode.currentBoard) && previousNode.equals(otherNode.previousNode)
                    && movesToCurrent == otherNode.movesToCurrent;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        if (initial == null)
        {
            throw new IllegalArgumentException();
        }

        final ManhattanPriorityComparator manhattanPC = new ManhattanPriorityComparator();
        MinPQ<SearchNode> mainPQ = new MinPQ<SearchNode>(manhattanPC);
        mainPQ.insert(new SearchNode(initial, null, 0));

        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>(manhattanPC);
        final Board twin = initial.twin();
        twinPQ.insert(new SearchNode(twin, null, 0));

        while (!mainPQ.isEmpty() || !twinPQ.isEmpty())
        {
            SearchNode main = findGoalNode(mainPQ);

            if (main != null)
            {
                candidateNode = main;
                break;
            }

            SearchNode twinNode = findGoalNode(twinPQ);

            if (twinNode != null)
            {
                break;
            }
        }
    }

    private SearchNode findGoalNode(MinPQ<SearchNode> pq)
    {
        if (pq.isEmpty())
        {
            return null;
        }

        final SearchNode searchNode = pq.delMin();
        final Board board = searchNode.currentBoard;

        if (board.isGoal())
        {
            return searchNode;
        }

        final Iterator<Board> iter = board.neighbors().iterator();

        while (iter.hasNext())
        {
            final Board next = iter.next();
            final SearchNode previousNode = searchNode.previousNode;

            if (previousNode == null || !next.equals(previousNode.currentBoard))
            {
                pq.insert(new SearchNode(next, searchNode, searchNode.movesToCurrent + 1));
            }
        }

        return null;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable()
    {
        return candidateNode != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()
    {
        if (!isSolvable())
        {
            return -1;
        }

        return candidateNode.movesToCurrent;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()
    {
        if (!isSolvable())
        {
            return null;
        }

        SearchNode head = candidateNode;
        final Stack<Board> boards = new Stack<>();

        while (head != null)
        {
            final Board board = head.currentBoard;
            boards.push(board);
            head = head.previousNode;
        }

        return boards;
    }

    // test client (see below)
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

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else
        {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}