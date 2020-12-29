package day23;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CustomLinkedList {
    Node current;
    // Map of value and node.
    Map<Integer, Node> nodeMap = new HashMap<>();

    public CustomLinkedList(List<Integer> list) {
        Node prev = null;
        Node first = null;
        for (int val : list) {
            Node node = new Node();
            node.setValue(val);
            node.setPrev(prev);
            nodeMap.put(val, node);
            if (prev != null) {
                prev.setNext(node);
            }
            if (first == null) {
                first = node;
            }
            prev = node;
        }
        prev.setNext(first);
        first.setPrev(prev);
        current = first;
    }

    public void next() {
        current = current.next;
    }

    public List<Node> nextNodes(int size) {
        List<Node> ret = new ArrayList<>();
        Node node = current;
        for (int i = 0; i < size; i++) {
            ret.add(node.next);
            node = node.next;
        }
        return ret;
    }

    public void moveNotes(Node dist, List<Node> movedNodes){
        Node movedTop = movedNodes.get(0);
        Node movedLast = movedNodes.get(movedNodes.size() - 1);

        Node distNext = dist.next;
        Node srcPrev = movedTop.prev;
        Node srcNext = movedLast.next;

        srcPrev.next = srcNext;
        srcNext.prev = srcPrev;

        dist.next = movedTop;
        movedTop.prev = dist;
        distNext.prev = movedLast;
        movedLast.next = distNext;
    }

    @Data
    public static class Node {
        int value;
        Node prev;
        Node next;

        @Override
        public String toString() {
            return "value: " + value + ", "
                   + "prev: " + (prev != null ? "" + prev.value : "") + ", "
                   + "next: " + (next != null ? "" + next.value : "");
        }
    }
}
