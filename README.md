# WG Admin Panel

Java-приложение (Spring Boot) + JavaScript UI для управления туннелями через удалённый `easy-wireguard` API.

## Возможности
- Создание туннелей с датой/временем окончания активности.
- При создании указывается префикс из БД, который добавляется в начало имени туннеля.
- `id` туннеля в `easy-wireguard` создаётся автоматически удалённым API.
- Редактирование даты окончания активности.
- Ручное включение/выключение туннеля.
- Автоматическая деактивация туннелей после `expiresAt` (scheduler).
- Хранение состояния в PostgreSQL.
- Тёмная тема UI на Bootstrap 5.

## Подключение к PostgreSQL
Приложение настроено на уже запущенный PostgreSQL в соседнем контейнере:
- Host: `192.168.51.171`
- Port: `5432`
- User: `wg_admin_user`
- Password: `HgfPsxccWalOr32`

Эти значения установлены как дефолт в `application.yml` и могут быть переопределены env-переменными `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`.

## Структура таблиц
### `prefix`
| Колонка | Тип | Ограничения |
|---|---|---|
| `prefix` | `varchar(40)` | `PRIMARY KEY`, `NOT NULL` |

### `tunnels`
| Колонка | Тип | Ограничения |
|---|---|---|
| `id` | `bigserial` | `PRIMARY KEY` |
| `name` | `varchar(255)` | `NOT NULL` |
| `external_tunnel_id` | `varchar(255)` | `NOT NULL`, `UNIQUE` |
| `active` | `boolean` | `NOT NULL` |
| `expires_at` | `timestamp with time zone` | `NOT NULL` |

## SQL для создания таблиц
```sql
CREATE TABLE IF NOT EXISTS prefix (
    prefix varchar(40) PRIMARY KEY NOT NULL
);

CREATE TABLE IF NOT EXISTS tunnels (
    id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL,
    external_tunnel_id varchar(255) NOT NULL UNIQUE,
    active boolean NOT NULL,
    expires_at timestamp with time zone NOT NULL
);

INSERT INTO prefix(prefix)
VALUES ('[-]'), ('(-)'), ('{-}')
ON CONFLICT (prefix) DO NOTHING;
```

## Запуск в LXC контейнере
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
