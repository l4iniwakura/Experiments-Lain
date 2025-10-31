package com.github.l4iniwakura.experiments;

import java.util.*;

public class ATM_DP_Memo {
    private final Map<Integer, Integer> bills;

    public ATM_DP_Memo(Map<Integer, Integer> bills) {
        this.bills = new TreeMap<>(Comparator.reverseOrder());
        this.bills.putAll(bills);
    }

    public Map<Integer, Integer> withdraw(int amount) {
        Map<Integer, Integer> result = dfs(new ArrayList<>(bills.keySet()), 0, amount, new HashMap<>());
        return result == null ? Collections.emptyMap() : result;
    }

    private Map<Integer, Integer> dfs(List<Integer> denominations, int index, int remaining, Map<MemoKey, Map<Integer, Integer>> memo) {
        if (remaining == 0) {
            return new HashMap<>(); // нашли точную комбинацию
        }
        if (index >= denominations.size() || remaining < 0) {
            return null;
        }

        var key = new MemoKey(index, remaining);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        int denomination = denominations.get(index);
        int count = bills.get(denomination);

        for (int k = 0; k <= count; k++) {
            int next = remaining - k * denomination;
            if (next < 0) {
                break;
            }

            Map<Integer, Integer> result = dfs(denominations, index + 1, next, memo);
            if (result != null) {
                if (k > 0) {
                    result.put(denomination, k);
                }
                memo.put(key, result);
                return result;
            }
        }

        memo.put(key, null);
        return null;
    }

    public static void main(String[] args) {
        Map<Integer, Integer> init = Map.of(427, 32, 56, 12, 145, 41,765, 123);
        ATM_DP_Memo atm = new ATM_DP_Memo(init);
        int amount = 5123;
        Map<Integer, Integer> res = atm.withdraw(amount);
        if (res.isEmpty()) System.out.println("Cannot dispense " + amount);
        else System.out.println("Withdrawn: " + res);
    }

    private record MemoKey(int index, int remaining) {
    }
}
