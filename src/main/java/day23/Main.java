package day23;

import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        new Main().execute();
    }

    private void execute() {
        //String input = "389125467";
        String input = "467528193";
        list = input.chars()
                    .mapToObj(Character::getNumericValue)
                    .collect(toCollection(LinkedList::new));
        int p = 0;
        while (true) {
            State state = setupState(p);
            refreshList(state);
            p = list.indexOf(state.getCurrent());
            p++;
            if (p >= list.size()) {
                p = 0;
            }
            if(move == 100){
                output();
                break;
            }
            move++;
        }
    }

    int move = 1;
    LinkedList<Integer> list;

    private void output(){
        int p = list.indexOf(1);
        p++;
        if (p >= list.size()) {
            p = 0;
        }
        String ret = "";
        for(int i = 0; i < list.size() - 1; i++) {
            ret += list.get(p);
            p++;
            if (p >= list.size()) {
                p = 0;
            }
        }
        log.info("{}", ret);
    }

    private void refreshList(State state) {
        LinkedList<Integer> newList = new LinkedList<>(list);
        for (int removed : state.getPickUps()) {
            newList.remove(newList.indexOf(removed));
        }
        int p = newList.indexOf(state.getDestination());
        newList.add(p + 1, state.getPickUps().get(0));
        newList.add(p + 2, state.getPickUps().get(1));
        newList.add(p + 3, state.getPickUps().get(2));
        list = newList;
    }

    private State setupState(int p) {
        int current = list.get(p);
        p++;
        List<Integer> pickUps = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (p >= list.size()) {
                p = 0;
            }
            pickUps.add(list.get(p));
            p++;
        }
        int destination = current - 1;
        if (destination <= 0) {
            destination = 9;
        }
        while (true) {
            if (destination != current && !pickUps.contains(destination)) {
                break;
            }
            destination--;
            if (destination <= 0) {
                destination = 9;
            }
        }
        State state = new State();
        state.setCurrent(current);
        state.setPickUps(pickUps);
        state.setDestination(destination);
        return state;
    }

    @Data
    private static class State {
        int current;
        List<Integer> pickUps;
        int destination;
    }
}
