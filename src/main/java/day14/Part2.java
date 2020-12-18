package day14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ReadDataClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    Pattern maskPattern = Pattern.compile("^mask = ([X01]+)$");
    Pattern memPattern = Pattern.compile("^mem\\[([0-9]+)\\] = ([0-9]+)$");

    List<String> data;
    Map<Long, Long> resultMap = new HashMap<>();
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
                setUpResultMap(mask, num, val);
                continue;
            }
        }
        long ret = 0;
        for (long key : resultMap.keySet()) {
            ret += resultMap.get(key);
        }
        System.out.println(ret);
    }

    private void setUpResultMap(String mask, int num, long val) {
        char[] masked = maskedBinary(mask, changeBinary(num));
        // masked is "000000000000000000000000000000X1101X"
        List<ResultCandidate> candidates = makeCandidates(masked);
        for(ResultCandidate candidate: candidates) {
            resultMap.put(candidate.getValue(), val);
        }
    }

    @AllArgsConstructor
    private static class ResultCandidate {
        int[] result;

        public long getValue(){
            return changeLong(result);
        }
    }

    private List<ResultCandidate> makeCandidates(char[] masked){
        List<ResultCandidate> candidates = new ArrayList<>();
        List<Combination> combinations = makeCombinations(masked);
        for (int i = 0; i < combinations.size(); i++) {
            candidates.add(makeResultCandidate(masked, combinations.get(i).chars));
        }
        return candidates;
    }

    private ResultCandidate makeResultCandidate(char[] masked, char[] chars) {
        int[] ret = new int[36];
        int cnt = 0;
        for (int i = 0; i < masked.length; i++) {
            switch (masked[i]){
                case '0':
                    ret[i] = 0;
                    break;
                case '1':
                    ret[i] = 1;
                    break;
                case 'X':
                    ret[i] = chars[cnt] == '1' ? 1 : 0;
                    cnt++;
                    break;
            }
        }
        return new ResultCandidate(ret);
    }

    private List<Combination> makeCombinations(char[] masked) {
        List<Combination> combinations = new ArrayList<>();
        int countX = countX(masked);
        makeCombination(new char[countX], 0, countX, combinations);
        return combinations;
    }

    private void makeCombination(char[] tmp, int depth, int maxDepth, List<Combination> combinations) {
        if (depth == maxDepth) {
            combinations.add(new Combination(tmp.clone()));
            return;
        }
        for (int i = 0; i < 2; i++) {
            tmp[depth] = i == 1 ? '1' : '0';
            makeCombination(tmp, depth + 1, maxDepth, combinations);
        }
    }

    @AllArgsConstructor
    private static class Combination {
        char[] chars;
    }

    private static int countX(char[] masked) {
        int cnt = 0;
        for (int i = 0; i < masked.length; i++) {
            char c = masked[i];
            if (c == 'X') {
                cnt++;
            }
        }
        return cnt;
    }

    private static char[] maskedBinary(String mask, int[] addresses) {
        char[] ret = new char[36];
        char[] chars = mask.toCharArray();
        for (int i = chars.length - 1, j = 0; i >= 0; i--, j++) {
            char c = chars[i];
            int address = addresses[j];
            if (c == 'X') {
                ret[j] = 'X';
            } else if (c == '0') {
                ret[j] = address == 1 ? '1' : '0';
            } else if (c == '1') {
                ret[j] = '1';
            }
        }
        return ret;
    }

    private static int[] changeBinary(int x) {
        int[] ret = new int[36];
        int n = 0;
        while (x >= 2) {
            ret[n] = x % 2;
            n++;
            x = x / 2;
        }
        ret[n] = x;
        return ret;
    }

    private static long changeLong(int[] arr) {
        long ret = 0;
        for (int i = 0; i < arr.length; i++) {
            ret += arr[i] * Math.pow(2, i);
        }
        return ret;
    }
}
