package day6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    public void execute(){
        int answer = 0;
        List<Group> dataList = data();
        for (Group group : dataList) {
            answer += count(group);
        }
        System.out.println(answer);
    }

    public int count(Group group) {
        HashMap<Character, AtomicInteger> map = null;
        for (String line : group.lines) {
            List<Character> list = line.chars()
                                       .mapToObj(c -> (char) c)
                                       .collect(Collectors.toList());
            if (map == null) {
                map = new HashMap<>();
                for (Character c : list) {
                    map.put(c, new AtomicInteger(1));
                }
            }else{
                for (Character c : list) {
                    if(map.containsKey(c)){
                        map.get(c).incrementAndGet();
                    }
                }
            }
        }
        if(map == null) {
            return 0;
        }
        int ret = 0;
        for(Map.Entry<Character, AtomicInteger> entry : map.entrySet()){
            if(entry.getValue().get() == group.lines.size()){
                ret++;
            }
        }
        return ret;
    }

    public List<Group> data() {
        List<Group> list = new ArrayList<>();
        BufferedReader br = null;
        try {
            InputStream in = getClass().getClassLoader()
                                       .getResourceAsStream("day6.txt");
            br = new BufferedReader(new InputStreamReader(in));

            List<String> tmp = new ArrayList<>();

            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if(line.length() != 0) {
                    tmp.add(line);
                }
                if (line.length() == 0) {
                    list.add(new Group(List.copyOf(tmp)));
                    tmp = new ArrayList<>();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static class Group {
        List<String> lines;
        public Group(List<String> lines){
            this.lines = lines;
        }
    }
}
