package day17;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import client.ReadDataClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    private void execute() {
        List<String> data = ReadDataClient.getInputDataList("day17.txt");

        Set<Position> positions = new HashSet<>();
        for (int i = 0; i < data.size(); i++) {
            String line = data.get(i);
            char[] chars = line.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] == '#') {
                    positions.add(Position.of(j, i, 0));
                }
            }
        }

        for (int i = 0; i < 6; i++) {
            Set<Position> newPositions = new HashSet<>();
            for (Position position : positions) {
                updatePosition(position, positions, newPositions,  true);
            }
            positions = newPositions;
        }
        log.info("{}", positions.size());
    }
/*
    private void output(Set<Position> positions) {
        for (int k = -6; k <= 6; k++) {
            System.out.println("z=" + k);
            for (int i = 0; i <= 15; i++) {
                for (int j = 0; j <= 15; j++) {
                    Position position = Position.of(j, i, k);
                    if (positions.contains(position)) {
                        System.out.print("#");
                    } else {
                        System.out.print(".");
                    }
                }
                System.out.println("");
            }
        }
    }
*/
    private void updatePosition(Position position, Set<Position> positions, Set<Position> newPositions,
                                boolean activedPosition) {
        int activeNeighborCnt = 0;
        for (int k = -1; k <= 1; k++) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0 && k == 0) {
                        continue;
                    }
                    Position neighbor = Position.of(position.x + k, position.y + i, position.z + j);
                    if (positions.contains(neighbor)) {
                        activeNeighborCnt++;
                    }
                    if (activedPosition) {
                        updatePosition(neighbor, positions, newPositions, false);
                    }
                }
            }
        }
        if (activedPosition) {
            if (activeNeighborCnt == 2 || activeNeighborCnt == 3) {
                newPositions.add(position);
            }
        } else {
            if (activeNeighborCnt == 3) {
                newPositions.add(position);
            }
        }
    }

    @AllArgsConstructor(staticName = "of")
    @Data
    private static class Position {
        int x;
        int y;
        int z;
    }
}
