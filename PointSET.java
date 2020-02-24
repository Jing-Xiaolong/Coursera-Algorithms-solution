import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.TreeSet;
import java.util.ArrayList;


public class PointSET{
    private TreeSet<Point2D> points;

    public PointSET(){
        points = new TreeSet<Point2D>();
    }

    public boolean isEmpty(){
        return points.isEmpty();
    }

    public int size(){
        return points.size();
    }

    public void insert(Point2D p){
        if(p == null)
            throw new IllegalArgumentException();
        points.add(p);
    }

    public boolean contains(Point2D p){
        if(p == null)
            throw new IllegalArgumentException();
        return points.contains(p);
    }

    public void draw(){
        for(Point2D p : points)
            p.draw();
    }

    public Iterable<Point2D> range(RectHV rect){
        if(rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> al = new ArrayList<Point2D>();

        for(Point2D p : points){
            if(rect.contains(p))
                al.add(p);
        }

        return al;
    }

    public Point2D nearest(Point2D p){
        if(p == null)
            throw new IllegalArgumentException();
        Point2D res = null;
        double d = Double.MAX_VALUE;
        for(Point2D pt : points){
            double dtmp = pt.distanceSquaredTo(p);
            if(dtmp < d){
                res = pt;
                d = dtmp;
            }
        }
        return res;
    }


    public static void main(String[] args){
        // PointSET ps = new PointSET();
        // ps.insert(new Point2D(0.4, 0.75));
        // ps.insert(new Point2D(0,0));
        // ps.insert(new Point2D(0.3, 0.6));

    }
}