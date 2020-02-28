
// import java.util.*;

import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture pic;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("the constructor is called with a null arg");
        this.pic = picture;
    }

    // current picture
    public Picture picture() {
        return pic;
    }

    // width of current picture
    public int width() {
        return pic.width();
    }

    // height of current picture
    public int height() {
        return pic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height())
            throw new IllegalArgumentException("x or y out of its prescribed range");

        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1)
            return 1000.;

        double Rx = pic.get(x - 1, y).getRed() - pic.get(x + 1, y).getRed();
        double Gx = pic.get(x - 1, y).getGreen() - pic.get(x + 1, y).getGreen();
        double Bx = pic.get(x - 1, y).getBlue() - pic.get(x + 1, y).getBlue();
        double Ry = pic.get(x, y - 1).getRed() - pic.get(x, y + 1).getRed();
        double Gy = pic.get(x, y - 1).getGreen() - pic.get(x, y + 1).getGreen();
        double By = pic.get(x, y - 1).getBlue() - pic.get(x, y + 1).getBlue();
        return Math.sqrt(Rx * Rx + Gx * Gx + Bx * Bx + Ry * Ry + Gy * Gy + By * By);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (height() == 1) {
            int[] seam = new int[width()];
            for (int i = 0; i < width(); ++i)
                seam[i] = 0;
            return seam;
        }

        double[][] energy = new double[width()][height()];
        for (int w = 0; w < width(); ++w)
            for (int h = 0; h < height(); ++h)
                energy[w][h] = energy(w, h);

        // 竖直方向, 每一列寻找其最小累加能量
        int[][] edgeTo = new int[width()][height()];
        for (int w = 1; w < width(); ++w) {
            for (int h = 0; h < height(); ++h) {
                if (h == 0) {
                    energy[w][h] += Math.min(energy[w - 1][h], energy[w - 1][h + 1]);
                    edgeTo[w][h] = energy[w - 1][h] < energy[w - 1][h + 1] ? h : h + 1;
                } else if (h == height() - 1) {
                    energy[w][h] += Math.min(energy[w - 1][h], energy[w - 1][h - 1]);
                    edgeTo[w][h] = energy[w - 1][h] < energy[w - 1][h - 1] ? h : h - 1;
                } else {
                    double minE = Double.MAX_VALUE;
                    int to = -1;
                    for (int hh = h - 1; hh <= h + 1; ++hh) {
                        if (energy[w - 1][hh] <= minE) {
                            minE = energy[w - 1][hh];
                            to = hh;
                        }
                    }
                    energy[w][h] += minE;
                    edgeTo[w][h] = to;
                }
            }
        }

        // 找到最右一列中的最小能量值(出口)
        int end = -1;
        double minE = Double.MAX_VALUE;
        int w = width() - 1;
        for (int h = 0; h < height(); ++h) {
            if (energy[w][h] <= minE) {
                end = h;
                minE = energy[w][h];
            }
        }

        // 从最小能量处往回找到路径 seam
        int[] seam = new int[width()];
        for (int ww = width() - 1; ww >= 0; --ww) {
            seam[ww] = end;
            end = edgeTo[ww][end];
        }

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (width() == 1) {
            int[] seam = new int[height()];
            for (int i = 0; i < height(); ++i)
                seam[i] = 0;
            return seam;
        }

        double[][] energy = new double[width()][height()];
        for (int w = 0; w < width(); ++w)
            for (int h = 0; h < height(); ++h)
                energy[w][h] = energy(w, h);

        // 水平方向, 每一行找到最小累计能量
        int[][] edgeTo = new int[width()][height()];
        for (int h = 1; h < height(); ++h) {
            for (int w = 0; w < width(); ++w) {
                if (w == 0) {
                    energy[w][h] += Math.min(energy[w][h - 1], energy[w + 1][h - 1]);
                    edgeTo[w][h] = (energy[w][h - 1] < energy[w + 1][h - 1]) ? w : w + 1;
                } else if (w == width() - 1) {
                    energy[w][h] += Math.min(energy[w][h - 1], energy[w - 1][h - 1]);
                    edgeTo[w][h] = (energy[w][h - 1] < energy[w - 1][h - 1]) ? w : w - 1;
                } else {
                    double minE = Double.MAX_VALUE;
                    int to = -1;
                    for (int ww = w - 1; ww <= w + 1; ++ww) {
                        if (energy[ww][h - 1] < minE) {
                            to = ww;
                            minE = energy[ww][h - 1];
                        }
                    }
                    edgeTo[w][h] = to;
                    energy[w][h] += minE;
                }
            }
        }

        // 找到最下一行的最小能量值(出口)
        int end = -1;
        double maxE = Double.MAX_VALUE;
        int h = height() - 1;
        for (int w = 0; w < width(); ++w) {
            if (energy[w][h] <= maxE) {
                end = w;
                maxE = energy[w][h];
            }
        }

        // 最小能量值处往回找到路径 seam
        int seam[] = new int[height()];
        for (int hh = height() - 1; hh >= 0; --hh) {
            seam[hh] = end;
            end = edgeTo[end][hh];
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        checkHorizontalSeam(seam);

        Picture newPic = new Picture(width(), height() - 1);
        for (int w = 0; w < newPic.width(); ++w) {
            for (int h = 0; h < newPic.height(); ++h) {
                if (h < seam[w])
                    newPic.set(w, h, pic.get(w, h));
                else
                    newPic.set(w, h, pic.get(w, h + 1));
                ;
            }
        }
        pic = newPic;
    }

    private void checkHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("removeHorizontalSeam is called with null arg");
        if (seam.length != width())
            throw new IllegalArgumentException("removeHorizontalSeam is called with an arrayof the wrong length");
        for (int i = 0; i < width(); ++i) {
            if (seam[i] < 0 || seam[i] >= height())
                throw new IllegalArgumentException(
                        "removeHorizaontalSeam is called with an invalid seam (wrong filed)");
            if (i < width() - 1 && Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException(
                        "removeHorizontalSeam is called with an invalid seam (not continuous)");
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException(
                        "removeHorizontalSeam is called with an invalid seam (not continuous)");
        }
        if (pic.height() <= 1)
            throw new IllegalArgumentException("picture height less than or equal to 1");
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        checkVerticalSeam(seam);

        Picture newPic = new Picture(width() - 1, height());
        for (int w = 0; w < newPic.width(); ++w) {
            for (int h = 0; h < newPic.height(); ++h) {
                if (w < seam[h])
                    newPic.set(w, h, pic.get(w, h));
                else
                    newPic.set(w, h, pic.get(w + 1, h));

            }
        }
        pic = newPic;
    }

    private void checkVerticalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("removeVerticalSeam is called with null arg");
        if (seam.length != height())
            throw new IllegalArgumentException("removeVerticalSeam is called with an array of the wrong length");
        for (int i = 0; i < height(); ++i) {
            if (seam[i] < 0 || seam[i] >= width())
                throw new IllegalArgumentException("removeVerticalSeam is called with an invalid seam (wrong field)");
            if (i < height() - 1 && Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException(
                        "removeVerticalSeam is called with an invalid seam (not continuous)");
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException(
                        "removeVerticalSeam is called with an invalid seam (not continuous)");
        }
        if (pic.width() <= 1)
            throw new IllegalArgumentException("picture width less than or equal to 1");
    }

    // unit testing (optional)
    public static void main(String[] args) {

    }
}