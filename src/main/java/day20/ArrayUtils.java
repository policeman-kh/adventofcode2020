package day20;

public class ArrayUtils {
    public static char[][] getRotateOrFlipPoints(char[][] chars, int num, boolean removedEdges) {
        int width = chars[0].length;
        char[][] tmp;
        switch (num) {
            case 0:
                tmp = chars;
                break;
            case 1: // rotate to left 90
                char[][] ret1 = new char[width][width];
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < width; j++) {
                        ret1[width - 1 - j][i] = chars[i][j];
                    }
                }
                tmp = ret1;
                break;
            case 2: // rotate to right 90
                char[][] ret2 = new char[width][width];
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < width; j++) {
                        ret2[j][width - i - 1] = chars[i][j];
                    }
                }
                tmp = ret2;
                break;
            case 3: // rotate to right 180
                char[][] ret3 = new char[width][width];
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < width; j++) {
                        ret3[width - i - 1][width - j - 1] = chars[i][j];
                    }
                }
                tmp = ret3;
                break;
            case 4:
                tmp = flip(getRotateOrFlipPoints(chars,0, false));
                break;
            case 5:
                tmp = flip(getRotateOrFlipPoints(chars,1, false));
                break;
            case 6:
                tmp = flip(getRotateOrFlipPoints(chars,2, false));
                break;
            case 7:
                tmp = flip(getRotateOrFlipPoints(chars,3, false));
                break;
            default:
                throw new IllegalArgumentException();
        }
        return removedEdges ? removeEdges(tmp) : tmp;
    }

    public static char[][] flip(char[][] src) {
        int width = src[0].length;
        char[][] ret = new char[src.length][];
        for (int i = 0; i < width; i++) {
            ret[i] = reverse(src[i]);
        }
        return ret;
    }

    public static char[] reverse(char[] src) {
        char[] ret = new char[src.length];
        for (int i = src.length - 1, j = 0; i >= 0; i--, j++) {
            ret[j] = src[i];
        }
        return ret;
    }

    public static char[][] removeEdges(char[][] src) {
        int width = src[0].length;
        char[][] ret = new char[width - 2][width - 2];
        for (int i = 1, j = 0; i < width - 1; i++, j++) {
            for (int k = 1, l = 0; k < width - 1; k++, l++) {
                ret[j][l] = src[i][k];
            }
        }
        return ret;
    }
}
