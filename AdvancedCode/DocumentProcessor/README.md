message TDocument {
string Url = 1; // URL Документа, его уникальный идентификатор
uint64 PubDate = 2; // время заявляемой публикации документа
uint64 FetchTime = 3; // время получения данного обновления документа,
может рассматриваться как идентификатор версии. Пара (Url, FetchTime) уникальна.
string Text = 4; // текст документа
uint64 FirstFetchTime = 5; // изначально отсутствует, необходимо заполнить.
}

```java
class TProcessor {
    public TDocument Process(TDocument input);
}
```

- Документы могут поступать в произвольном порядке (не в том как они обновлялись), также возможно дублирование отдельных сообщений.
- Необходимо на выходе формировать такие же сообщения, но с исправленными отдельными полями по следующим правилам (всё нижеуказанное - для группы документов с совпадающим полем Url)
- Поле Text и FetchTime должны быть такими, какими были в документе с наибольшим FetchTime, полученным на данный момент.
- Поле PubDate должно быть таким, каким было у сообщения с наименьшим FetchTime
- Поле FirstFetchTime должно быть равно минимальному значению FetchTime
- Т. е. в каждый момент времени мы берем PubDate и FirstFetchTime от самой первой из полученных на данный момент версий (если отсортировать их по FetchTime), а Text - от самой последней версии.

Примерное решение

```java
import java.util.concurrent.ConcurrentHashMap;

class TDocument {
    public String Url;
    public long PubDate;
    public long FetchTime;
    public String Text;
    public long FirstFetchTime;
}

class TProcessor {
    // Храним состояние по каждому Url
    private final ConcurrentHashMap<String, State> states = new ConcurrentHashMap<>();

    public TDocument Process(TDocument input) {
        State s = states.compute(input.Url, (url, prev) -> {
            if (prev == null) return new State(input);

            // обновляем минимумы/максимумы
            if (input.FetchTime < prev.minFetchTime) {
                prev.minFetchTime = input.FetchTime;
                prev.pubDate = input.PubDate;
            }
            if (input.FetchTime > prev.maxFetchTime) {
                prev.maxFetchTime = input.FetchTime;
                prev.text = input.Text;
            }
            return prev;
        });

        // формируем итоговый документ
        TDocument result = new TDocument();
        result.Url = input.Url;
        result.PubDate = s.pubDate;
        result.FetchTime = s.maxFetchTime;
        result.Text = s.text;
        result.FirstFetchTime = s.minFetchTime;
        return result;
    }

    private static class State {
        long minFetchTime;
        long maxFetchTime;
        long pubDate;
        String text;

        State(TDocument doc) {
            this.minFetchTime = doc.FetchTime;
            this.maxFetchTime = doc.FetchTime;
            this.pubDate = doc.PubDate;
            this.text = doc.Text;
        }
    }
}

```