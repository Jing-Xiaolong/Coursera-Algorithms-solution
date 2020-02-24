import java.util.Arrays;
import java.util.ArrayList;

public class FastCollinearPoints {
    private final Point[] pset;
    private LineSegment[] lSeg;

    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();
        pset = points;
        check();
        solve();
    }

    public int numberOfSegments() {
        return lSeg.length;
    }

    public LineSegment[] segments() {
        return lSeg;
    }

    private void solve() {
        ArrayList<LineSegment> res = new ArrayList<LineSegment>();   // 保存已找到的
        ArrayList<String> exists = new ArrayList<String>();              // 保存已存在的直线

        // 寻找直线含四点及以上 
        Point[] copy = Arrays.copyOf(pset, pset.length);
        for(int i = 0; i < pset.length; ++i){
            Arrays.sort(copy, pset[i].slopeOrder());    // 重合的点必然在首位
            Point p = copy[0];
            for(int idx = 1; idx < copy.length - 2; ++idx){ // 从次首位开始找
                Point q = copy[idx];
                Point r = copy[idx + 1];
                Point s = copy[idx + 2];
                if(p.slopeTo(q) == p.slopeTo(r) && p.slopeTo(q) == p.slopeTo(s)){
                    int right = idx + 3;
                    while(right < copy.length && p.slopeTo(q) == p.slopeTo(copy[right])){
                        ++right;
                    }
                    
                    // copy[0] 和 copy[idx...right) 构成一条直线
                    Point[] line = new Point[right - idx + 1];
                    for(int k = idx; k < right; ++k){
                        line[k - idx] = copy[k];
                    }
                    line[line.length - 1] = copy[0];
                    Arrays.sort(line);
                    String l = "Find line : " + line[0].toString() + "->" + line[line.length - 1].toString();
                    if(!exists.contains(l)){    // 这条直线不存在时才放进来
                        res.add(new LineSegment(line[0], line[line.length - 1]));
                        exists.add(l);
                        // System.out.println(l);
                    }
                    idx = right - 1;
                }
            }
        }

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