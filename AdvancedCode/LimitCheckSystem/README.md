Вы — backend-разработчик в финтех компании.
Компания предоставляет платежные услуги и должна контролировать лимиты пользователей.
Product owner просит создать систему проверки лимитов перед проведением платежей.

### Определения

Платеж:
- сумма (в рублях)
- тип операции (только списание)
- timestamp

Лимиты пользователя:
- дневной лимит по сумме
- максимальный размер одной операции

История операций:
- список совершенных платежей пользователя

### Задача
Написать систему проверки лимитов, которая:
- на вход получает платеж и id пользователя
- проверяет все установленные лимиты
- возвращает результат: можно ли провести операцию
- если нельзя, то указывает причину (какой лимит превышен)

### Ограничения
- в рамках данной задачи считаем, что все платежи одного пользователя происходят строго последовательно.
  Во время проверки лимита не может быть проведен платеж того же пользователя.

Предлагаемое чатгпт решение
```java
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/** Платеж (только списание) */
record Payment(double amount, Instant timestamp) { }

/** Лимиты пользователя */
record UserLimits(double dailyLimit, double singleLimit) { }

/** Результат проверки */
record LimitCheckResult(boolean allowed, String reason) {
    static LimitCheckResult ok() { return new LimitCheckResult(true, "OK"); }
    static LimitCheckResult fail(String reason) { return new LimitCheckResult(false, reason); }

    @Override public String toString() {
        return allowed ? "Операция разрешена" : "Отклонено: " + reason;
    }
}

/** Сервис проверки лимитов */
class LimitChecker {
    private static final ZoneId ZONE = ZoneId.of("Europe/Moscow");

    public LimitCheckResult check(Payment payment, UserLimits limits, List<Payment> history) {
        // Проверка лимита на одну операцию
        if (payment.amount() > limits.singleLimit()) {
            return LimitCheckResult.fail("Превышен лимит одной операции: " + limits.singleLimit());
        }

        // Фильтруем платежи за тот же день
        LocalDate today = LocalDate.ofInstant(payment.timestamp(), ZONE);
        double sumToday = history.stream()
                .filter(p -> LocalDate.ofInstant(p.timestamp(), ZONE).equals(today))
                .mapToDouble(Payment::amount)
                .sum();

        if (sumToday + payment.amount() > limits.dailyLimit()) {
            return LimitCheckResult.fail("Превышен дневной лимит: " + limits.dailyLimit());
        }

        return LimitCheckResult.ok();
    }
}
```