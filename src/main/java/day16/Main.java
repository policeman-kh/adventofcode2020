package day16;

import static day16.Main.State.NEAR_BY_TICKET;
import static day16.Main.State.VALIDATION_RULE;
import static day16.Main.State.YOUR_TICKET;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import client.ReadDataClient;
import lombok.AllArgsConstructor;
import lombok.Data;

public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    State state = VALIDATION_RULE;  //
    Validation validation = new Validation();
    long errorRate = 0;

    List<String> data;

    private void execute() {
        data = ReadDataClient.getInputDataList("day15.txt");
        for (String line : data) {
            if (line.indexOf("your ticket:") == 0) {
                state = YOUR_TICKET;
                continue;
            }
            if (line.indexOf("nearby tickets:") == 0) {
                state = NEAR_BY_TICKET;
                continue;
            }
            switch (state) {
                case VALIDATION_RULE:
                    List<Range> ranges = makeRange(line);
                    if(ranges != null) {
                        validation.ranges.add(ranges);
                    }
                    break;
                case YOUR_TICKET:
                    break;
                case NEAR_BY_TICKET:
                    validateTickets(line);
                    break;
            }
        }
        System.out.println(errorRate);
    }

    private void validateTickets(String line) {
        List<Integer> tickets = Arrays.asList(line.split(","))
                                      .stream()
                                      .map(Integer::parseInt)
                                      .collect(Collectors.toList());

        for (int i = 0; i < tickets.size(); i++) {
            int num = tickets.get(i);
            if (!isValidTicket(num)) {
                errorRate += num;
            }
        }
    }

    private boolean isValidTicket(int num) {
        List<List<Range>> ranges = validation.ranges;
        Map<List<Range>, Integer> validateMap = new HashMap<>();
        for (List<Range> list : ranges) {
            validateMap.put(list, Integer.valueOf(0));
        }
        for (List<Range> list : ranges) {
            boolean isValid = false;
            LOOP:
            for (Range range : list) {
                if (range.isValid(num)) {
                    isValid = true;
                    break LOOP;
                }
            }
            if(isValid){
                int cnt = validateMap.get(list);
                validateMap.put(list, cnt + 1);
            }
        }
        for (List<Range> list : validateMap.keySet()) {
            if(validateMap.get(list) > 0){
                return true;
            }
        }
        return false;
    }

    private static List<Range> makeRange(String line) {
        if (line.indexOf(":") == -1) {
            return null;
        }
        String[] params = line.split(":");
        String[] conditions = params[1].split("or");
        return Arrays.asList(conditions)
                     .stream()
                     .map(s -> {
                         String[] p = s.split("-");
                         return new Range(Integer.parseInt(p[0].trim()),
                                          Integer.parseInt(p[1].trim()));
                     })
                     .collect(Collectors.toList());
    }

    @Data
    private static class Validation {
        List<List<Range>> ranges = new ArrayList<>();
    }

    @AllArgsConstructor
    @Data
    private static class Range {
        int min;
        int max;

        public boolean isValid(int num) {
            return min <= num && num <= max;
        }
    }

    enum State {
        VALIDATION_RULE,
        YOUR_TICKET,
        NEAR_BY_TICKET;
    }
}
