package tasks;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicMarkableReference;


public class LockFreeSkipList<T extends Comparable<T>> {

    int validTopLevelValue = 32;
    private static final int MAX_LEVEL = 32;
    private Node<T> head = new Node<>(null, Integer.MIN_VALUE,validTopLevelValue);
    private Node<T> tail = new Node<>(null, Integer.MAX_VALUE,validTopLevelValue);

    public LockFreeSkipList() {
        for (int i = 0; i < MAX_LEVEL; i++) {
            head.next[i] = new AtomicMarkableReference<>(tail, false);
        }
    }

    public boolean add(T value) {
        int topLevel = randomLevel();
        Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL];
        Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL];
        Node<T> newNode = new Node<>(value, topLevel,validTopLevelValue);

        while (true) {
            boolean found = find(value, preds, succs);

            if (found) {
                return false;
            } else {
                for (int level = 0; level < topLevel; level++) {
                    newNode.next[level] = new AtomicMarkableReference<>(succs[level], false);
                    preds[level].next[level].compareAndSet(succs[level], newNode, false, false);
                }
                return true;
            }
        }
    }

    public boolean remove(T value) {
        Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL];
        Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL];

        while (true) {
            boolean found = find(value, preds, succs);

            if (!found) {
                return false;
            } else {
                Node<T> nodeToRemove = succs[0];
                for (int level = nodeToRemove.topLevel - 1; level >= 0; level--) {
                    boolean[] marked = {false};
                    Node<T> succ = nodeToRemove.next[level].get(marked);
                    nodeToRemove.next[level].attemptMark(succ, true);
                }
                return true;
            }
        }
    }

    public boolean contains(T value) {
        Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL];
        Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL];
        return find(value, preds, succs);
    }

    private boolean find(T value, Node<T>[] preds, Node<T>[] succs) {
        int bottomLevel = 0;
        int key = value.hashCode();

        boolean[] marked = {false};
        boolean snip;

        Node<T> pred, curr = null, succ;
        retry:
        while (true) {
            pred = head;
            for (int level = MAX_LEVEL - 1; level >= bottomLevel; level--) {
                curr = pred.next[level].getReference();
                while (true) {
                    succ = curr.next[level].get(marked);
                    while (marked[0]) {
                        snip = pred.next[level].compareAndSet(curr, succ, false, false);
                        if (!snip) continue retry;
                        curr = pred.next[level].getReference();
                        succ = curr.next[level].get(marked);
                    }
                    if (curr.value != null && curr.value.hashCode() < key) {
                        pred = curr;
                        curr = succ;
                    } else {
                        break;
                    }
                }
                preds[level] = pred;
                succs[level] = curr;
            }
            return (curr.value != null && curr.value.equals(value));
        }
    }

    private int randomLevel() {
        int level = 1;
        while (ThreadLocalRandom.current().nextDouble() < 0.5 && level < MAX_LEVEL) {
            level++;
        }
        return level;
    }

    private static class Node<T> {
        private final T value;
        private final int topLevel;
        private final AtomicMarkableReference<Node<T>>[] next;

        public Node(T value, int key, int topLevel) {
            this.value = value;
            this.topLevel = topLevel;

            this.next = new AtomicMarkableReference[topLevel];
            for (int i = 0; i < topLevel; i++) {
                this.next[i] = new AtomicMarkableReference<>(null, false);
            }
        }
    }
}