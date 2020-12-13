package day10;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import client.ReadDataClient;

public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    private void execute() {
        List<Long> inputs = ReadDataClient.getInputDataList("day10.txt")
                                          .stream()
                                          .map(Long::parseLong)
                                          .collect(Collectors.toList());
        inputs.add(0L);
        Collections.sort(inputs);
        inputs.add(inputs.get(inputs.size() - 1) + 3);

        int differencesOfOne = 0;
        long distinctWays = 1;
        for (int i = 1; i < inputs.size(); i++) {
            long prev = inputs.get(i - 1);
            long value = inputs.get(i);
            if (value - 1 == prev){
                differencesOfOne++;
            }else{
                double combinations;
                if (differencesOfOne >= 2) {
                    double n = differencesOfOne + 1;
                    double free = n - 2;

                    double constant = 0;
                    if(free > 2) {
                        constant = free - 2;
                    }
                    combinations = Math.pow(2, free) - constant;
                    distinctWays *= combinations;
                }
                differencesOfOne = 0;
            }
        }
        System.out.println(distinctWays);
    }
}
