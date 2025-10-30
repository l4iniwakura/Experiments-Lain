Нам дана функция которая оценивает релевантность документа пользователю

```java
interface Scorer<Document, User> {
    double getScore(Document doc, User user);
}
```

Необходимо реализовать сервис, который может сохранять документ и получать топ К (limit) документов для пользователя по скору этой функции
```java
interface RecommenderService<Document, User> {
    List<Document> getTop(User user, int limit);

    void addDocument(Document document);
}
```