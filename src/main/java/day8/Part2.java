package day8;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import client.ReadDataClient;

public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    public void execute() {
        List<String> data = ReadDataClient.getInputDataList("day8.txt");
        for (int i = 0; i < data.size(); i++) {
            List<String> replaced = new ArrayList<>(data.size());
            replaced.addAll(data);
            final String line = data.get(i);
            if (isNop(line)) {
                String replacedLine = replaceNopToJmp(line);
                replaced.set(i, replacedLine);
                if(executeLocal(replaced)){
                    break;
                }
            } else if (isJmp(line)) {
                String replacedLine = replaceJmpToNop(line);
                replaced.set(i, replacedLine);
                if(executeLocal(replaced)){
                    break;
                }
            }
        }
    }

    private String replaceJmpToNop(String line) {
        return line.replaceFirst("jmp", "nop");
    }

    private String replaceNopToJmp(String line) {
        return line.replaceFirst("nop", "jmp");
    }

    private boolean executeLocal(List<String> data) {
        int acc = 0;
        int p = 0;
        Set<Integer> history = new HashSet<>();
        while (true) {
            String line = data.get(p);
            if (isNullOrEmpty(line)) {
                break;
            }
            if (history.contains(p)) {
                return false;
            } else {
                history.add(p);
            }
            if (isNop(line)) {
                p++;
            } else if (isAcc(line)) {
                p++;
                acc += getNumber(line);
            } else if (isJmp(line)) {
                if (getNumber(line) == 0) {
                    return false;
                }
                p += getNumber(line);

            }
        }
        System.out.println(acc);
        return true;
    }

    private boolean isJmp(String line) {
        return line.indexOf("jmp") == 0;
    }

    private boolean isAcc(String line) {
        return line.indexOf("acc") == 0;
    }

    private boolean isNop(String line) {
        return line.indexOf("nop") == 0;
    }

    private int getNumber(String line) {
        return Integer.parseInt(line.split(" ")[1]);
    }
}
