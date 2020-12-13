package day9;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import client.ReadDataClient;

public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }
    public void execute(){
        List<Long> data = ReadDataClient.getInputDataList("day9.txt")
                                        .stream()
                                        .map(Long::parseLong)
                                        .collect(Collectors.toList());
        List<Long> results = executeLocal(data, 248131121);
        Collections.sort(results);
        System.out.println(results.get(0) + results.get(results.size() -1));
    }

    private List<Long> executeLocal(List<Long> data, long target) {
        for (int i = 0; i < data.size(); i++) {
            long amt = data.get(i);
            List<Long> list = new ArrayList<>();
            list.add(amt);
            for (int j = i+1; j < data.size(); j++) {
                long added = data.get(j);
                list.add(added);
                amt += added;
                if(amt == target) {
                    return list;
                }
            }
        }
        return null;
    }


}
