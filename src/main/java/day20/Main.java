package day20;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ReadDataClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    Pattern pattern = Pattern.compile("^Tile ([0-9]+):$");

    private void execute() {
        List<Tile> tiles = getTiles();

        Map<Tile, Integer> countTiles = new HashMap<>();
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            for (int j = 0; j < tiles.size(); j++) {
                Tile tile1 = tiles.get(j);
                if (tile.getId() == tile1.getId()) {
                    continue;
                }
                int count = getCountWithSameOutermostEdges(tile, tile1);
                if (count == 2) {
                    addCountTiles(countTiles, tile);
                }
            }
        }
        long ans = 1;
        for (Tile tile : countTiles.keySet()) {
            int cnt = countTiles.get(tile);
            if (cnt == 2) {
                ans *= tile.getId();
            }
        }
        log.info("{}", ans);
    }

    private void addCountTiles(Map<Tile, Integer> countTiles, Tile tile) {
        if (countTiles.containsKey(tile)) {
            int cnt = countTiles.get(tile);
            countTiles.put(tile, cnt + 1);
        } else {
            countTiles.put(tile, 1);
        }
    }

    private int getCountWithSameOutermostEdges(Tile tile, Tile tile1) {
        int cnt = 0;
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (Arrays.equals(tile.getOutermostEdges(i),
                                  tile1.getOutermostEdges(j))) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    private List<Tile> getTiles() {
        List<String> data = ReadDataClient.getInputDataList("day20.txt");
        List<Tile> tiles = new ArrayList<>();
        int id;
        Tile tile = null;
        int num = 0;
        for (String line : data) {
            if (line.length() == 0) {
                continue;
            }
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                id = Integer.parseInt(matcher.group(1));
                tile = new Tile();
                tile.setId(id);
                tiles.add(tile);
                num = 0;
                continue;
            }
            char[] chars = line.toCharArray();
            if (tile != null && tile.getPoints() == null) {
                tile.points = new char[chars.length][];
            }
            tile.points[num] = chars;
            num++;
        }
        return tiles;
    }

    @Data
    private static class Tile {
        int id;
        char[][] points;

        /**
         * ## 0 ##
         * #     #
         * 2     3
         * #     #
         * ## 1 ##
         */
        public char[] getOutermostEdges(int num) {
            int width = points[0].length;
            switch (num) {
                case 0:
                    return points[0];
                case 1:
                    return points[width - 1];
                case 2:
                    char[] ret1 = new char[width];
                    for (int i = 0; i < width; i++) {
                        ret1[i] = points[i][0];
                    }
                    return ret1;
                case 3:
                    char[] ret2 = new char[width];
                    for (int i = 0; i < width; i++) {
                        ret2[i] = points[i][width - 1];
                    }
                    return ret2;
                case 4:
                    return reverse(getOutermostEdges(0));
                case 5:
                    return reverse(getOutermostEdges(1));
                case 6:
                    return reverse(getOutermostEdges(2));
                case 7:
                    return reverse(getOutermostEdges(3));
                default:
                    return null;
            }
        }

        private static char[] reverse(char[] src) {
            char[] ret = new char[src.length];
            for (int i = src.length - 1, j = 0; i >= 0; i--, j++) {
                ret[j] = src[i];
            }
            return ret;
        }
    }
}
