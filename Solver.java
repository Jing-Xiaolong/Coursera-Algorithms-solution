import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;

import edu.princeton.cs.algs4.MinPQ;

public final class Solver{

    private class Node {
        Node prev;
        Board board;
        Node(Node prev, Board b) {
            this.prev = prev;
            this.board = b;
        }
    }

    private class nodeComp implements Comparator<Node> {
        @Override
        public int compare(Node na, Node nb) {
            int priority_a = numMoves(na) + na.board.manhattan();
            int priority_b = numMoves(nb) + nb.board.manhattan();
            if (priority_a < priority_b)
                return -1;
            else if (priority_a > priority_b)
                return 1;
            else
                return 0;
        }
    }


    private ArrayList<Board> sol;
    private MinPQ<Node> pq;
    private MinPQ<Node> pqTwin;
    private Node soluNode = null;
    private boolean isSolvable;

    public Solver(Board initial){
        if(initial == null)
            throw new IllegalArgumentException();
        
        isSolvable = true;

        pq = new MinPQ<Node>(new nodeComp());
        pq.insert(new Node(null, initial));

        pqTwin = new MinPQ<Node>(new nodeComp());
        pqTwin.insert(new Node(null, initial.twin()));

        while(true){
            if(pq.size() > 100000){
                isSolvable = false;
                soluNode = null;
                break;
            }
            if(pq.min().board.isGoal()){
                isSolvable = true;
                soluNode = pq.min();
                break;
            }
            if(pqTwin.min().board.isGoal()){
                isSolvable = false;
                soluNode = null;
                break;
            }

            Node preNode = pq.min();
            for(Board neighbor : preNode.board.neighbors()){
                boolean isDup = false;
                Node pre = preNode;
                while(pre != null){
                    if(pre.board.equals(neighbor)){
                        isDup = true;
                        break;
                    }
                    pre = pre.prev;
                }
                // 与前面的步骤不重复才添加
                if(!isDup){
                    Node curNode = new Node(null, neighbor);
                    curNode.prev = preNode;
                    pq.insert(curNode);
                }
            }

            Node preNodeTwin = pqTwin.min();
            for(Board neighbor : preNodeTwin.board.neighbors()){
                boolean isDup = false;
                Node pre = preNodeTwin;
                while(pre != null){
                    if(pre.board.equals(neighbor)){
                        isDup = true;
                        break;
                    }
                    pre = pre.prev;
                }
                if(!isDup){
                    Node curNode = new Node(null, neighbor);
                    curNode.prev = preNode;
                    pqTwin.insert(curNode);
                }
            }

            pq.delMin();
            pqTwin.delMin();
        }
    }

    public boolean isSolvable(){
        return isSolvable;
    }

    public int moves(){
        if(isSolvable)
            return numMoves(soluNode) - 1;
        else
            return -1;
    }

    public Iterable<Board> solution(){
        sol = new ArrayList<Board>();
        
        if (!isSolvable)
            return null;

        Node cur = soluNode;
        while(cur != null){
            sol.add(cur.board);
            cur = cur.prev;
        }
        Collections.reverse(sol);
        return sol;
    }

    private int numMoves(Node node){
        int num = 0;
        Node n = node;
        while(n != null){
            ++num;
            n = n.prev;
        }
        return num;
    }

    public static void main(String[] args){
        Board b = new Board(new int[][]{{0,3,1},{4,2,5},{7,8,6}});
        Solver sol = new Solver(b);
        System.out.println("原棋盘:" + b + "\n");

        System.out.println(sol.moves() + "步求解:");
        for (Board board : sol.solution())
            System.out.println(board);
    }
}