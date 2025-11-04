package com.github.l4iniwakura.experiments;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ATM_DP_MemoTest {

    private ATM_DP_Memo atm;

    @Test
    void test() {
        atm = new ATM_DP_Memo(Map.of(
                10, 10,
                100, 2,
                200, 3,
                500, 5,
                1000, 12,
                5000, 2
        ));
        var expected = Map.of(100, 1, 200, 2, 10, 10);
        assertEquals(expected, atm.withdraw(600));
    }

}