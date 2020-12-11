package day7;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import client.ReadDataClient;

public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    int count = 0;
    List<String> data;
    Map<String, List<BagInfo>> bagMap = new HashMap<>();
    Pattern pattern = Pattern.compile("^([1-9]) (.+)$");

    public void execute() {
        data = ReadDataClient.getInputDataList("day7.txt");

        init();

        countContainingBag("shiny gold bag", 1);

        System.out.println(count-1);
    }

    private void init() {
        for (String line : data) {
            String containingBagName = getContainingBagName(line);
            if(containingBagName == null) {
                continue;
            }
            List<BagInfo> bags = getContainedBags(line);
            bagMap.put(containingBagName, bags);
        }
    }

    private String getContainingBagName(String line) {
        if (line.indexOf("contain") == -1) {
            return null;
        }
        return line.substring(0, line.indexOf("contain")).trim().replaceFirst("bags", "bag");
    }

    private List<BagInfo> getContainedBags(String line) {
        return Arrays.asList(line.substring(line.indexOf("contain") + "contain".length())
                                 .replace('.', ' ')
                                 .trim()
                                 .split(","))
                     .stream()
                     .map(s -> makeBagInfo(s))
                     .filter(b -> b != null)
                     .collect(Collectors.toList());
    }

    private static class BagInfo {
        int count;
        String bagName;

        public BagInfo(int count, String bagName) {
            this.count = count;
            this.bagName = bagName;
        }
    }
    
    private BagInfo makeBagInfo(String line) {
        Matcher matcher = pattern.matcher(line.trim());
        if (matcher.matches()) {
            int count = Integer.parseInt(matcher.group(1));
            String bagName = matcher.group(2)
                                    .replaceFirst("bags", "bag");
            return new BagInfo(count, bagName);
        }
        return null;
    }

    private void countContainingBag(String bagName, int numberOfBag) {
        count = count + numberOfBag;

        if (!bagMap.containsKey(bagName)) {
            return;
        }
        List<BagInfo> bags = bagMap.get(bagName);
        for (BagInfo bag : bags) {
            countContainingBag(bag.bagName, numberOfBag * bag.count);
        }
    }
}
