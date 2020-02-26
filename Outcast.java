


public class Outcast{
    private final WordNet wordnet;
    public Outcast(WordNet wordnet){
        if(wordnet == null)
            throw new IllegalArgumentException();
        
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns){
        int maxDis = Integer.MIN_VALUE;
        String res = nouns[0];
        for(int i = 0; i < nouns.length; ++i){
            int tmpDis = 0;
            for(int j = 0; j < nouns.length; ++j)
                tmpDis += wordnet.distance(nouns[i], nouns[j]);
            
            res = (tmpDis > maxDis) ? nouns[i] : res;
            maxDis = (tmpDis > maxDis) ? tmpDis : maxDis;
            // System.out.println(nouns[i] + ": " + tmpDis);
        }

        return res;
    }

    public static void main(String[] args){
        // Outcast oc = new Outcast(new WordNet("synsets.txt", "hypernyms.txt"));
        // String[] nouns = {"Turing", "von_Neumann", "Mickey_Mouse"};
        // System.out.println(oc.outcast(nouns));
    }
}