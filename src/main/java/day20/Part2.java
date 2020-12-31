package day20;

import static day20.ArrayUtils.reverse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ReadDataClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    Pattern pattern = Pattern.compile("^Tile ([0-9]+):$");
    int width;
    int removedPointCnt = 8;

    Map<Tile, Set<Tile>> neighborsMap = new HashMap<>();
    Map<Tile, Integer> rotateNumMap = new HashMap<>();
    Map<Integer, Set<Tile>> neighborCountMap = new HashMap<>();

    Map<Position, Tile> positionTileMap = new HashMap<>();

    private void execute() {
        List<Tile> tiles = getTiles();

        width = (int) Math.sqrt(tiles.size());

        Map<Tile, Integer> countTiles = new HashMap<>();

        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            for (int j = 0; j < tiles.size(); j++) {
                Tile tile1 = tiles.get(j);
                if (tile.getId() == tile1.getId()) {
                    continue;
                }
                if (hasSameOutermostEdges(tile, tile1)) {
                    addCountTiles(countTiles, tile);
                    setupNeighborsMap(tile, tile1);
                    setupNeighborsMap(tile1, tile);
                }
            }
        }
        setupNeighborCountMap(countTiles);
        setupPositionTileMap();
        setupRotateNumMap();

        Image image = getImage();
        int max = 0;
        for (int i = 0; i < 8; i++) {
            int cnt = getMatchCount(image.getRotateOrFlipPoints(i));
            max = Math.max(max, cnt);
        }
        log.info("{}", image.getActiveCount() - max * 15);
    }

    private int getMatchCount(char[][] chars) {
        Set<Position> checkedPosition = new HashSet<>();

        int cnt = 0;
        int maxX = removedPointCnt * width - 19;
        for (int x = 0; x < maxX; x++) {
            for (int i = 0; i < chars.length - 2; i++) {
                List<Position> positions = new ArrayList<>();
                positions.add(Position.of(i, 18 + x));
                positions.add(Position.of(i + 1, x));
                positions.add(Position.of(i + 1, 5 + x));
                positions.add(Position.of(i + 1, 6 + x));
                positions.add(Position.of(i + 1, 11 + x));
                positions.add(Position.of(i + 1, 12 + x));
                positions.add(Position.of(i + 1, 17 + x));
                positions.add(Position.of(i + 1, 18 + x));
                positions.add(Position.of(i + 1, 19 + x));
                positions.add(Position.of(i + 2, 1 + x));
                positions.add(Position.of(i + 2, 4 + x));
                positions.add(Position.of(i + 2, 7 + x));
                positions.add(Position.of(i + 2, 10 + x));
                positions.add(Position.of(i + 2, 13 + x));
                positions.add(Position.of(i + 2, 16 + x));
                if (isMatchedPosition(chars, positions, checkedPosition)) {
                    cnt++;
                    checkedPosition.addAll(positions);
                }
            }
        }
        return cnt;
    }

    private boolean isMatchedPosition(char[][] chars, List<Position> positions, Set<Position> checkedPosition) {
        for (Position position : positions) {
            if (checkedPosition.contains(position)) {
                return false;
            }
            if (chars[position.y][position.x] != '#') {
                return false;
            }
        }
        return true;
    }

    private Image getImage() {
        char[][] imageChars = new char[removedPointCnt * width][removedPointCnt * width];
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = positionTileMap.get(Position.of(x, y));
                int rotateNum = rotateNumMap.get(tile);
                char[][] chars = tile.getRotateOrFlipPoints(rotateNum, true);
                for (int i = 0; i < removedPointCnt; i++) {
                    for (int j = 0; j < removedPointCnt; j++) {
                        imageChars[i + y * removedPointCnt][j + x * removedPointCnt] = chars[i][j];
                    }
                }
            }
        }
        return new Image(imageChars);
    }

    @AllArgsConstructor
    @Data
    private static class Image {
        char[][] chars;

        public char[][] getRotateOrFlipPoints(int num) {
            return ArrayUtils.getRotateOrFlipPoints(chars, num, false);
        }

        public int getActiveCount() {
            int ret = 0;
            int width = chars[0].length;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < width; j++) {
                    if (chars[i][j] == '#') {
                        ret++;
                    }
                }
            }
            return ret;
        }
    }

    private void setupRotateNumMap() {
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = positionTileMap.get(Position.of(x, y));
                Map<Edge, Tile> neighborMap = getNeighborMap(x, y);
                for (int i = 0; i < 8; i++) {
                    char[][] chars = tile.getRotateOrFlipPoints(i, false);
                    if (!isMatchNeighbor(chars, Edge.LEFT, Edge.RIGHT, neighborMap)) {
                        continue;
                    }
                    if (!isMatchNeighbor(chars, Edge.RIGHT, Edge.LEFT, neighborMap)) {
                        continue;
                    }
                    if (!isMatchNeighbor(chars, Edge.TOP, Edge.BOTTOM, neighborMap)) {
                        continue;
                    }
                    if (!isMatchNeighbor(chars, Edge.BOTTOM, Edge.TOP, neighborMap)) {
                        continue;
                    }
                    rotateNumMap.put(tile, i);
                    break;
                }
            }
        }
    }

    private boolean isMatchNeighbor(char[][] chars, Edge neighborEdge, Edge ownEdge,
                                    Map<Edge, Tile> neighborMap) {
        if (!neighborMap.containsKey(neighborEdge)) {
            return true;
        }
        boolean match = false;
        for (int j = 0; j < 8; j++) {
            char[][] neighbor = neighborMap.get(neighborEdge).getRotateOrFlipPoints(j, false);
            if (Arrays.equals(getEdges(neighborEdge, chars), getEdges(ownEdge, neighbor))) {
                return true;

            }
        }
        return false;
    }

    private Map<Edge, Tile> getNeighborMap(int x, int y) {
        Map<Edge, Tile> ret = new HashMap<>();
        if (x - 1 >= 0) {
            ret.put(Edge.LEFT, positionTileMap.get(Position.of(x - 1, y)));
        }
        if (x + 1 < width) {
            ret.put(Edge.RIGHT, positionTileMap.get(Position.of(x + 1, y)));
        }
        if (y - 1 >= 0) {
            ret.put(Edge.TOP, positionTileMap.get(Position.of(x, y - 1)));
        }
        if (y + 1 < width) {
            ret.put(Edge.BOTTOM, positionTileMap.get(Position.of(x, y + 1)));
        }
        return ret;
    }

    private enum
    Edge {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    private static char[] getEdges(Edge edge, char[][] chars) {
        int width = chars[0].length;
        switch (edge) {
            case TOP: // top
                return chars[0];
            case BOTTOM: // bottom
                return chars[width - 1];
            case LEFT: // left
                char[] ret1 = new char[width];
                for (int i = 0; i < width; i++) {
                    ret1[i] = chars[i][0];
                }
                return ret1;
            case RIGHT: // right
                char[] ret2 = new char[width];
                for (int i = 0; i < width; i++) {
                    ret2[i] = chars[i][width - 1];
                }
                return ret2;
            default:
                return null;
        }
    }

    private char[] concat(char[]... arrays) {
        int length = 0;
        for (char[] array : arrays) {
            length += array.length;
        }
        char[] result = new char[length];
        int pos = 0;
        for (char[] array : arrays) {
            for (char c : array) {
                result[pos] = c;
                pos++;
            }
        }
        return result;
    }

    private void setupPositionTileMap() {
        Set<Tile> checked = new HashSet<>();
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = getTileFromNeighborCountMap(x, y, checked);
                positionTileMap.put(Position.of(x, y), tile);
            }
        }
    }

    private boolean isCorner(int x, int y) {
        return (x == 0 && y == 0)
               || (x == width - 1 && y == width - 1)
               || (x == 0 && y == width - 1)
               || (x == width - 1 && y == 0);
    }

    private boolean isEdge(int x, int y) {
        return x == 0 || x == width - 1 || y == 0 || y == width - 1;
    }

    private Tile getTileFromNeighborCountMap(int x, int y, Set<Tile> checked) {
        if (isCorner(x, y)) {
            return getTileFromNeighbors(x, y, neighborCountMap.get(2), checked);
        } else if (isEdge(x, y)) {
            return getTileFromNeighbors(x, y, neighborCountMap.get(3), checked);
        } else {
            return getTileFromNeighbors(x, y, neighborCountMap.get(4), checked);
        }
    }

    private Tile getTileFromNeighbors(int x, int y, Set<Tile> candidates, Set<Tile> checked) {
        for (Tile candidate : candidates) {
            if (checked.contains(candidate)) {
                continue;
            }
            if (x == 0 && y == 0) {
                checked.add(candidate);
                return candidate;
            } else if (x == 0) {
                Tile above = positionTileMap.get(Position.of(x, y - 1));
                if (neighborsMap.get(above).contains(candidate)) {
                    checked.add(candidate);
                    return candidate;
                }
            } else if (y == 0) {
                Tile prev = positionTileMap.get(Position.of(x - 1, y));
                if (neighborsMap.get(prev).contains(candidate)) {
                    checked.add(candidate);
                    return candidate;
                }
            } else {
                Tile prev = positionTileMap.get(Position.of(x - 1, y));
                Tile above = positionTileMap.get(Position.of(x, y - 1));
                if (neighborsMap.get(prev).contains(candidate)
                    && neighborsMap.get(above).contains(candidate)) {
                    checked.add(candidate);
                    return candidate;
                }
            }
        }
        throw new IllegalArgumentException("candidate not found. x=" + x + " y=" + y);
    }

    @AllArgsConstructor(staticName = "of")
    @Data
    private static class Position {
        int x;
        int y;
    }

    private void setupNeighborCountMap(Map<Tile, Integer> countTiles) {
        for (Tile tile : countTiles.keySet()) {
            int cnt = countTiles.get(tile);
            if (neighborCountMap.containsKey(cnt)) {
                neighborCountMap.get(cnt).add(tile);
            } else {
                neighborCountMap.put(cnt, new HashSet<>());
                neighborCountMap.get(cnt).add(tile);
            }
        }
    }

    private void setupNeighborsMap(Tile tile, Tile tile1) {
        if (neighborsMap.containsKey(tile)) {
            neighborsMap.get(tile).add(tile1);
        } else {
            Set<Tile> set = new HashSet<>();
            set.add(tile1);
            neighborsMap.put(tile, set);
        }
    }

    private void addCountTiles(Map<Tile, Integer> countTiles, Tile tile) {
        if (countTiles.containsKey(tile)) {
            int cnt = countTiles.get(tile);
            countTiles.put(tile, cnt + 1);
        } else {
            countTiles.put(tile, 1);
        }
    }

    private boolean hasSameOutermostEdges(Tile tile, Tile tile1) {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (Arrays.equals(tile.getOutermostEdges(i),
                                  tile1.getOutermostEdges(j))) {
                    return true;
                }
            }
        }
        return false;
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

        public char[][] getRotateOrFlipPoints(int num, boolean removedEdges) {
            return ArrayUtils.getRotateOrFlipPoints(points, num, removedEdges);
        }

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
    }
}
