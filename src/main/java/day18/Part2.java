package day18;

import static java.lang.Character.getNumericValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import client.ReadDataClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    List<String> data;

    private void execute() {
        data = ReadDataClient.getInputDataList("day18.txt");
        long amt = 0;
        for (String line : data) {
            if (line.indexOf("#") >= 0) {
                continue;
            }
            amt += calc(line);
        }
        System.out.println(amt);
    }

    private static final long add = -1L;
    private static final long multi = -2L;

    Map<Integer, Stack<Long>> stackMap = new HashMap<>();

    private long calc(String line) {
        char[] chars = line.toCharArray();

        boolean isFirst = true;
        int depth = 0;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            Stack<Long> stack = getStack(depth);
            switch (c) {
                case '+':
                    stack.push(add);
                    break;
                case '*':
                    stack.push(multi);
                    break;
                case '(':
                    isFirst = true;
                    depth++;
                    break;
                case ')':
                    long val = calcStack(stack);
                    depth--;
                    getStack(depth).push(val);
                    break;
                case ' ':
                    break;
                default:
                    long value = getNumericValue(c);
                    if (isFirst) {
                        stack.push(value);
                        isFirst = false;
                    } else {
                        long ope = stack.pop();
                        if (ope == add) {
                            stack.push(value + stack.pop());
                        }
                        if (ope == multi) {
                            stack.push(ope);
                            stack.push(value);
                        }
                    }
                    break;
            }
        }
        Stack<Long> stack = getStack(depth);
        long ret = calcStack(stack);
        return ret;
    }

    private long calcStack(Stack<Long> stack) {
        // Calculate addition first.
        Stack<Long> stack2 = new Stack<>();
        while (true) {
            long val = stack.pop();
            if (stack.isEmpty()) {
                stack2.push(val);
                break;
            }
            long ope = stack.pop();
            if (ope == add) {
                stack.push(val + stack.pop());
            }
            if (ope == multi) {
                stack2.push(val);
                stack2.push(ope);
            }
        }
        // Calculate multiplication.
        while (true) {
            long val = stack2.pop();
            if (stack2.isEmpty()) {
                return val;
            }
            long ope = stack2.pop();
            if (ope == multi) {
                stack2.push(val * stack2.pop());
            }
        }
    }

    private Stack<Long> getStack(int depth) {
        if (!stackMap.containsKey(depth)) {
            Stack<Long> stack = new Stack<>();
            stackMap.put(depth, stack);
        }
        return stackMap.get(depth);
    }

    private static long calc(long val1, long ope, long val2) {
        if (ope == add) {
            return val1 + val2;
        }
        if (ope == multi) {
            return val1 * val2;
        }
        throw new IllegalArgumentException();
    }
}
