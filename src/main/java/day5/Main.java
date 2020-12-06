package day5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import client.ReadDataClient;

public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    public void execute(){
        List<Integer> seats = new ArrayList<>();
        List<String> data = ReadDataClient.getInputDataList("day5.txt");
        int max = 0;
        for (String line: data) {
            seats.add(calc(line));
            max = Math.max(max, calc(line));
        }
        System.out.println(max);

        Collections.sort(seats);
        Set<Integer> set = seats.stream()
                                .collect(Collectors.toSet());
        int first = seats.get(0);
        int last = seats.get(seats.size() -1);
        for(int i = first; i<= last; i++) {
            if(!set.contains(i)){
                System.out.println("missing:" + i);
            }
        }
    }

    private int calc(String line){
        if(line.length() < 10){
            return 0;
        }
        char[] rows = line.substring(0, 7).toCharArray();
        Range rowRange = null;
        for (int i = 0; i < rows.length; i++) {
            if (rowRange == null) {
                rowRange = new Range(0, 127);
            }
            int newRange = rowRange.range / 2;
            if (rows[i] == 'F') {
                rowRange.update(rowRange.s, rowRange.e - newRange);
            } else {
                rowRange.update(rowRange.s + newRange, rowRange.e);
            }
        }
        char[] columns = line.substring(7, 10).toCharArray();
        Range columnRange = null;
        for (int i = 0; i < columns.length; i++) {
            if (columnRange == null) {
                columnRange = new Range(0, 7);
            }
            int newRange = columnRange.range / 2;
            if (columns[i] == 'L') {
                columnRange.update(columnRange.s, columnRange.e - newRange);
            } else {
                columnRange.update(columnRange.s + newRange, columnRange.e);
            }
        }
        return rowRange.s * 8 + columnRange.s;
    }

    private static class Range {
        int s;
        int e;
        int range;

        public Range(int s, int e){
            update(s, e);
        }

        public void update(int s, int e){
            this.s = s;
            this.e = e;
            range = e - s + 1;
        }
    }
}
