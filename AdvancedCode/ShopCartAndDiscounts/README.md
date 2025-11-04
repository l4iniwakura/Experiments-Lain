```java
/*
Корзина - список товаров покупателя.

Товар:
- id
- цена
- итоговая стоимость c учетом скидки

Скидка. Для покупателя может быть задан % скидки (целое число).

## Задача

Написать часть системы, которая:

- на вход получает id покупателя и корзину
- вычисляет и применяет скидки
- возвращает корзину, с применёнными скидками. Скидка учитывается в стоимости покупки
*/
```


```java
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Purchase {
    private final String productId;
    private final BigDecimal price;
    private BigDecimal discountedPrice;

    public Purchase(String productId, BigDecimal price) {
        this.productId = productId;
        this.price = price.setScale(2, RoundingMode.HALF_EVEN);
        this.discountedPrice = this.price;
    }

    public String getProductId() {
        return productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void applyDiscount(int discountPercent) {
        BigDecimal discount = price
                .multiply(BigDecimal.valueOf(discountPercent))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
        this.discountedPrice = price.subtract(discount).setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "productId='" + productId + '\'' +
                ", price=" + price +
                ", discountedPrice=" + discountedPrice +
                '}';
    }
}

class LoyaltyService {
    private final Map<String, Integer> customerDiscounts;

    public LoyaltyService(Map<String, Integer> customerDiscounts) {
        this.customerDiscounts = customerDiscounts;
    }

    public List<Purchase> applyDiscounts(String customerId, List<Purchase> cart) {
        int discountPercent = customerDiscounts.getOrDefault(customerId, 0);
        return cart.stream()
                .peek(p -> p.applyDiscount(discountPercent))
                .collect(Collectors.toList());
    }
}

// Пример использования
public class Main {
    public static void main(String[] args) {
        List<Purchase> cart = List.of(
                new Purchase("A123", BigDecimal.valueOf(100)),
                new Purchase("B456", BigDecimal.valueOf(200.555))
        );

        Map<String, Integer> discounts = Map.of(
                "customer1", 10,
                "customer2", 20
        );

        LoyaltyService service = new LoyaltyService(discounts);
        List<Purchase> discountedCart = service.applyDiscounts("customer1", cart);

        discountedCart.forEach(System.out::println);
    }
}

```