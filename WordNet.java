import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Bag;

import java.util.HashMap;
import java.util.ArrayList;



public class WordNet{
    private final Digraph G;
    private final HashMap<Integer, String> Id2Noun;
    private final HashMap<String, Bag<Integer>> Noun2Id;    // 同一个String可能对应多个id, 比如id=25514,25515的bout
    private final SAP sap;

    public WordNet(String synsets, String hypernyms){
        if(synsets == null || hypernyms == null)
            throw new IllegalArgumentException();

        Id2Noun = new HashMap<Integer, String>();
        Noun2Id = new HashMap<String, Bag<Integer>>();
        
        // 根据synsets创建Id->Noun, Noun->Id的映射关系
        In syn = new In(synsets);
        while(!syn.isEmpty()){
            String[] s = syn.readLine().split(",");
            Integer id = Integer.parseInt(s[0]);
            String[] nouns = s[1].split(" ");

            Id2Noun.put(id, s[1]);
            for(int i = 0; i < nouns.length; ++i){
                if(!Noun2Id.containsKey(nouns[i]))
                    Noun2Id.put(nouns[i], new Bag<Integer>());
                Noun2Id.get(nouns[i]).add(id);
            }
        }

        // 根据hypernyms创建有向图G
        In hyp = new In(hypernyms);
        G = new Digraph(Id2Noun.size());
        while(!hyp.isEmpty()){
            String[] s = hyp.readLine().split(",");
            for(int i = 1; i < s.length; ++i)
                G.addEdge(Integer.parseInt(s[0]), Integer.parseInt(s[i]));
        }
        
        // test8会检测有向图是否为无环的、且只有一个根, 因此需要进行检测
        DirectedCycle dc = new DirectedCycle(G);
        if(dc.hasCycle())
            throw new IllegalArgumentException();
        int root = 0;
        for(int v = 0; v < G.V(); ++v){
            int next = 0;
            for(Integer w : G.adj(v))   // 没有邻节点的就是根
                ++next;
            if(next == 0)
                ++root;
        }
        if(root != 1)
            throw new IllegalArgumentException();
        
        // 根据有向图G创建SAP类(用于寻找最近公共祖先)
        sap = new SAP(G);
    }

    // 返回WordNet中的所有名词
    public Iterable<String> nouns(){
        return Noun2Id.keySet();
    }

    // word是否是WordNet中的名词
    public boolean isNoun(String word){
        if(word == null)
            throw new IllegalArgumentException();

        return Noun2Id.containsKey(word);
    }

    // nounA与nounB的距离
    public int distance(String nounA, String nounB){
        if(nounA == null || nounB == null || !Noun2Id.containsKey(nounA) || !Noun2Id.containsKey(nounB))
            throw new IllegalArgumentException();

        Bag<Integer> v = Noun2Id.get(nounA);
        Bag<Integer> w = Noun2Id.get(nounB);
        return sap.length(v, w);
    }

    // 
    public String sap(String nounA, String nounB){
        if (nounA == null || nounB == null || !Noun2Id.containsKey(nounA) || !Noun2Id.containsKey(nounB))
            throw new IllegalArgumentException();

        Bag<Integer> v = Noun2Id.get(nounA);
        Bag<Integer> w = Noun2Id.get(nounB);

        int ancestor = sap.ancestor(v, w);

        return Id2Noun.get(ancestor);
    }

    public static void main(String[] args){
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println(wn.distance("bile_duct", "life_history"));
    }
}