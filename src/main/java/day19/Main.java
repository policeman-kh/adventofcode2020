package day19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import client.ReadDataClient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    List<String> messages = new ArrayList<>();

    private void execute() {
        List<String> data = ReadDataClient.getInputDataList("day19.txt");
        boolean isCondition = true;
        for (String line : data) {
            if (line.length() == 0) {
                isCondition = false;
                continue;
            }
            if (isCondition) {
                setupPointers(line);
            } else {
                messages.add(line);
            }
        }
        for (int num : pointerMap.keySet()) {
            convertPointer(pointerMap.get(num));
        }
        Pointer pointer = pointerMap.get(0);
        StringBuilder sb = new StringBuilder();
        makeCondition(pointer, sb);

        Pattern pattern = Pattern.compile(sb.toString());
        int cnt = 0;
        for (String message : messages) {
            if (pattern.matcher(message).matches()) {
                cnt++;
            }
        }
        log.info("{}", cnt);
    }

    private void makeCondition(Pointer pointer, StringBuilder sb) {
        if (pointer.getCharacter() != null) {
            sb.append(pointer.getCharacter());
            return;
        }
        if (pointer.getPointers().size() == 1) {
            for (Pointer pointer1 : pointer.getPointers().get(0)) {
                makeCondition(pointer1, sb);
            }
        } else {
            sb.append("(");
            for (Pointer pointer1 : pointer.getPointers().get(0)) {
                makeCondition(pointer1, sb);
            }
            sb.append("|");
            for (Pointer pointer1 : pointer.getPointers().get(1)) {
                makeCondition(pointer1, sb);
            }
            sb.append(")");
        }
    }

    Pattern pattern = Pattern.compile("^([0-9]+): (.+)$");

    Map<Integer, Pointer> pointerMap = new HashMap<>();

    private void setupPointers(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            int num = Integer.parseInt(matcher.group(1));
            String condition = matcher.group(2);

            Pointer pointer = new Pointer();
            pointer.setNum(num);
            pointer.setCondition(condition);
            if (condition.equals("\"a\"")) {
                pointer.setCharacter('a');
            } else if (condition.equals("\"b\"")) {
                pointer.setCharacter('b');
            }
            pointerMap.put(num, pointer);
        }
    }

    private void convertPointer(Pointer pointer) {
        List<String> list = Arrays.asList(pointer.getCondition().split("\\|"));
        List<List<Pointer>> pointers = new ArrayList<>();
        pointers.add(makePointers(list.get(0)));
        if (list.size() == 2) {
            pointers.add(makePointers(list.get(1)));
        }
        pointer.setPointers(pointers);
    }

    private List<Pointer> makePointers(String condition) {
        return Arrays.asList(condition.split(" "))
                     .stream()
                     .map(c -> {
                         try {
                             int i = Integer.parseInt(c);
                             return pointerMap.get(i);
                         } catch (Exception e) {
                             return null;
                         }
                     })
                     .filter(p -> p != null)
                     .collect(Collectors.toList());
    }

    @NoArgsConstructor
    @Data
    private static class Pointer {
        int num;
        List<List<Pointer>> pointers;

        Character character;    // a or b

        String condition;
    }
}
