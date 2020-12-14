package day11;

import java.util.List;

import client.ReadDataClient;

public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    private static char empty = 'L';
    private static char floor = '.';
    private static char occupied = '#';

    private int size_x = 0;
    private int size_y = 0;

    private void execute() {
        char[][] matrix = getMatrix();

        size_x = matrix.length;
        size_y = matrix[0].length;

        while (true) {
            char[][] updated1 = update(matrix);
            char[][] updated2 = update(updated1);
            matrix = update(updated2);
            if (equal(updated1, matrix)) {
                break;
            }
        }
        countOccupied(matrix);
    }

    private void countOccupied(char[][] matrix) {
        int count = 0;
        for (int x = 0; x < matrix.length; x++) {
            char[] row = matrix[x];
            for (int y = 0; y < row.length; y++) {
                if (matrix[x][y] == occupied) {
                    count++;
                }
            }
        }
        System.out.println(count);
    }

    private boolean equal(char[][] matrix1, char[][] matrix2) {
        for (int x = 0; x < matrix1.length; x++) {
            char[] row = matrix1[x];
            for (int y = 0; y < row.length; y++) {
                if (matrix1[x][y] != matrix2[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }

    private char[][] update(char[][] matrix) {
        char[][] ret = new char[size_x][size_y];
        for (int x = 0; x < matrix.length; x++) {
            char[] row = matrix[x];
            for (int y = 0; y < row.length; y++) {
                if (isBecomeOccupied(x, y, matrix)) {
                    ret[x][y] = occupied;
                } else if (isBecomeEmpty(x, y, matrix)) {
                    ret[x][y] = empty;
                } else {
                    ret[x][y] = matrix[x][y];
                }
            }
        }
        return ret;
    }

    private void print(char[][] matrix) {
        System.out.println("---");
        for (int x = 0; x < matrix.length; x++) {
            char[] row = matrix[x];
            for (int y = 0; y < row.length; y++) {
                System.out.print(matrix[x][y]);
            }
            System.out.println("");
        }
    }

    private char getStatusCanSee(int x, int y, int addedX, int addedY, char[][] matrix) {
        int totalAddedX = addedX;
        int totalAddedY = addedY;
        while (true) {
            if (!exist(x + totalAddedX, y + totalAddedY)) {
                return empty;
            }
            if (matrix[x + totalAddedX][y + totalAddedY] != floor) {
                return matrix[x + totalAddedX][y + totalAddedY];
            }
            totalAddedX += addedX;
            totalAddedY += addedY;
        }
    }

    private boolean isBecomeOccupied(int x, int y, char[][] matrix) {
        return getStatusCanSee(x, y, -1, -1, matrix) == empty
               && getStatusCanSee(x, y, 0, -1, matrix) == empty
               && getStatusCanSee(x, y, 1, -1, matrix) == empty
               && getStatusCanSee(x, y, 1, 0, matrix) == empty
               && getStatusCanSee(x, y, 1, 1, matrix) == empty
               && getStatusCanSee(x, y, 0, 1, matrix) == empty
               && getStatusCanSee(x, y, -1, 1, matrix) == empty
               && getStatusCanSee(x, y, -1, 0, matrix) == empty
               && matrix[x][y] == empty;
    }

    private boolean isBecomeEmpty(int x, int y, char[][] matrix) {
        int count = 0;
        if (getStatusCanSee(x, y, -1, -1, matrix) == occupied) {
            count++;
        }
        if (getStatusCanSee(x, y, 0, -1, matrix) == occupied) {
            count++;
        }
        if (getStatusCanSee(x, y, 1, -1, matrix) == occupied) {
            count++;
        }
        if (getStatusCanSee(x, y, 1, 0, matrix) == occupied) {
            count++;
        }
        if (getStatusCanSee(x, y, 1, 1, matrix) == occupied) {
            count++;
        }
        if (getStatusCanSee(x, y, 0, 1, matrix) == occupied) {
            count++;
        }
        if (getStatusCanSee(x, y, -1, 1, matrix) == occupied) {
            count++;
        }
        if (getStatusCanSee(x, y, -1, 0, matrix) == occupied) {
            count++;
        }
        return count >= 5 && matrix[x][y] == occupied;
    }

    private boolean exist(int x, int y) {
        return x >= 0 && y >= 0 && x < size_x && y < size_y;
    }

    private char[][] getMatrix() {
        List<String> data = ReadDataClient.getInputDataList("day11.txt");
        char[][] ret = new char[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            ret[i] = data.get(i).toCharArray();
        }
        return ret;
    }
}
