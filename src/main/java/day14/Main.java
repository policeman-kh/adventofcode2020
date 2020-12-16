package day14;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ReadDataClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    Pattern maskPattern = Pattern.compile("^mask = ([X01]+)$");
    Pattern memPattern = Pattern.compile("^mem\\[([0-9]+)\\] = ([0-9]+)$");

    List<String> data;
    Map<Integer, Long> result = new HashMap<>();
    String mask;

    private void execute() {
        data = ReadDataClient.getInputDataList("day14.txt");
        for (String line : data) {
            Matcher maskMatcher = maskPattern.matcher(line);
            if (maskMatcher.matches()) {
                mask = maskMatcher.group(1);
                continue;
            }
            Matcher memMatcher = memPattern.matcher(line);
            if (memMatcher.matches()) {
                int num = Integer.parseInt(memMatcher.group(1));
                long val = Long.parseLong(memMatcher.group(2));
                result.put(num, getResult(val, mask));
                continue;
            }
        }
        long total = 0;
        for (Integer num : result.keySet()) {
            total += result.get(num);
        }
        log.info("{}", total);
    }

    private static long getResult(long x, String mask) {
        int[] ret = new int[36];
        int[] arr = changeBinary(x);
        char[] masks = mask.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            int v = arr[i];
            char m = masks[masks.length - 1 - i];
            if (m == 'X') {
                ret[i] = v;
            }
            if (m == '0') {
                ret[i] = 0;
            }
            if (m == '1') {
                ret[i] = 1;
            }
        }
        return changeLong(ret);
    }

    private static long changeLong(int[] arr) {
        long ret = 0;
        for (int i = 0; i < arr.length; i++) {
            ret += arr[i] * Math.pow(2, i);
        }
        return ret;
    }

    private static int[] changeBinary(long x) {
        int[] ret = new int[36];
        int n = 0;
        while (x >= 2) {
            ret[n] = (int) x % 2;
            n++;
            x = x / 2;
        }
        ret[n] = (int) x;
        return ret;
    }
}
