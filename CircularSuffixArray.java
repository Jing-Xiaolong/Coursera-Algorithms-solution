import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {

    private final String s;
    private final Integer[] index;
    private final int len;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();

        this.s = s;
        this.len = s.length();
        this.index = new Integer[len];
        for (int i = 0; i < len; ++i)
            index[i] = i;

        Arrays.sort(index, new comp());
    }

    // length of s
    public int length() {
        return this.len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= this.len)
            throw new IllegalArgumentException();

        return this.index[i];
    }

    private class comp implements Comparator<Integer> {
        public int compare(Integer int1, Integer int2) {
            if (int1 == int2)
                return 0;

            // String src = s + s;
            // String item1 = src.substring(int1, int1 + len);
            // String item2 = src.substring(int2, int2 + len);
            // return item1.compareTo(item2);

            // 一位一位进行比较
            for (int i = 0; i < len; ++i) {
                char c1 = s.charAt((int1 + i) % len);
                char c2 = s.charAt((int2 + i) % len);
                if (c1 < c2)
                    return -1;
                if (c1 > c2)
                    return 1;
                // c1 == c2, ++i后继续判断
            }
            return 0;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {

    }
}