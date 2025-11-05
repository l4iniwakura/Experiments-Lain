package com.github.mirocidij;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BaseTest {

    @Test
    @DisplayName("Базовый тест для проверки работы зависимости для тестирования")
    void test() {
        Assertions.assertTrue(Boolean.TRUE);
    }
}
