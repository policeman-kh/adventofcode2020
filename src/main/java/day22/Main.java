package day22;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import client.ReadDataClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    Queue<Integer> deck1 = new ArrayDeque<>();
    Queue<Integer> deck2 = new ArrayDeque<>();

    private void execute() {
        List<String> data = ReadDataClient.getInputDataList("day22.txt");
        boolean readPlayer1 = false;
        boolean readPlayer2 = false;
        for (String line : data) {
            if (line.length() == 0) {
                continue;
            }
            if (line.startsWith("Player 1:")) {
                readPlayer1 = true;
                continue;
            }
            if (line.startsWith("Player 2:")) {
                readPlayer1 = false;
                readPlayer2 = true;
                continue;
            }
            if (readPlayer1) {
                deck1.add(Integer.parseInt(line));
            }
            if (readPlayer2) {
                deck2.add(Integer.parseInt(line));
            }
        }
        int winner = playGame();
        log.info("{}", calcScore(winner == 1 ? deck1 : deck2));
    }

    private long calcScore(Queue<Integer> deck) {
        List<Integer> cards = deck.stream().collect(Collectors.toList());
        long ret = 0;
        for (int i = 0; i < cards.size(); i++) {
            ret += (cards.get(cards.size() - 1 - i) * (i + 1));
        }
        return ret;
    }

    private int playGame() {
        int round = 1;
        while (true) {
            int card1 = deck1.poll();
            int card2 = deck2.poll();
            if (card1 > card2) {
                deck1.add(card1);
                deck1.add(card2);
            }
            if (card1 < card2) {
                deck2.add(card2);
                deck2.add(card1);
            }
            round++;
            if (deck1.isEmpty()) {
                return 2;
            }
            if (deck2.isEmpty()) {
                return 1;
            }
        }
    }
}
