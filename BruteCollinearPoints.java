import java.util.Arrays;
import java.util.ArrayList;

public class BruteCollinearPoints{
    private final Point[] pset;
    private LineSegment[] lSeg;
    public BruteCollinearPoints(Point[] points){
        if(points == null)
            throw new IllegalArgumentException();
        pset = points;
        check();
        solve();
    }

    public int numberOfSegments(){
        return lSeg.length;
    }

    public LineSegment[] segments(){
        return lSeg;
    }

    private void solve(){
        ArrayList<LineSegment> res = new ArrayList<LineSegment>(); // 保存已找到的

        Point[] copy = Arrays.copyOf(pset, pset.length); // 用来进行排序的副本
        Arrays.sort(copy);
        for(int p = 0; p < copy.length - 3; ++p)
            for(int q = p + 1; q < copy.length - 2; ++q)
                for(int r = q + 1; r < copy.length - 1; ++r)
                    for(int s = r + 1; s < copy.length; ++s)
                        if(copy[p].slopeTo(copy[q]) == copy[p].slopeTo(copy[r]) && copy[p].slopeTo(copy[q]) == copy[p].slopeTo(copy[s]))
                            res.add(new LineSegment(copy[p], copy[s]));

        lSeg = res.toArray(new LineSegment[res.size()]);
    }

    private void check(){
        for (Point p : pset) {
            if (p == null)
                throw new IllegalArgumentException();
        }

        Point[] copy = Arrays.copyOf(pset, pset.length); // 用来进行排序的副本
        Arrays.sort(copy);
        for (int i = 0; i < copy.length; ++i) {
            for (int j = i + 1; j < copy.length; ++j) {
                if (copy[i].compareTo(copy[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

}