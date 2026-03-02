# WG Admin Panel

Java-приложение (Spring Boot) + JavaScript UI для управления туннелями через удалённый `easy-wireguard` API.

## Возможности
- Создание туннелей с датой/временем окончания активности.
- При создании указывается префикс из БД, который добавляется в начало имени туннеля.
- `id` туннеля в `easy-wireguard` создаётся автоматически удалённым API.
- Редактирование даты окончания активности.
- Ручное включение/выключение туннеля.
- Автоматическая деактивация туннелей после `expiresAt` (scheduler).
- Хранение состояния в PostgreSQL (отдельный контейнер в `docker-compose`).
- Тёмная тема UI на Bootstrap 5.

## База данных
Создаётся таблица `prefix` с одной колонкой `prefix` типа `varchar(40)`.

По умолчанию при первом запуске добавляются значения:
- `[-]`
- `(-)`
- `{-}`

## Запуск в LXC контейнере
1. Поднимите PostgreSQL отдельно:
```bash
docker compose up -d
```
2. Запустите приложение напрямую:
```bash
mvn spring-boot:run
```

После запуска:
- UI: `http://localhost:8080`
- API: `http://localhost:8080/api/admin/tunnels`
- Prefixes API: `http://localhost:8080/api/admin/prefixes`

## Конфигурация
Через env-переменные:
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
- `EW_API_BASE_URL`, `EW_API_TOKEN`
- `APP_PORT`
- `tunnels.deactivation-check-ms` (через Spring config)
