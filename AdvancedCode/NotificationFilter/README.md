Компания предоставляет сервис массовой рассылки уведомлений для других бизнесов.
К вам обратился product owner с задачей создать систему фильтрации уведомлений с учетом предпочтений пользователей.

## Определения

Уведомление:
- id уведомления
- тип уведомления (EMAIL, SMS, PUSH)
- получатель (id пользователя)
- текст сообщения

Получатель может иметь настройки предпочтений:
- разрешенные каналы уведомлений (список типов)
- заблокированные отправители (список id отправителей)

История отправленных уведомлений:
- список уведомлений, отправленных пользователю

## Важно
Настройки пользователей и история уведомлений предоставляются другими компонентами системы.
Вам необходимо спроектировать контракты для получения этих данных.
Реализацию хранения делать не нужно.

## Задача
Написать систему фильтрации уведомлений, которая:
- на вход получает список уведомлений для фильтрации и id отправителя
- исключает уведомления, не соответствующие предпочтениям получателя
- предотвращает повторную отправку: если уведомление с таким же id уже было отправлено конкретному пользователю за последние 24 часа, оно не должно быть отправлено снова (защита от дублирования)
- возвращает отфильтрованный список уведомлений, готовых к отправке.

Отправка уведомлений не входит в вашу задачу - другая команда займется отправкой отфильтрованного списка.
Ваша задача - только фильтрация.



```java
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

// Тип уведомления
enum NotificationType {
    EMAIL, SMS, PUSH
}

// Модель уведомления
record Notification(String id, NotificationType type, String recipientId, String message) {}

// Настройки пользователя
record UserPreferences(Set<NotificationType> allowedChannels, Set<String> blockedSenders) {}

// Контракт для получения настроек пользователя
interface UserPreferencesService {
    UserPreferences getPreferences(String userId);
}

// Контракт для получения истории уведомлений
interface NotificationHistoryService {
    // Возвращает список уведомлений, отправленных пользователю за последние 24 часа
    List<Notification> getSentNotifications(String userId, Instant since);
}

class NotificationFilterService {

    private final UserPreferencesService preferencesService;
    private final NotificationHistoryService historyService;

    public NotificationFilterService(UserPreferencesService preferencesService,
                                     NotificationHistoryService historyService) {
        this.preferencesService = preferencesService;
        this.historyService = historyService;
    }

    public List<Notification> filterNotifications(List<Notification> notifications, String senderId) {
        Instant since = Instant.now().minus(24, ChronoUnit.HOURS);

        // Группируем уведомления по получателю
        Map<String, List<Notification>> notificationsByUser = notifications.stream()
                .collect(Collectors.groupingBy(Notification::recipientId));

        List<Notification> result = new ArrayList<>();

        for (var entry : notificationsByUser.entrySet()) {
            String userId = entry.getKey();
            List<Notification> userNotifications = entry.getValue();

            UserPreferences prefs = preferencesService.getPreferences(userId);

            // Если отправитель заблокирован, пропускаем все уведомления
            if (prefs.blockedSenders().contains(senderId)) continue;

            // Получаем историю за последние 24 часа
            Set<String> sentNotificationIds = historyService.getSentNotifications(userId, since).stream()
                    .map(Notification::id)
                    .collect(Collectors.toSet());

            // Фильтруем по каналу и дубликатам
            userNotifications.stream()
                    .filter(n -> prefs.allowedChannels().contains(n.type()))
                    .filter(n -> !sentNotificationIds.contains(n.id()))
                    .forEach(result::add);
        }

        return result;
    }
}
```