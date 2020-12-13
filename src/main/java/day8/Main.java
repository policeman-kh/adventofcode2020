package day8;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import client.ReadDataClient;

public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    List<String> data;
    public void execute(){
        data = ReadDataClient.getInputDataList("day8.txt");

        Set<Integer> history = new HashSet<>();
        int acc = 0;
        int p = 0;
        while (true) {
            String line = data.get(p);
            if(history.contains(p)){
                break;
            }else{
                history.add(p);
            }
            if(isNop(line)) {
                p++;
            }else if(isAcc(line)) {
                p++;
                acc += getNumber(line);
            }else if(isJmp(line)) {
                p += getNumber(line);
            }
        }
        System.out.println(acc);
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
