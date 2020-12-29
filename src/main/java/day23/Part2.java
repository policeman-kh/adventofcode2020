package day23;

import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import day23.CustomLinkedList.Node;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Part2 {
    public static void main(String[] args) {
        new Part2().execute();
    }

    private void execute() {
        //String input = "389125467";
        String input = "467528193";
        List<Integer> list = input.chars()
                                  .mapToObj(Character::getNumericValue)
                                  .collect(toCollection(ArrayList::new));
        fillList(list, 1000000);
        customLinkedList = new CustomLinkedList(list);
        //maxValue = 9;
        while (true) {
            State state = setupState();
            refreshList(state);

            //if (move == 100){
            if (move == 10000000) {
                //outputPart1();
                outputPart2();
                break;
            }
            customLinkedList.next();
            move++;
        }
    }

    private void outputPart2() {
        Node first = customLinkedList.getNodeMap().get(1);
        customLinkedList.setCurrent(first);
        List<Node> nodes = customLinkedList.nextNodes(2);
        log.info("{} {}",nodes.get(0).value, nodes.get(1).value);
        log.info("{}",(long)nodes.get(0).value * nodes.get(1).value);
    }

    private void outputPart1() {
        Node first = customLinkedList.getNodeMap().get(1);
        Node node = first;
        String str = "";
        while (true) {
            node = node.next;
            if (node == first) {
                break;
            }
            str += node.value;
        }
        log.info("{}", str);
    }

    int maxValue;
    CustomLinkedList customLinkedList;
    int move = 1;

    private void fillList(List<Integer> list, int maxLength) {
        int n = 10;
        while (true) {
            list.add(n);
            if (list.size() == maxLength) {
                maxValue = n;
                break;
            }
            n++;
        }
    }

    private void refreshList(State state) {
        customLinkedList.moveNotes(state.getDestination(), state.getPickUps());
    }

    private State setupState() {
        List<Node> pickUpNodes = customLinkedList.nextNodes(3);
        List<Integer> pickUps = pickUpNodes.stream().map(Node::getValue).collect(Collectors.toList());
        int current = customLinkedList.getCurrent().getValue();
        int destination = current - 1;
        if (destination <= 0) {
            destination = maxValue;
        }
        while (true) {
            if (destination != current && !pickUps.contains(destination)) {
                break;
            }
            destination--;
            if (destination <= 0) {
                destination = maxValue;
            }
        }
        State state = new State();
        state.setCurrent(customLinkedList.getCurrent());
        state.setPickUps(pickUpNodes);
        state.setDestination(customLinkedList.getNodeMap().get(destination));
        return state;
    }

    @Data
    private static class State {
        Node current;
        List<Node> pickUps;
        Node destination;
    }
}
