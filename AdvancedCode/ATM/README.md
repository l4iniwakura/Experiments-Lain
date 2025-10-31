База

Поставлена задача на реализацию логики банкомата. Важно сделать только функцию снятия. Пополнение или другие методы не обязательны и остаются на усмотрение кандидата.

Это не виртуальный банкомат, а реальный, и будет находиться в реальном мире.

Качество кода должно быть таким, чтобы было не стыдно закоммитить его и показать коллегам. При этом стоит обойтись без оверинжиниринга и построения большой цепочки абстракций.

Проверку счёта пользователя, и тд - считаем что это уже проверено, когда добрались до класса банкомата. Тут обрабатываем взаимодействие непосредственно с банкоматом
Код должен в идеале расширяться под доп валюты и многопоточные кейсы.
Писать код стоит итеративно: сначала базовое решение, а затем фоллоуапы. Это для того, чтобы не переиначивать все потом

Болванка для задачи

```java
/**
* Банкомат.
* Инициализируется набором купюр и умеет выдавать купюры для заданной суммы, либо отвечать отказом.
* При выдаче купюры списываются с баланса банкомата.
* Допустимые номиналы: 50₽, 100₽, 500₽, 1000₽, 5000₽.
*
* Другие валюты и номиналы должны легко добавляться разработчиками в будущем.
* Многопоточные сценарии могут быть добавлены позже (например резервирование).
*/
class ATM {
// место для функции снятия
}
```

Усложнения

Поддержка других валют с другими номиналами, например EUR 500, 100, 20
Добавляем операцию "зарезервируй деньги под выдачу по QR-коду”. Запрос на резервирование приходит с сервера. Тут добавляется небольшая многопоточка, и нужно её аккуратно обработать.


Solution:
```java
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

```