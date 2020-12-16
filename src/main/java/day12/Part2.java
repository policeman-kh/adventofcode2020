package day12;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ReadDataClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    Pattern pattern = Pattern.compile("^([NSEWLRF])([0-9]+)$");
    List<String> data;

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
        log.info("{}", Math.abs(ship.N) + Math.abs(ship.E));
    }

    @Data
    private class Ship {
        int N = 0;
        int E = 0;
    }

    @Data
    private class WayPoint {
        int N = 1;
        int E = 10;
    }

    Ship ship = new Ship();
    WayPoint wayPoint = new WayPoint();

    private void doAction(String command, int step) {
        if ("N".equals(command)) {
            wayPoint.N += step;
        }
        if ("S".equals(command)) {
            wayPoint.N -= step;
        }
        if ("E".equals(command)) {
            wayPoint.E += step;
        }
        if ("W".equals(command)) {
            wayPoint.E -= step;
        }
        if ("F".equals(command)) {
            ship.N += wayPoint.N * step;
            ship.E += wayPoint.E * step;
        }
        if ("R".equals(command)) {
            if(step == 180) {
                wayPoint.N *= -1;
                wayPoint.E *= -1;
            }
            if(step == 90) {
                int n = wayPoint.N;
                int e = wayPoint.E;
                wayPoint.N = e * -1;
                wayPoint.E = n * 1;
            }
            if(step == 270) {
                int n = wayPoint.N;
                int e = wayPoint.E;
                wayPoint.N = e * 1;
                wayPoint.E = n * -1;
            }
        }
        if ("L".equals(command)) {
            if(step == 180) {
                wayPoint.N *= -1;
                wayPoint.E *= -1;
            }
            if(step == 90) {
                int n = wayPoint.N;
                int e = wayPoint.E;
                wayPoint.N = e * 1;
                wayPoint.E = n * -1;
            }
            if(step == 270) {
                int n = wayPoint.N;
                int e = wayPoint.E;
                wayPoint.N = e * -1;
                wayPoint.E = n * 1;
            }
        }
    }
}
