package day24;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.ReadDataClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    static Map<String, Point> POINT_MAP = pointMap();
    static final char NONE = ' ';

    static Map<Point, Integer> TILES = new HashMap<>();
    static final int BLACK = 1;
    static final int WHITE = 0;

    private void execute() {
        List<String> data = ReadDataClient.getInputDataList("day24.txt");
        for (String line : data) {
            executeLine(line);
        }
        for (int i = 1; i <= 100; i++) {
            flip();
            calcBlack(i);
        }
    }

    private void flip() {
        Map<Point, Integer> map = new HashMap<>(TILES);
        for (Point point : map.keySet()) {
            prepare(point);
        }
        map = new HashMap<>(TILES);
        for (Point point : map.keySet()) {
            if (map.get(point) == BLACK) {
                flip1(point, map);
            }
        }
        for (Point point : map.keySet()) {
            if (map.get(point) == WHITE) {
                flip2(point, map);
            }
        }
    }

    private void prepare(Point point) {
        for (Point direction : POINT_MAP.values()) {
            int x = point.getX() + direction.getX();
            int y = point.getY() + direction.getY();
            Point adjacent = new Point(x, y);
            Integer adjacentColor = TILES.get(adjacent);
            if (adjacentColor == null) {
                TILES.put(adjacent, WHITE);
            }
        }
    }

    private void flip1(Point point, Map<Point, Integer> map) {
        int cnt = 0;
        for (Point direction : POINT_MAP.values()) {
            int x = point.getX() + direction.getX();
            int y = point.getY() + direction.getY();
            Point adjacent = new Point(x, y);
            Integer adjacentColor = map.get(adjacent);
            if (adjacentColor != null && adjacentColor == BLACK) {
                cnt++;
            }
        }
        if (cnt == 0 || cnt > 2) {
            TILES.put(point, WHITE);
            //log.info("flip 1..{}", point);
        }
    }

    private void flip2(Point point, Map<Point, Integer> map) {
        int cnt = 0;
        for (Point direction : POINT_MAP.values()) {
            int x = point.getX() + direction.getX();
            int y = point.getY() + direction.getY();
            Point adjacent = new Point(x, y);
            Integer adjacentColor = map.get(adjacent);
            if (adjacentColor != null && adjacentColor == BLACK) {
                cnt++;
            }
        }
        if (cnt == 2) {
            TILES.put(point, BLACK);
            //log.info("flip 2..{}", point);
        }
    }

    private void executeLine(String line) {
        int x = 0;
        int y = 0;
        char[] chars = line.toCharArray();

        char prefix = NONE;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == 's' || chars[i] == 'n') {
                prefix = chars[i];
                continue;
            } else {
                String key = prefix != NONE
                             ? String.valueOf(prefix) + String.valueOf(chars[i])
                             : String.valueOf(chars[i]);
                Point point = POINT_MAP.get(key);
                x += point.getX();
                y += point.getY();
                prefix = NONE;
            }
        }
        updateTiles(x, y);
    }

    private void updateTiles(int x, int y) {
        Point point = new Point(x, y);
        if (!TILES.containsKey(point)) {
            TILES.put(point, BLACK);
        } else {
            int value = TILES.get(point);
            TILES.put(point, value == BLACK ? WHITE : BLACK);
        }
    }

    private void calcBlack(int num) {
        int blackCnt = 0;
        int whiteCnt = 0;
        for (Point point : TILES.keySet()) {
            if (TILES.get(point) == BLACK) {
                blackCnt++;
            } else {
                whiteCnt++;
            }
        }
        log.info("### {}", num);
        log.info("blackCnt={}", blackCnt);
        log.info("whiteCnt={}", whiteCnt);
    }

    private static Map<String, Point> pointMap() {
        Map<String, Point> pointMap = new HashMap<>();
        pointMap.put("e", new Point(2, 0));
        pointMap.put("w", new Point(-2, 0));
        pointMap.put("se", new Point(1, -1));
        pointMap.put("sw", new Point(-1, -1));
        pointMap.put("nw", new Point(-1, 1));
        pointMap.put("ne", new Point(1, 1));
        return pointMap;
    }

    @AllArgsConstructor
    @Data
    private static class Point {
        int x;
        int y;
    }
}
