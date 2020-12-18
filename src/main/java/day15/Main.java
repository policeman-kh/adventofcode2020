package day15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    Map<Integer, List<Integer>> spokenNumMap = new HashMap<>();

    private void execute() {
        //int inputs[] = { 0, 3, 6 };
        //int inputs[] = { 2,3,1 };
        int inputs[] = { 9, 19, 1, 6, 0, 5, 4 };
        int lastSpoken = -1;
        for (int i = 1; i <= 30000000; i++) {
            if (i > inputs.length) {
                int spokenCount = spokenCount(lastSpoken);
                if (spokenCount < 2) { // First time spoken
                    lastSpoken = 0;
                }
                if (spokenCount >= 2) {
                    lastSpoken = getDifference(lastSpoken);
                }
                markSpoken(lastSpoken, i);
            } else {
                markSpoken(inputs[i - 1], i);
                lastSpoken = inputs[i - 1];
            }
        }
        System.out.println(lastSpoken);
    }

    private void markSpoken(int spokenNum, int turnNum) {
        List<Integer> list;
        if (!spokenNumMap.containsKey(spokenNum)) {
            list = new ArrayList<>();
        } else {
            list = spokenNumMap.get(spokenNum);
        }
        list.add(turnNum);
        spokenNumMap.put(spokenNum, list);
    }

    private int spokenCount(int num) {
        if (!spokenNumMap.containsKey(num)) {
            return 0;
        }
        return spokenNumMap.get(num).size();
    }

    private int getDifference(int num) {
        List<Integer> list = spokenNumMap.get(num);
        return list.get(list.size() - 1) - list.get(list.size() - 2);
    }
}
