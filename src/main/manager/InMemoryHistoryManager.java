package main.manager;

import jdk.dynalink.NoSuchDynamicMethodException;
import main.data.Node;
import main.task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node<Task>> history;
private Node<Task> head;
private Node<Task> tail;
    public InMemoryHistoryManager() {
        history = new HashMap<>();
        head = null;
        tail = null;
    }

    public void removeNode(Node<Task> node) {
        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        } else {
            head = node.getNext();
        }

        if (node.getNext() != null) {
            node.getNext().setPrev( node.getPrev());
        } else {
            tail = node.getPrev();
        }

        history.remove(node.getInfo().getId());
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        remove(task.getId());
        linkLast(task);
    }

    private void linkLast(Task task) {
        Node<Task> newNode = new Node<>(tail, task, null);
        if (tail != null) {
            tail.setNext(newNode);
        } else {
            head = newNode;
        }
        tail = newNode;
        history.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new LinkedList<>();
        Node current = head;
        while (current != null) {
            history.add((Task) current.getInfo());
            current = current.getNext();
        }
        return history;
    }

    @Override
    public void remove(int id) {
        Node<Task> node = history.get(id);
        if (node != null) {
            removeNode(node);
        }
    }
}
