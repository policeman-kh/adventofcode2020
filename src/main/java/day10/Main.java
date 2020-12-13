package day10;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import client.ReadDataClient;

public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    private void execute(){
        List<Long> data = ReadDataClient.getInputDataList("day10_test.txt")
                                        .stream()
                                        .map(Long::parseLong)
                                        .collect(Collectors.toList());
        Collections.sort(data);
        data.add(data.get(data.size()-1) + 3);

        int jolt1 = 0;
        int jolt3 = 0;

        long prev = 0;
        for(long value : data) {
            if(value == prev + 1){
                jolt1++;
            }
            if(value == prev + 3){
                jolt3++;
            }
            prev = value;
        }
        System.out.println(jolt1 * jolt3);
    }
}
