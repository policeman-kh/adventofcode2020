package day13;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.ReadDataClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    List<String> data;

    private void execute() {
        data = ReadDataClient.getInputDataList("day13.txt");
        List<String> list = Arrays.asList(data.get(1).split(","));

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            if("x".equals(list.get(i))){
                continue;
            }
            map.put(i, Integer.parseInt(list.get(i)));
        }
        boolean first = true;
        long a = map.get(0);
        long b = 0;
        for(Integer added : map.keySet()) {
            if (first) {
                first = false;
                continue;
            } else {
                int x = 0;
                b += added;
                while (true){
                    // ( 7 * x + 1 ) mod 13 = 0
                    if((a * x + b) % map.get(added) == 0){
                        b += a * x - added;
                        a *= map.get(added);
                        break;
                    }
                    x++;
                }
            }
        }
        log.info("{}", b);
    }
}
