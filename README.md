# javalrs

Лабораторные работы по Java.

| Модуль | Описание |
|---|---|
| `species-grouper` | ЛР1 — группировка персонажей из CSV по видам (`TreeMap<String, List<String>>`) |

Сборка: Maven, Java 25.

## Запуск ЛР1

В IDEA — конфигурация `App`. Из консоли — из папки `species-grouper`:

```
mvn compile
java -cp target/classes ru.bivis.App [входной.csv] [выходной.txt]
```

По умолчанию читается `characters.csv`, результат пишется в `species.txt`.
