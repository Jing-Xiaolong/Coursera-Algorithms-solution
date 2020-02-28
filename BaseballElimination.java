import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {
    // create a baseball division from given filename in format specified below
    private String[] teams;
    private int[] wins;
    private int[] losses;
    private int[] remains;
    private int[][] g;
    private ArrayList<String>[] R;
    private HashMap<String, Integer> ID;   // 节点名称 -> 节点编号的映射
    public BaseballElimination(String filename) {
        if(filename == null)
            throw new IllegalArgumentException();

        read_preprocess(new In(filename));

        // 确定第n_th个队伍是否mathematically eliminated
        // 如果w[x] + r[x] < w[i], 则是trivial elimination, 直接判断, 否则通过network判断
        for(int n_th = 0; n_th < teams.length; ++n_th){
            int max_n = wins[n_th] + remains[n_th];
            for(int i = 0; i < teams.length; ++i){
                if(wins[i] <= max_n)
                    continue;
                if(R[n_th] == null)
                    R[n_th] = new ArrayList<String>();
                R[n_th].add(teams[i]);
            }

            if(R[n_th] == null)
                determinElimination(n_th);
        }
    }
    
    private void read_preprocess(In in){

        // 读取数据, 并初始化各个私有变量
        int N = in.readInt();
        ID = new HashMap<String, Integer>();
        teams = new String[N];
        wins = new int[N];
        losses = new int[N];
        remains = new int[N];
        g = new int[N][N];
        R = (ArrayList<String>[]) new ArrayList[N];
        for(int i = 0; i < N; ++i){
            teams[i] = in.readString();
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remains[i] = in.readInt();
            for(int j = 0; j < N; ++j)
                g[i][j] = in.readInt();
        }

        // 创建每个节点名称->节点编号的映射
        int len = teams.length;
        int V = 1 + 1 + len + len * len;
        for (int i = 0; i < len; ++i)
            ID.put(teams[i], i);
        for (int i = 0; i < len; ++i) {
            for (int j = 0; j < len; ++j) {
                ID.put(teams[i] + "-" + teams[j], len + len * i + j);
            }
        }
        ID.put("source", V - 2);
        ID.put("sink", V - 1);
    }

    private void determinElimination(int n_th){
        int len = teams.length;
        int V = 1 + 1 + len + len * len;
        FlowNetwork net = new FlowNetwork(V);

        // 添加FlowEdge: teams[i] -> sink
        for(int i = 0; i < len; ++i){
            if(i != n_th){
                int capacity = wins[n_th] + remains[n_th] - wins[i];
                FlowEdge e = new FlowEdge(ID.get(teams[i]), ID.get("sink"), capacity < 0 ? 0 : capacity);
                net.addEdge(e);
            }
        }

        // 添加FlowEdge: source -> game[i][j]
        // 同时添加: game[i][j] -> team[i]/team[j]
        for(int i = 0; i < len; ++i){
            for(int j = i + 1; j < len; ++j){
                if (i == n_th || j == n_th)
                    continue;

                // 添加 source -> game[i][j]
                FlowEdge e = new FlowEdge(ID.get("source"), ID.get(teams[i] + "-" + teams[j]), g[i][j]);
                net.addEdge(e);

                // 添加 game[i][j] -> team[i]/team[j]
                FlowEdge ei = new FlowEdge(ID.get(teams[i] + "-" + teams[j]), ID.get(teams[i]), Double.POSITIVE_INFINITY);
                FlowEdge ej = new FlowEdge(ID.get(teams[i] + "-" + teams[j]), ID.get(teams[j]), Double.POSITIVE_INFINITY);
                net.addEdge(ei);
                net.addEdge(ej);
            }
        }

        FordFulkerson ff =  new FordFulkerson(net, ID.get("source"), ID.get("sink"));
        for(int i = 0; i < len; ++i){
            if(i == n_th)
                continue;
            if(ff.inCut(ID.get(teams[i]))){
                if(R[n_th] == null){
                    R[n_th] = new ArrayList<String>();
                }
                R[n_th].add(teams[i]);
            }
        }
    }

    // number of teams
    public int numberOfTeams(){
        return teams.length;
    }

    // all teams
    public Iterable<String> teams(){
        ArrayList<String> al = new ArrayList<String>();
        for(int i = 0; i < teams.length; ++i)
            al.add(teams[i]);
        return al;
    }

    // number of wins for given team
    public int wins(String team){
        int index = 0;
        for(; index < teams.length; ++index)
            if(teams[index].equals(team))
                break;
        if(index == teams.length)
            throw new IllegalArgumentException("invalid team");
        return wins[index];
    }

    // number of losses for given team
    public int losses(String team){
        int index = 0;
        for(; index < teams.length; ++index)
            if(teams[index].equals(team))
                break;
        if(index == teams.length)
            throw new IllegalArgumentException("invalid team");
        return losses[index];
    }

    // number of remaining games for given team
    public int remaining(String team){
        int index = 0;
        for(; index < teams.length; ++index)
            if(teams[index].equals(team))
                break;
        if(index == teams.length)
            throw new IllegalArgumentException("invalid team");
        return remains[index];

    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2){
        int i = 0, j = 0;
        for(; i < teams.length; ++i)
            if(teams[i].equals(team1))
                break;
        for(; j < teams.length; ++j)
            if(teams[j].equals(team2))
                break;
        if(i == teams.length || j == teams.length)
            throw new IllegalArgumentException("invalid team");
        return g[i][j];
    }

    // is given team eliminated?
    public boolean isEliminated(String team){
        int index = 0;
        for (; index < teams.length; ++index)
            if (teams[index].equals(team))
                break;
        if (index == teams.length)
            throw new IllegalArgumentException("invalid team");

        return R[index] != null;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team){
        int index = 0;
        for (; index < teams.length; ++index)
            if (teams[index].equals(team))
                break;
        if (index == teams.length)
            throw new IllegalArgumentException("invalid team");
        return R[index];
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("teams4.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }

    }
}