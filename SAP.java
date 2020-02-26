import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP{
    private final Digraph G;
    public SAP(Digraph G){
        this.G = new Digraph(G);
    }

    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 || v >= this.G.V() || w >= this.G.V())
            throw new IllegalArgumentException();

        // 以v和w分别为source进行bfs搜索
        BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(G, w);

        int ancestor = -1;
        int minDist = Integer.MAX_VALUE;

        for (int n = 0; n < this.G.V(); ++n)
            if (bfs_v.hasPathTo(n) && bfs_w.hasPathTo(n) && (bfs_v.distTo(n) + bfs_w.distTo(n)) < minDist) {
                ancestor = n;
                minDist = bfs_v.distTo(n) + bfs_w.distTo(n);
            }

        return ancestor;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if(v == null || w == null)
            throw new IllegalArgumentException();
        for (Object i : v)
            if (i == null || (Integer)i < 0 || (Integer)i >= this.G.V())
                throw new IllegalArgumentException();
        for (Object i : w)
            if (i == null || (Integer)i < 0 || (Integer)i >= this.G.V())
                throw new IllegalArgumentException();

        // 后面与 int ancestor(int v, int w) 完全一致，因为BreathFirstDirectedPaths能以一系列点为源进行bfs
        BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(G, w);

        int ancestor = -1;
        int minDist = Integer.MAX_VALUE;

        for (int n = 0; n < this.G.V(); ++n)
            if (bfs_v.hasPathTo(n) && bfs_w.hasPathTo(n) && (bfs_v.distTo(n) + bfs_w.distTo(n) < minDist)) {
                ancestor = n;
                minDist = bfs_v.distTo(n) + bfs_w.distTo(n);
            }

        return ancestor;
    }

    public int length(int v, int w){
        if(v < 0 || w < 0 || v >= this.G.V() || w >= this.G.V())
            throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(G, w);

        // 寻找ancestor, 并根据ancestor是否存在2而返回最短距离
        int ancestor = ancestor(v, w);

        return (ancestor == -1) ? -1 : (bfs_v.distTo(ancestor) + bfs_w.distTo(ancestor));
    }
    
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        if(v == null || w == null)
            throw new IllegalArgumentException();
        for(Object i : v)
            if(i == null || (Integer)i < 0 || (Integer)i >= this.G.V())
                throw new IllegalArgumentException();
        for(Object i : w)
            if(i == null || (Integer)i < 0 || (Integer)i >= this.G.V())
                throw new IllegalArgumentException();

        // 后面与 int length(int v, int w)完全一致, 因为BreadthFirstDirectedPaths能以一系列点为源进行bfs
        BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(G, w);

        int ancestor = ancestor(v, w);

        return (ancestor == -1) ? -1 : bfs_v.distTo(ancestor) + bfs_w.distTo(ancestor);
    }


    public static void main(String[] args){
        In in = new In("digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}