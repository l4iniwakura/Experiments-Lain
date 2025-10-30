```java
/**
 * Банкомат.
 * Взаимодействует с SDK, контракты для которого описаны ниже.
 * Необходимо реализовать запрос на выдачу определенной суммы (в рублях).
 * В случае, если нужную сумму выдать невозможно, отвечать отказом.
 * Допустимые номиналы: 50₽, 100₽, 500₽, 1000₽, 5000₽.
 */
class ATM {
    // место для кода
}
```


```java
/**
 * Интерфейс SDK может быть изменён/расширен по договорённости сторон, если это необходимо.
 */
interface Sdk {
  /**
   * Посчитать количество купюр определенного номинала в банкомате.
   * Эта операция занимает около 10 секунд, и шумная, её стоит вызывать как можно реже.
   *
   * @param banknote номинал купюры
   * @return количество купюр в банкомате
   */
  int countBanknotes(int banknote);

  /**
   * Переместить некоторое количество купюр одного номинала в лоток выдачи.
   *
   * @param banknote номинал купюры
   * @param count    количество купюр
   */
  void moveBanknoteToDispenser(int banknote, int count);

  /**
   * Открыть лоток выдачи.
   */
  void openDispenser();
}
```

```java
/**
* Пример реализации SDK, которую можно использовать в тестах.
* Реализацию можно и нужно менять.
*/
class StubSdk implements Sdk {
  @Override
  public int countBanknotes(int banknote) {
  return 0;
  }

  @Override
  public void moveBanknoteToDispenser(int banknote, int count) {
  System.out.printf("Перемещаю купюру %s в лоток выдачи, %s штук%n", banknote, count);
  }

  @Override
  public void openDispenser() {
  System.out.printf("Лоток выдачи открыт пользователю%n");
  }
}
```