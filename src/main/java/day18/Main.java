package day18;

import static java.lang.Character.getNumericValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import client.ReadDataClient;

public class Main {
    public static void main(String[] args) {
        new Main().execute();
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
                    long val1 = stack.pop();
                    depth--;
                    Stack<Long> newStack = getStack(depth);
                    if (!newStack.isEmpty()) {
                        newStack.push(calc(val1, newStack.pop(), newStack.pop()));
                    } else {
                        newStack.push(val1);
                    }
                    break;
                case ' ':
                    break;
                default:
                    if (isFirst) {
                        stack.push((long) getNumericValue(c));
                        isFirst = false;
                    } else {
                        stack.push(calc(getNumericValue(c), stack.pop(), stack.pop()));
                    }
                    break;
            }
        }
        Stack<Long> stack = getStack(depth);
        return stack.pop();
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
