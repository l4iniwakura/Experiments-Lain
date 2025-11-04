package com.github.l4iniwakura.experiments.ac.demo.deque;

import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static void main(String[] args) {
        var cowal = new CopyOnWriteArrayList<Integer>();
        cowal.add(1);
        cowal.add(1);
        cowal.add(1);
        System.out.println(cowal.subList(0, 2));
    }
}
