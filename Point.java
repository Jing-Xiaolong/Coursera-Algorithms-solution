import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x; // x-coordinate of this point
    private final int y; // y-coordinate of this point

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        int dy = this.y - that.y;
        int dx = this.x - that.x;
        if(dy == 0 && dx == 0)
            return Double.NEGATIVE_INFINITY;
        else if(dx == 0)
            return Double.POSITIVE_INFINITY;
        else if(dy == 0)
            return 0;
        else
            return dy / (double)dx;
    }

    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if(this.x == that.x && this.y == that.y){
            return 0;
        }

        if(this.y < that.y ||(this.y == that.y && this.x < that.x))
            return -1;
        else
            return 1;
    }

    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new slopeComparator();
    }

    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    private class slopeComparator implements Comparator<Point>{
        @Override
        public int compare(Point p1, Point p2){
            double sl1 = slopeTo(p1);
            double sl2 = slopeTo(p2);
            if(sl1 < sl2)
                return -1;
            else if(sl1 > sl2)
                return 1;
            else
                return 0;
        }
    }

    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point p = new Point(1,1), q = new Point(3,10);
        LineSegment ls = new LineSegment(p, q);
        ls.draw();
    }
}