package main.data;

import main.task.Task;

import static java.awt.SystemColor.info;

public class Node <T>{
    private T info;
    private Node<T> prev;
    private Node<T> next;

    public Node(Node<T> prev, T info, Node<T> next) {
        this.info = info;
        this.prev = prev;
        this.next = next;
    }

    public Node<T> getPrev() {
        return prev;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }
}
