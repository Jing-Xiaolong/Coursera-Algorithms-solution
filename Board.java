import java.util.ArrayList;

public final class Board{
    private int[][] tiles;
    private final int N;

    public Board(int[][] tiles){
        this.tiles = new int[tiles.length][tiles.length];
        assert tiles.length >= 2 && tiles.length < 128
                && tiles[0].length >= 2 && tiles[0].length < 128;

        N = tiles.length;
        for(int i = 0; i < tiles.length; ++i){
            for(int j = 0; j < tiles[i].length; ++j){
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    public String toString(){
        StringBuffer res = new StringBuffer();
        res.append(N);
        for(int i = 0; i < N; ++i){
            res.append("\n");
            res.append(tiles[i][0]);
            for(int j = 1; j < N; ++j){
                res.append(" " + tiles[i][j]);
            }
        }
        return new String(res);
    }

    public int dimension(){
        return N;
    }
    // number of tile in wrong pos
    public int hamming(){
        int res = 0;
        for(int i = 0; i < N; ++i){
            for(int j = 0; j < N; ++j){
                int target = i * N + j + 1;
                if(target == N * N)
                    break;
                if(target != tiles[i][j])
                    ++res;
            }
        }
        return res;
    }

    public int manhattan(){
        int res = 0;
        for(int i = 0; i < N; ++i){
            for(int j = 0; j < N; ++j){
                int tile = tiles[i][j];
                if(tile == 0)
                    continue;
                int row = (tile - 1) / N;
                int col = (tile - 1) % N;
                res = res + Math.abs(row - i) + Math.abs(col - j);
            }
        }
        return res;
    }

    public boolean isGoal(){
        return hamming() == 0;
    }

    public boolean equals(Object y){
        if(y == null)       // y 为空, 肯定不同
            return false;
        if(y == this)      // y和this指向相同, 必然相同
            return true;
        if(y.getClass() != this.getClass())    // 不相同的类, 不能比较
            return false;
        
        Board that = (Board)y;      // y和this是指向不同位置的Board类, 判断每个元素是否相同
        if(that.dimension() != this.dimension())
            return false;
        for(int i = 0; i < N; ++i){
            for(int j = 0; j < N; ++j){
                if(that.tiles[i][j] != this.tiles[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors(){
        ArrayList<Board> al = new ArrayList<Board>();
        int row = 0, col = 0;
        int[][] copy = new int[N][N];

        for(int i = 0; i < N; ++i){
            for(int j = 0; j < N; ++j){
                copy[i][j] = this.tiles[i][j];
                if(this.tiles[i][j] == 0){
                    row = i;
                    col = j;
                }
            }
        }

        if(row > 0){
            exch(copy, row, col, row - 1, col);
            al.add(new Board(copy));
            exch(copy, row, col, row - 1, col);
        }
        if(col > 0){
            exch(copy, row, col, row, col - 1);
            al.add(new Board(copy));
            exch(copy, row, col, row, col - 1);
        }
        if(row < N - 1){
            exch(copy, row, col, row + 1, col);
            al.add(new Board(copy));
            exch(copy, row, col, row + 1, col);
        }
        if(col < N - 1){
            exch(copy, row, col, row, col + 1);
            al.add(new Board(copy));
            exch(copy, row, col, row, col + 1);
        }

        return al;
    }

    // 交换任意一对就可以了
    public Board twin(){
        Board twin = new Board(this.tiles);
        // 确保不交换到空
        if(twin.tiles[0][0] == 0){  // (0,0)是空, 不交换它
            exch(twin.tiles, 0, 1, 1, 0);       
        }else if(twin.tiles[1][0] == 0){    //(1,0)是空，不交换它
            exch(twin.tiles, 0, 0, 0, 1);
        }else{                              // (0,0)和(1,0)都非空， 交换它们
            exch(twin.tiles, 0, 0, 1, 0);
        }
        return twin;
    }

    private void exch(int[][] data, int i, int j, int p, int q){
        int tmp = data[i][j];
        data[i][j] = data[p][q];
        data[p][q] = tmp;
    }

    public static void main(String[] args){
        int[][] tiles = {{8,1,3},{4,0,2},{7,6,5}};
        Board b = new Board(tiles);
        System.out.println(b.hamming() + " " + b.manhattan());
        System.out.print(b);
    }
}