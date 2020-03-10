import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.LinkedList;
import java.util.Arrays;

public class BurrowsWheeler {

    private static int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        if (BinaryStdIn.isEmpty())
            return;

        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int len = s.length();

        // 输出 first
        for (int i = 0; i < len; ++i)
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }

        // 输出 t[]
        for (int i = 0; i < len; ++i)
            BinaryStdOut.write(s.charAt((csa.index(i) - 1 + len) % len));

        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        // 第一步：读取first、t[]
        if (BinaryStdIn.isEmpty())
            return;

        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();

        // int first = 3;
        // String s = "ARD!RCAAAABB";

        int len = s.length();
        char[] t = new char[len];
        for (int i = 0; i < len; ++i)
            t[i] = s.charAt(i);

        // 第二步：根据first、t[]计算next[]
        LinkedList<Integer>[] counts = new LinkedList[R]; // 保存每个字母出现的所有位置
        for (int i = 0; i < len; ++i) {
            if (counts[t[i]] == null)
                counts[t[i]] = new LinkedList<Integer>();
            counts[t[i]].addLast(i);
        }
        int[] next = new int[len]; // 根据出现的位置重建next
        for (int i = 0, j = 0; i < R; ++i)
            while (counts[i] != null && !counts[i].isEmpty()) {
                next[j++] = counts[i].removeFirst();
            }

        // 第三步, 根据first, t[], next[] 重建s
        Arrays.sort(t);
        for (int cnt = 0; cnt < len; ++cnt, first = next[first])
            BinaryStdOut.write(t[first]);

        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-"))
            transform();
        if (args[0].equals("+"))
            inverseTransform();

    }

}