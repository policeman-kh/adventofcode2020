package day9;

import java.util.List;
import java.util.stream.Collectors;

import client.ReadDataClient;

public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    public void execute() {
        List<Long> data = ReadDataClient.getInputDataList("day9.txt")
                                           .stream()
                                           .map(Long::parseLong)
                                           .collect(Collectors.toList());
        executeLocal(data, 25);
    }

    private void executeLocal(List<Long> data, int preamble) {
        int start = 0;
        int end = start + preamble;
        for (int i = end; i < data.size(); i++) {
            List<Long> stack = data.subList(start, end);
            long target = data.get(i);
            if(!isValid(target, stack)){
                System.out.println(target);
                return;
            }
            start++;
            end++;
        }
    }

    private boolean isValid(long target, List<Long> stack){
        for (int j = 0; j < stack.size(); j++) {
            for (int k = 0; k < stack.size(); k++) {
                if(stack.get(j) + stack.get(k) == target){
                    return true;
                }
            }
        }
        return false;
    }
}
