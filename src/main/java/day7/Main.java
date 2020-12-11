package day7;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import client.ReadDataClient;

public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    int count = 0;
    List<String> data;
    Set<String> countedBags = new HashSet<>();

    public void execute() {
        data = ReadDataClient.getInputDataList("day7.txt");

        String bagName = "shiny gold bag";
        countContainingBag(bagName);

        System.out.println(count);
    }

    private void countContainingBag(String bagName) {
        for (String line: data) {
            if(containBag(line,bagName)){
                String containingBag = getContainingBag(line);
                if(!countedBags.contains(containingBag)) {
                    count++;
                    countedBags.add(containingBag);
                }
                countContainingBag(containingBag);
            }
        }
    }

    private boolean containBag(String line, String bagName){
        return line.indexOf(bagName) > 0;
    }

    private String getContainingBag(String line){
        return line.substring(0, line.indexOf("bags contain")) + "bag";
    }
}
