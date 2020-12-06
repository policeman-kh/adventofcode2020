package day3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }
    int xMax = 323;
    int yMax = 12400;
    String fileName = "day3.txt";
    //int xMax = 11;
    //int yMax = 1240;
    //String fileName = "day3_test.txt";

    public void execute(){
        long answer = 1;
        answer *= count(1, 1);
        answer *= count(1, 3);
        answer *= count(1, 5);
        answer *= count(1, 7);
        answer *= count(2, 1);
        System.out.println(answer);
        //System.out.println(count(1, 3));
    }

    public int count(int addX, int addY) {
        Character[][] data = data();

        int count = 0;
        int x = 0;
        int y = 0;
        while (true) {
            if (isEnd(x + addX, y + addY)) {
                break;
            }
            if (data[x + addX][y + addY] == '#') {
                count++;
            }
            x = x + addX;
            y = y + addY;
        }
        return count;
    }



    private boolean isEnd(int x, int y) {
        return x >= xMax || y >= yMax;
    }

    private Character[][] data() {
        Character[][] array = new Character[323][1240];
        BufferedReader br = null;
        try {
            InputStream in = getClass().getClassLoader()
                                       .getResourceAsStream(fileName);
            br = new BufferedReader(new InputStreamReader(in));
            int n = 0;
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                List<Character> chars = line.chars()
                                            .mapToObj(c -> (char) c)
                                            .collect(Collectors.toList());
                List<Character> added = new ArrayList<>();
                // Loop with enough large number.
                for (int i = 0; i < 400; i++) {
                    added.addAll(chars);
                }
                array[n] = added.toArray(Character[]::new);
                n++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return array;
    }
}
