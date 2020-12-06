package day2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    public void execute() {
        long cnt = data().stream()
                         .filter(Part2::validateLine)
                         .peek(c -> System.out.println(c))
                         .count();
        System.out.println(cnt);
    }

    private static boolean validateLine(String line) {
        List<String> records = Arrays.asList(line.split(" "));
        List<String> rules = Arrays.asList(records.get(0).split("-"));
        int matchPosition = Integer.parseInt(rules.get(0)) - 1;
        int matchPosition2 = Integer.parseInt(rules.get(1)) - 1;

        String word = records.get(1).trim().replaceFirst(":", "");
        String password = records.get(2).trim();

        int correct = 0;
        if(password.charAt(matchPosition) == word.charAt(0)){
            correct++;
        }
        if(password.charAt(matchPosition2) == word.charAt(0)){
            correct++;
        }
        return correct == 1;

    }

    public List<String> data() {
        List<String> list = new ArrayList<>();
        BufferedReader br = null;
        try {
            InputStream in = getClass().getClassLoader()
                                       .getResourceAsStream("day2_part2.txt");
            br = new BufferedReader(new InputStreamReader(in));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                list.add(line);
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
