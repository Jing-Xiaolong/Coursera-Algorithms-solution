import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;

public class BoggleSolver {
    private final Tries tries; // Tries为内部自定义类,

    public BoggleSolver(String[] dictionary) {
        tries = new Tries();
        for (int i = 0; i < dictionary.length; ++i)
            tries.put(dictionary[i]);
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> al = new HashSet<String>();
        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.cols(); ++j) {
                boolean[][] marked = new boolean[board.rows()][board.cols()];
                dfs(board, marked, al, i, j, "");
            }
        }
        return al;
    }

    public int scoreOf(String word) {
        if (!tries.containsKey(word))
            return 0;

        switch (word.length()) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    private void dfs(BoggleBoard bb, boolean[][] marked, HashSet<String> res, int row, int col, String prefix) {
        // 已到访过, 返回
        if (marked[row][col])
            return;

        // 未到访过, 前缀中添加当前字母
        prefix += (bb.getLetter(row, col) == 'Q') ? "QU" : bb.getLetter(row, col);

        // tries树不存在该前缀, 直接返回不继续搜索
        if (!tries.containsPrefix(prefix))
            return;

        // 该前缀前缀刚好是一个tries树中的一个元素
        if (prefix.length() > 2 && tries.containsKey(prefix) && !res.contains(prefix))
            res.add(prefix);

        // 标记该处访问的点
        marked[row][col] = true;

        // 朝8个方向进行dfs搜索
        if (row > 0)
            dfs(bb, marked, res, row - 1, col, prefix);
        if (col > 0)
            dfs(bb, marked, res, row, col - 1, prefix);
        if (row < bb.rows() - 1)
            dfs(bb, marked, res, row + 1, col, prefix);
        if (col < bb.cols() - 1)
            dfs(bb, marked, res, row, col + 1, prefix);
        if (row > 0 && col > 0)
            dfs(bb, marked, res, row - 1, col - 1, prefix);
        if (row > 0 && col < bb.cols() - 1)
            dfs(bb, marked, res, row - 1, col + 1, prefix);
        if (row < bb.rows() - 1 && col > 0)
            dfs(bb, marked, res, row + 1, col - 1, prefix);
        if (row < bb.rows() - 1 && col < bb.cols() - 1)
            dfs(bb, marked, res, row + 1, col + 1, prefix);

        // 访问完成, 擦除标记
        marked[row][col] = false;
    }

    /**
     * 前缀树 只需实现put, containsKey, containsPrefix 三个公有成员即可
     */
    private class Tries {
        private static final int R = 26; // 只包含26大写字母
        private Node root = new Node();

        private class Node {
            boolean isExist = false;
            Node[] next = new Node[R];
        }

        public Tries() {
        }

        public void put(String key) {
            root = put(root, key, 0);
        }

        public boolean containsKey(String key) {
            return get(key) != false;
        }

        public boolean containsPrefix(String prefix) {
            return get(root, prefix, 0) != null;
        }

        private boolean get(String key) {
            Node x = get(root, key, 0);
            if (x == null)
                return false;
            return x.isExist;
        }

        private Node get(Node x, String key, int idx) {
            if (x == null)
                return null;
            if (idx == key.length())
                return x;
            int i = key.charAt(idx) - 'A';
            return get(x.next[i], key, idx + 1);
        }

        private Node put(Node x, String key, int idx) {
            if (x == null)
                x = new Node();
            if (idx == key.length()) {
                x.isExist = true;
                return x;
            }
            int i = key.charAt(idx) - 'A';
            x.next[i] = put(x.next[i], key, idx + 1);
            return x;
        }
    }

    public static void main(String[] args) {
        In in = new In("dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board-q.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);

    }
}
