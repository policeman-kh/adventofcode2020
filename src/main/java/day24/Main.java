package day24;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.ReadDataClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    static Map<String, Point> POINT_MAP = pointMap();
    static final char NONE = ' ';

    private void execute() {
        List<String> data = ReadDataClient.getInputDataList("day24.txt");
        for (String line : data) {
            executeLine(line);
        }
        calcBlack();
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

    static Map<String, Integer> TILES = new HashMap<>();
    static final int BLACK = 1;
    static final int WHITE = 0;

    private void updateTiles(int x, int y) {
        String key = String.valueOf(x) + '_' + String.valueOf(y);
        if (!TILES.containsKey(key)) {
            TILES.put(key, BLACK);
        } else {
            int value = TILES.get(key);
            TILES.put(key, value == BLACK ? WHITE : BLACK);
        }
    }

    private void calcBlack() {
        int blackCnt = 0;
        int whiteCnt = 0;
        for (String key : TILES.keySet()) {
            if (TILES.get(key) == BLACK) {
                blackCnt++;
            }else {
                whiteCnt++;
            }
        }
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
