# javalrs

Лабораторные работы по Java.

| Модуль | Описание |
|---|---|
| `lr1` (`species-grouper`) | ЛР1 — группировка персонажей из CSV по видам (`TreeMap<String, List<String>>`) |
| `lr2` (`car-repository`) | ЛР2 — CRUD-репозиторий для сущности Car (JDBC, HikariCP, Flyway, PostgreSQL) |
| `lr3` | ЛР3 — пока пусто |

Сборка: Maven, Java 25.

## Запуск ЛР1

Входные данные - `src/main/resources/characters.csv`, читаются из classpath. Результат пишется в `species.txt` в текущей папке.

## Запуск ЛР2

1. Поднять PostgreSQL:
   ```bash
   cd lr2
   docker compose up -d
   ```
2. Запустить `ru.bivis.car.Main` из модуля `lr2`.

Параметры подключения - `lr2/src/main/resources/application.properties`
(можно переопределить переменными окружения `DB_URL`, `DB_USER`, `DB_PASSWORD`, `DB_SCHEMA`).
Приложение работает под отдельным пользователем `car_user` в схеме `car_schema` —
их создаёт скрипт `lr2/docker/init/01-create-app-user.sh` при первом запуске контейнера.
Таблица `car` создаётся миграцией Flyway `V1__create_car_table.sql` при первом запуске.
