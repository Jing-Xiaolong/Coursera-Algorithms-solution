import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static int R = 256; // 256 ASCII

    // apply move-to-front encoding, reading from standard input and writing to
    // standard output
    public static void encode() {
        char[] seq = generateSequence();
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            char prev = seq[0];
            int index = 0;
            for (; seq[index] != ch; ++index) {
                char tmp = seq[index];
                seq[index] = prev;
                prev = tmp;
            }
            seq[index] = prev;
            seq[0] = ch;
            BinaryStdOut.write(index, 8);
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to
    // standard output
    public static void decode() {
        char[] seq = generateSequence();
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char ch = seq[index];
            for (int i = index; i > 0; --i)
                seq[i] = seq[i - 1];
            seq[0] = ch;
            BinaryStdOut.write(ch, 8);
        }
        BinaryStdOut.flush();
    }

    private static char[] generateSequence() {
        char[] seq = new char[R];
        for (int i = 0; i < R; ++i)
            seq[i] = (char) i;
        return seq;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding

    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        if (args[0].equals("+"))
            decode();
    }

}