package day12;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ReadDataClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    Pattern pattern = Pattern.compile("^([NSEWLRF])([0-9]+)$");
    List<String> data;

    int N = 0;
    int E = 0;
    String direction = "E";

    List<String> rDirections = List.of("E", "S", "W", "N");
    List<String> lDirections = List.of("E", "N", "W", "S");

    private void execute() {
        data = ReadDataClient.getInputDataList("day12.txt");

        for (String line : data) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String command = matcher.group(1);
                int step = Integer.parseInt(matcher.group(2));
                doAction(command, step);
            }
        }
        log.info("{}", Math.abs(N) + Math.abs(E));
    }

    private void doAction(String command, int step) {
        if ("N".equals(command)) {
            N += step;
            return;
        }
        if ("S".equals(command)) {
            N -= step;
            return;
        }
        if ("E".equals(command)) {
            E += step;
            return;
        }
        if ("W".equals(command)) {
            E -= step;
            return;
        }
        if ("F".equals(command)) {
            doAction(direction, step);
            return;
        }
        if ("R".equals(command)) {
            int s = step / 90;

            int p = rDirections.indexOf(direction);
            if (p + s >= 4) {
                direction = rDirections.get(p + s - 4);
            } else {
                direction = rDirections.get(p + s);
            }
            return;
        }
        if ("L".equals(command)) {
            int s = step / 90;

            int p = lDirections.indexOf(direction);
            if (p + s >= 4) {
                direction = lDirections.get(p + s - 4);
            } else {
                direction = lDirections.get(p + s);
            }
            return;
        }
    }
}
