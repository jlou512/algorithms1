import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private int trials;
  private double[] thresholds;
  private double mean;
  private double stddev;

  // perform independent trials on an n-by-n grid.
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException("invalid parameters");
    }

    this.trials = trials;
    thresholds = new double[trials];
    final double totalSites = n * n;

    for (int i = 0; i < trials; i++) {
      boolean percolates = false;
      final Percolation percolation = new Percolation(n);
      while (!percolates) {
        int row = StdRandom.uniform(1, n + 1);
        int col = StdRandom.uniform(1, n + 1);

        if (!percolation.isOpen(row, col)) {
          percolation.open(row, col);
          percolates = percolation.percolates();
        }
      }

      final int openedSites = percolation.numberOfOpenSites();
      thresholds[i] = openedSites / totalSites;
    }

    mean = StdStats.mean(thresholds);
    stddev = StdStats.stddev(thresholds);
  }

  // sample mean of percolation threshold
  public double mean() {
    return mean;
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    if (trials == 1) {
      return Double.NaN;
    }

    return stddev;
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean() - 1.96 * stddev() / Math.sqrt(trials);
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean() + 1.96 * stddev() / Math.sqrt(trials);
  }

  // test client (see below)
  public static void main(String[] args) {
    final int n = Integer.parseInt(args[0]);
    final int T = Integer.parseInt(args[1]);

    final PercolationStats stats = new PercolationStats(n, T);

    final double mean = stats.mean();
    final double stddev = stats.stddev();
    final double confHi = stats.confidenceHi();
    final double confLo = stats.confidenceLo();

    System.out.println("mean                             = " + mean);
    System.out.println("stddev                           = " + stddev);
    System.out.println(
        "95% confidence interval          = [" + confLo + ", " + confHi + "]");
  }
}