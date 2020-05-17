
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private final boolean[] sites;
  private final int numberOfSites;
  private final int rowSize;
  private final WeightedQuickUnionUF unionFind;
  private final WeightedQuickUnionUF noBottomUF;

  private int openSites;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException(
          "Number of sites is less than or equal to zero.");
    }

    this.rowSize = n;
    numberOfSites = n * n;

    final int actualSize = numberOfSites + 2;
    final int actualSize2 = numberOfSites + 1;

    sites = new boolean[actualSize];

    for (int i = 0; i < actualSize; i++) {
      sites[i] = false;
    }

    unionFind = new WeightedQuickUnionUF(actualSize);
    noBottomUF = new WeightedQuickUnionUF(actualSize2);
    openSites = 0;

    // connect virtual sites
    for (int i = 1; i <= n; i++) {
      unionFind.union(0, i); // top site
      noBottomUF.union(0, i);
      unionFind.union(numberOfSites - 1, i + n * (n - 1)); // bottom site
    }
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    validateIndicies(row, col);

    if (!isOpen(row, col)) {
      final int curr = convertTo1DIndex(row, col);

      sites[curr] = true;
      openSites++;

      connectOpenNeighbor(curr, row + 1, col);
      connectOpenNeighbor(curr, row - 1, col);
      connectOpenNeighbor(curr, row, col + 1);
      connectOpenNeighbor(curr, row, col - 1);
    }
  }

  private void connectOpenNeighbor(int curr, int row, int col) {
    if (inBounds(row, col) && isOpen(row, col)) {
      final int neighbor = convertTo1DIndex(row, col);
      unionFind.union(curr, neighbor);
      noBottomUF.union(curr, neighbor);
    }
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    validateIndicies(row, col);
    return sites[convertTo1DIndex(row, col)];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    validateIndicies(row, col);

    if (isOpen(row, col)) {
      final int index = convertTo1DIndex(row, col);
      return noBottomUF.find(0) == noBottomUF.find(index);
    }

    return false;
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return openSites;
  }

  // does the system percolate?
  public boolean percolates() {
    
    if (rowSize == 1)
    {
      return sites[1];
    }
    
    final int top = unionFind.find(0);
    final int bottom = unionFind.find(numberOfSites - 1);
    return top == bottom;
  }

  private boolean inBounds(int row, int col) {
    return ((row >= 1) && (row <= rowSize) && (col >= 1) && (col <= rowSize));
  }

  private void validateIndicies(int row, int col) {
    if (!inBounds(row, col)) {
      throw new IllegalArgumentException();
    }
  }

  private int convertTo1DIndex(int row, int col) {
    return (row - 1) * rowSize + col;
  }

  // test client (optional)
  public static void main(String[] args) {
  }
}
