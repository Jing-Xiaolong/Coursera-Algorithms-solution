import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;

public class KdTree{
    private Node root;
    private int sz;

    private static boolean HORI = true;
    private static boolean VERT = false;

    private class Node{
        public Node left;
        public Node right;
        public Point2D pt;
        public boolean dir = false;

        Node(Point2D pt, boolean dir){
            this.left = null;
            this.right = null;
            this.pt = pt;
            this.dir = dir;
        }
    }

    public KdTree(){
        this.root = null;
        sz = 0;
    }

    public boolean isEmpty(){
        return sz == 0;
    }

    public int size(){
        return sz;
    }


    public void insert(Point2D p){
        if(p == null)
            throw new IllegalArgumentException();

        if(root == null){
            root = new Node(p, VERT);
            ++sz;
            return;
        }

        if(contains(p))
            return;

        Node node = root;
        while(true){
            // 新点插入node右节点
            if((node.dir == VERT && node.pt.x() <= p.x()) || (node.dir == HORI && node.pt.y() <= p.y())){
                if(node.right == null){
                    node.right = new Node(p, !node.dir);
                    ++sz;
                    break;
                }else
                    node = node.right;
            }
            else if((node.dir == VERT && node.pt.x() > p.x()) || (node.dir == HORI && node.pt.y() > p.y())){
                if(node.left == null){
                    node.left = new Node(p, !node.dir);
                    ++sz;
                    break;
                }else
                    node = node.left;
            }
        }
    }


    public boolean contains(Point2D p){
        if(p == null)
            throw new IllegalArgumentException();

        Node node = root;
        while(node != null){
            if (node.pt.equals(p))
                return true;
            else if ((node.dir == VERT && node.pt.x() <= p.x()) || (node.dir == HORI && node.pt.y() <= p.y()))
                node = node.right;
            else
                node = node.left;
        }

        return false;
    }

    public void draw(){
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        new RectHV(0, 0, 1, 1).draw();  // 大边框

        draw(root, 0., 1., 0., 1.);     // 从根部开始绘制
    }
    private void draw(Node node, double xmin, double xmax, double ymin, double ymax){
        if(node == null)
            return;
        
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        node.pt.draw();

        StdDraw.setPenRadius(0.002);
        if(node.dir == VERT){
            StdDraw.setPenColor(StdDraw.RED);
            new Point2D(node.pt.x(), ymin).drawTo(new Point2D(node.pt.x(), ymax));
            draw(node.left, xmin, node.pt.x(), ymin, ymax);
            draw(node.right, node.pt.x(), xmax, ymin, ymax);
        }else if(node.dir == HORI){
            StdDraw.setPenColor(StdDraw.BLUE);
            new Point2D(xmin, node.pt.y()) . drawTo(new Point2D(xmax, node.pt.y()));
            draw(node.left, xmin, xmax, ymin, node.pt.y());
            draw(node.right, xmin, xmax, node.pt.y(), ymax);
        }

    }


