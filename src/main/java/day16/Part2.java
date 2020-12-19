package day16;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import client.ReadDataClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    State state = State.VALIDATION_RULE;
    Validations validations = new Validations();
    //Validations departureValidation = new Validation();
    List<String> data;

    List<Integer> yourTickets;
    List<List<Integer>> validNearByTickets = new ArrayList<>();

    private void execute() {
        data = ReadDataClient.getInputDataList("day16.txt");

        for (String line : data) {
            if (line.length() == 0) {
                continue;
            }
            if (line.indexOf("your ticket:") == 0) {
                state = State.YOUR_TICKET;
                continue;
            }
            if (line.indexOf("nearby tickets:") == 0) {
                state = State.NEAR_BY_TICKET;
                continue;
            }
            switch (state) {
                case VALIDATION_RULE:
                    Validation validation = makeValidation(line);
                    if (validation != null) {
                        validations.validations.add(validation);
                        //if(line.contains("departure")) {
                        //    departureValidation.ranges.add(ranges);
                        //}
                    }
                    break;
                case YOUR_TICKET:
                    yourTickets = getTickets(line);
                    break;
                case NEAR_BY_TICKET:
                    List<Integer> validateTickets = getValidateTickets(line);
                    if (validateTickets != null) {
                        validNearByTickets.add(validateTickets);
                    }
                    break;
            }
        }

        int cnt = validations.validations.size();

        Map<Integer, List<String>> candidates = new HashMap<>();
        for (int j = 0; j < cnt; j++) {
            candidates.put(j, new ArrayList<>());
        }

        for (int j = 0; j < cnt; j++) {
            LOOP:
            for (int i = 0; i < validations.validations.size(); i++) {
                Validation validation = validations.validations.get(i);
                for (List<Integer> tickets : validNearByTickets) {
                    int num = tickets.get(j);
                    if (!validation.isValid(num)) {
                        continue LOOP;
                    }
                }
                candidates.get(j).add(validation.name);
            }
        }
        calc(0, candidates, new HashMap<>());

        long ret = 1;
        for (int num : result.keySet()) {
            String name = result.get(num);
            if (name.contains("departure")) {
                ret *= yourTickets.get(num);
                log.info("{} {} = {}", num, name, yourTickets.get(num));
            }
        }
        log.info("{}", ret);
    }

    Map<Integer, String> result;

    private void calc(int num, Map<Integer, List<String>> candidates, Map<Integer, String> done) {
        List<String> list = candidates.get(num);
        if (list == null) {
            result = Map.copyOf(done);
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            String name = list.get(i);
            if (done.containsValue(name)) {
                continue;
            }
            done.put(num, name);
            calc(num + 1, candidates, done);
            done.remove(num);
        }
    }

    private List<Integer> getTickets(String line) {
        return Arrays.asList(line.split(","))
                     .stream()
                     .map(Integer::parseInt)
                     .collect(Collectors.toList());
    }

    private List<Integer> getValidateTickets(String line) {
        List<Integer> tickets = getTickets(line);
        for (int i = 0; i < tickets.size(); i++) {
            int num = tickets.get(i);
            if (!isValidTicket(num)) {
                return null;
            }
        }
        return tickets;
    }

    private Map<Validation, Integer> getValidateMap(int num, List<Validation> validations) {
        Map<Validation, Integer> validateMap = new HashMap<>();
        for (Validation validation : validations) {
            validateMap.put(validation, Integer.valueOf(0));
        }
        for (Validation validation : validations) {
            if (validation.isValid(num)) {
                int cnt = validateMap.get(validation);
                validateMap.put(validation, cnt + 1);
            }
        }
        return validateMap;
    }

    private boolean isValidTicket(int num) {
        Map<Validation, Integer> validateMap = getValidateMap(num, validations.validations);
        for (Validation validation : validateMap.keySet()) {
            if (validateMap.get(validation) > 0) {
                return true;
            }
        }
        return false;
    }

    private static Validation makeValidation(String line) {
        if (line.indexOf(":") == -1) {
            return null;
        }
        String[] params = line.split(":");
        String[] conditions = params[1].split("or");
        List<Range> ranges = Arrays.asList(conditions)
                                   .stream()
                                   .map(s -> {
                                       String[] p = s.split("-");
                                       return new Range(Integer.parseInt(p[0].trim()),
                                                        Integer.parseInt(p[1].trim()));
                                   })
                                   .collect(Collectors.toList());
        return new Validation(params[0], ranges);
    }

    @Data
    private static class Validations {
        List<Validation> validations = new ArrayList<>();
    }

    @AllArgsConstructor
    @Data
    private static class Validation {
        String name;
        List<Range> ranges = new ArrayList<>();

        public boolean isValid(int num) {
            for (Range range : ranges) {
                if (range.isValid(num)) {
                    return true;
                }
            }
            return false;
        }
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
