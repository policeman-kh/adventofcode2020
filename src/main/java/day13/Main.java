package day13;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import client.ReadDataClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    List<String> data;
    int earliestTime;
    List<Integer> busNumbers;

    private void execute() {
        data = ReadDataClient.getInputDataList("day13.txt");
        earliestTime = Integer.parseInt(data.get(0));
        busNumbers = Arrays.asList(data.get(1).split(","))
                           .stream()
                           .filter(s -> !s.equals("x"))
                           .map(Integer::parseInt)
                           .collect(Collectors.toList());
        int nearestBus = 0;
        int nearestTime = Integer.MAX_VALUE;
        for (int i = 0; i < busNumbers.size(); i++) {
            int busNum = busNumbers.get(i);
            int cnt = 1;
            LOOP:
            while (true) {
                int time = busNum * cnt;
                if (time >= earliestTime) {
                    if (time < nearestTime) {
                        nearestTime = time;
                        nearestBus = busNum;
                    }
                    break LOOP;
                }
                cnt++;
            }
        }
        log.info("{}", (nearestTime - earliestTime) * nearestBus);
    }
}