    public Iterable<Point2D> range(RectHV rect){
        if(rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> al = new ArrayList<Point2D>();
        rangeSearch(root, rect, al);
        return al;
    }
    private void rangeSearch(Node node, RectHV rect, ArrayList<Point2D> al){
        if(node == null)
            return;

        if(rect.contains(node.pt)){
            al.add(node.pt);
            rangeSearch(node.left, rect, al);
            rangeSearch(node.right, rect, al);
            return;
        }

        if((node.dir == VERT && rect.xmin() <= node.pt.x()) || (node.dir == HORI && rect.ymin() <= node.pt.y()))
            rangeSearch(node.left, rect, al);
        
        if((node.dir == VERT && rect.xmax() >= node.pt.x()) || (node.dir == HORI && rect.ymax() >= node.pt.y()))
            rangeSearch(node.right, rect, al);
    }



    private Point2D res = null;
    public Point2D nearest(Point2D p){
        if(p == null)
            throw new IllegalArgumentException();
        nearest(root, p, Math.sqrt(2));
        return res;
    }
    private double nearest(Node cur, Point2D p, Double dis){
        if(cur == null)
            return Math.sqrt(2);
        
        // |cur.pt, p|距离小于已找到的最小dis, 更新最小dis和最近邻点res
        if(cur.pt.distanceTo(p) < dis){
            dis = cur.pt.distanceTo(p);
            res = new Point2D(cur.pt.x(), cur.pt.y());
        }

        // p在cur的左侧
        if(cur.dir == VERT && p.x() <= cur.pt.x()){
            dis = Math.min(nearest(cur.left, p, dis), dis);
            if(dis > Math.abs(p.x() - cur.pt.x()))              // 当前最小dis > p到分割线距离
                dis = Math.min(nearest(cur.right, p, dis), dis);// 继续查询右子是否存在更近的点
        }
        // p在cur的右侧
        else if(cur.dir == VERT && p.x() > cur.pt.x()){
            dis = Math.min(nearest(cur.right, p, dis), dis);
            if(dis > Math.abs(p.x() - cur.pt.x()))
                dis = Math.min(nearest(cur.left, p, dis), dis);
        }
        // p在cur的下方
        else if(cur.dir == HORI && p.y() <= cur.pt.y()){
            dis = Math.min(nearest(cur.left, p, dis), dis);
            if(dis > Math.abs(p.y() - cur.pt.y()))
                dis = Math.min(nearest(cur.right, p, dis), dis);
        }
        // p在cur的上方
        else if(cur.dir == HORI && p.y() > cur.pt.y()){
            dis = Math.min(nearest(cur.right, p, dis), dis);
            if(dis > Math.abs(p.y() - cur.pt.y()))
                dis = Math.min(nearest(cur.left, p, dis), dis);
        }

        return dis;
    }

    public static void main(String[] args){

        KdTree kdtree = new KdTree();
        kdtree.insert(new Point2D(0.8, 0.8));
        kdtree.insert(new Point2D(0.5, 0.5));
        kdtree.insert(new Point2D(0.9, 0.9));
        kdtree.insert(new Point2D(0.5, 0.4));
        kdtree.insert(new Point2D(0.2, 0.2));
        kdtree.insert(new Point2D(0.2, 0.2));
        System.out.println(kdtree.size());

        PointSET brute = new PointSET();
        brute.insert(new Point2D(0.8, 0.8));
        brute.insert(new Point2D(0.5, 0.5));
        brute.insert(new Point2D(0.9, 0.9));
        brute.insert(new Point2D(0.5, 0.4));
        brute.insert(new Point2D(0.2, 0.2));



        Point2D p = new Point2D(1.0, 0);
        Point2D res = kdtree.nearest(p);
        Point2D res1 = brute.nearest(p);

        
        StdDraw.enableDoubleBuffering();
        while (true) {

            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();

            // draw in red the nearest neighbor (using brute-force algorithm)
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            brute.nearest(query).draw();
            StdDraw.setPenRadius(0.02);

            // draw in blue the nearest neighbor (using kd-tree algorithm)
            StdDraw.setPenColor(StdDraw.BLUE);
            kdtree.nearest(query).draw();
            StdDraw.show();
            StdDraw.pause(40);
        }

        // for(Point2D p : kdtree.range(new RectHV(0.55,0.2,1.1,1))){  System.out.println(p); }

        // RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        // StdDraw.enableDoubleBuffering();
        // while (true) {
        //     if (StdDraw.isMousePressed()) {
        //         double x = StdDraw.mouseX();
        //         double y = StdDraw.mouseY();
        //         StdOut.printf("%8.6f %8.6f\n", x, y);
        //         Point2D p = new Point2D(x, y);
        //         if (rect.contains(p)) {
        //             StdOut.printf("%8.6f %8.6f\n", x, y);
        //             kdtree.insert(p);
        //             StdDraw.clear();
        //             kdtree.draw();
        //             StdDraw.show();
        //         }
        //     }
        //     StdDraw.pause(20);
        // }

    }
}