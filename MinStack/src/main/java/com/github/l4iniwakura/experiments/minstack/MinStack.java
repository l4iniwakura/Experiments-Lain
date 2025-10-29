package com.github.l4iniwakura.experiments.minstack;

import java.util.LinkedList;

public class MinStack {

    private final LinkedList<Integer> values = new LinkedList<>();
    private final LinkedList<Integer> minValues = new LinkedList<>();

    public MinStack() {
    }

    public void push(int val) {
        values.push(val);
        if (minValues.isEmpty() || minValues.peek() >= val) {
            minValues.push(val);
        }
    }

    public void pop() {
        var pop = values.pop();
        if (minValues.isEmpty() || minValues.peek().intValue() == pop) {
            minValues.pop();
        }
    }

    public int top() {
        return values.isEmpty() ? 0 : values.peek();
    }

    public int getMin() {
        return minValues.isEmpty() ? 0 : minValues.peek();
    }

}
