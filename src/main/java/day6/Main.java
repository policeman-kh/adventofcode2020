package day6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    public void execute() {
        int answer = 0;
        List<String> dataList = data();
        for (String line : dataList) {
            answer += count(line);
        }
        System.out.println(answer);
    }

    private int count(String line) {
        return (int) line.chars()
                         .mapToObj(c -> (char) c)
                         .distinct()
                         .count();
    }

    public List<String> data() {
        List<String> list = new ArrayList<>();
        BufferedReader br = null;
        try {
            InputStream in = getClass().getClassLoader()
                                       .getResourceAsStream("day6.txt");
            br = new BufferedReader(new InputStreamReader(in));

            String tmp = "";
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                tmp += line;
                if (line.length() == 0) {
                    list.add(tmp);
                    tmp = "";
                }
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
        return list;
    }
}
