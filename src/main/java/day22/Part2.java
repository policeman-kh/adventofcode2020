package day22;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import client.ReadDataClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    private void execute() {
        List<String> data = ReadDataClient.getInputDataList("day22.txt");
        boolean readPlayer1 = false;
        boolean readPlayer2 = false;

        Queue<Integer> deck1 = new ArrayDeque<>();
        Queue<Integer> deck2 = new ArrayDeque<>();
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
        int winner = playGame(deck1, deck2);
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

    private int playGame(Queue<Integer> deck1, Queue<Integer> deck2) {
        int cnt = 0;
        Set<String> marked = new HashSet<>();
        while (true) {
            log.info("round : {}", cnt + 1);
            log.info("Player 1 size: {} Player 2 size: {}", deck1.size(), deck2.size());
            int card1 = deck1.poll();
            int card2 = deck2.poll();
            log.info("Player 1 plays: {} Player 2 plays: {}", card1, card2);
            if(isInfiniteLoop(deck1, deck2, marked)) {
                return 1;
            }
            if (isGotoSubGame(card1, card2, deck1, deck2)) {
                log.info("round : {} Playing a sub-game to determine the winner", cnt + 1);
                Queue<Integer> sub1 = cloneQueue(deck1, card1);
                Queue<Integer> sub2 = cloneQueue(deck2, card2);
                int subWinner = playGame(sub1, sub2);
                if (subWinner == 1) {
                    deck1.add(card1);
                    deck1.add(card2);
                } else {
                    deck2.add(card2);
                    deck2.add(card1);
                }
            } else {
                if (card1 > card2) {
                    deck1.add(card1);
                    deck1.add(card2);
                }
                if (card1 < card2) {
                    deck2.add(card2);
                    deck2.add(card1);
                }
            }
            cnt++;
            if (deck1.isEmpty()) {
                return 2;
            }
            if (deck2.isEmpty()) {
                return 1;
            }
        }
    }

    private Queue<Integer> cloneQueue(Queue<Integer> src, int num) {
        List<Integer> list = src.stream().collect(Collectors.toList());
        Queue<Integer> dist = new ArrayDeque<>();
        for (int i = 0; i < num; i++) {
            dist.add(list.get(i));
        }
        return dist;
    }

    private boolean isGotoSubGame(int card1, int card2, Queue<Integer> deck1, Queue<Integer> deck2) {
        return deck1.size() >= card1
               && deck2.size() >= card2;
    }

    private boolean isInfiniteLoop(Queue<Integer> deck1, Queue<Integer> deck2, Set<String> marked) {
        List<String> list1 = deck1.stream().map(Object::toString).collect(Collectors.toList());
        List<String> list2 = deck2.stream().map(Object::toString).collect(Collectors.toList());
        String str = String.join(",", list1) + "_" + String.join(",", list2);
        if (marked.contains(str)) {
            return true;
        } else {
            marked.add(str);
            return false;
        }
    }
}
