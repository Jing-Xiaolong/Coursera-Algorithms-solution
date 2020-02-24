import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation{
    private final int size;           // 网格尺寸
    private boolean[][] grids;  // 网格是否打开
    private int opened;
    private final int TOP;
    private final int BOTTOM;
    private final WeightedQuickUnionUF uf;

    // 创建nxn网格,所有格初始化为”闭合“
    public Percolation(int n){
        if(n <= 0){
            throw new IllegalArgumentException();
        }
        size = n;
        grids = new boolean[n][n];
        opened = 0;
        uf = new WeightedQuickUnionUF(n * n + 2);
        TOP = 0;
        BOTTOM = n * n + 1;
    }

    // 打开(row, col)处的通道
    public void open(int row, int col){
        if(!isValidIndex(row, col)){    
            throw new IllegalArgumentException();
        }
        if(!isOpen(row, col))
            ++opened;
        grids[row - 1][col - 1] = true;
        if(row == 1)
            uf.union(TOP, ufIndex(row, col));
        if(row == size)
            uf.union(BOTTOM, ufIndex(row, col));

        if(row > 1 && isOpen(row - 1, col))
            uf.union(ufIndex(row, col), ufIndex(row - 1, col));
        if(row < size && isOpen(row + 1, col))
            uf.union(ufIndex(row, col), ufIndex(row + 1, col));
        if(col > 1 && isOpen(row, col - 1))
            uf.union(ufIndex(row, col), ufIndex(row, col - 1));
        if(col < size && isOpen(row, col + 1) )
            uf.union(ufIndex(row, col), ufIndex(row, col + 1));
    }

    // 确认(row, col)处是否为"张开"状态
    public boolean isOpen(int row, int col){
        if(isValidIndex(row, col))
            return grids[row - 1][col - 1];
        throw new IllegalArgumentException();
    }

    // 确认(row, col)处是否与顶部联通
    public boolean isFull(int row, int col){
        if(isValidIndex(row, col))
            return uf.connected(ufIndex(row, col), TOP);
        throw new IllegalArgumentException();
    }

    // 开放的连通区域的个数
    public int numberOfOpenSites(){
        return opened;
    }

    // 是否可以渗透(顶部到底部)
    public boolean percolates(){
        return uf.connected(TOP, BOTTOM);
    }

    // 确保下标正确
    private boolean isValidIndex(int row, int col){
        if(row <= 0 || row > size || col <= 0 || col > size)
            return false;
        else
            return true;
    }
    private int ufIndex(int row, int col){
        return (row - 1) * size + col;
    }

    public static void main(String[] args){
        return;
    }
}