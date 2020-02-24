import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats{
    private final double[] results;
    private final int trials;

    // 在nxn网格上进行T次实验
    public PercolationStats(int n, int T){
        trials = T;
        if(n <= 0 || T <= 0){
            throw new IllegalArgumentException();
        }

        results = new double[trials];
        for(int i = 0; i < trials; ++i){
            int numOfOpens = 0;
            Percolation perc = new Percolation(n);
            while(!perc.percolates()){
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if(!perc.isOpen(row, col)){// && !perc.isFull(row, col)){
                    perc.open(row, col);
                    ++numOfOpens;
                }
            }
            results[i] = (double)numOfOpens * 1.0 / (n * n * 1.0);
        }
    }

    public double mean(){
        return resMean();
    }

    public double stddev(){
        return resDev();
    }

    public double confidenceLo(){
        return resMean() - 1.96 * stddev() / Math.sqrt(trials);
    }

    public double confidenceHi(){
        return resMean() + 1.96 * stddev() / Math.sqrt(trials);
    }

    private double resMean(){
        return StdStats.mean(results);
    }
    private double resDev(){
        return StdStats.stddev(results);
    }

    public static void main(String[] args){
        return;
    }
}