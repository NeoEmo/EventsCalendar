# 📅 EventsCalendar 📅

# ⚠️ Бот в Телеграмме временно не работает и дорабатывается ⚠️ <!---->

[![SonarQube](https://github.com/NeoEmo/EventsCalendar/actions/workflows/build.yml/badge.svg)](https://github.com/NeoEmo/EventsCalendar/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=NeoEmo_EventsCalendar&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=NeoEmo_EventsCalendar)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=NeoEmo_EventsCalendar&metric=bugs)](https://sonarcloud.io/summary/new_code?id=NeoEmo_EventsCalendar)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=NeoEmo_EventsCalendar&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=NeoEmo_EventsCalendar)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=NeoEmo_EventsCalendar&metric=coverage)](https://sonarcloud.io/summary/new_code?id=NeoEmo_EventsCalendar)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=NeoEmo_EventsCalendar&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=NeoEmo_EventsCalendar)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=NeoEmo_EventsCalendar&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=NeoEmo_EventsCalendar)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=NeoEmo_EventsCalendar&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=NeoEmo_EventsCalendar)


CLI‑утилита для управления календарём событий.  
Состоит из трёх основных классов:
- `Event` — модель события
- `Calendar` — работа с хранилищем (JSON)
- `App` — интерфейс командной строки

---

## 👾 Основные возможности 👾

### Добавление событий
- `-a, --add` — добавить событие (требует `-n` и `-d`)
- `-n, --name` — название события
- `-d, --date` — дата в формате `yyyy-MM-dd`
- `-au, --autoUpdate` — включить ежегодное автообновление даты

### Просмотр событий
- `-s, --show N` — показать ближайшие N событий
- `-p, --past N` — показать N последних прошедших событий

### Удаление
- `-r, --remove <ID>` — удалить событие по ID
- `-rn, --removeByName <название>` — удалить все события с указанным названием

### Редактирование (только по ID)
- `-e, --edit <ID>` — указать ID события для редактирования
- `-ed, --editDate <дата>` — изменить дату события
- `-en, --editName <название>` — изменить название события

### Управление хранилищем
- `-c, --clear` — удалить все события
- `-f, --file <путь>` — указать путь к JSON‑файлу (по умолчанию `event.json`)

### Telegram
- `-t, --telegram` — открыть чат с ботом в браузере  
  (бот позволяет загружать JSON‑файлы и получать уведомления)

---

## 🧑‍🏫 Примеры использования 🧑‍🏫

```bash
# Добавить событие без автообновления
EventsCalendar -a -n "Встреча" -d 2026-12-31

# Добавить событие с ежегодным автообновлением
EventsCalendar -a -n "День рождения" -d 2000-01-01 -au

# Показать 5 ближайших событий
EventsCalendar -s 5

# Показать 3 последних прошедших события
EventsCalendar -p 3

# Удалить событие по ID
EventsCalendar -r a1b2c3d4-...

# Удалить все события с названием "Встреча"
EventsCalendar -rn "Встреча"

# Изменить дату события
EventsCalendar -e a1b2c3d4-... -ed 2027-01-01

# Изменить название события
EventsCalendar -e a1b2c3d4-... -en "Новое название"

# Очистить календарь
EventsCalendar -c

# Использовать другой файл для хранения
EventsCalendar -a -n "Проект" -d 2026-07-01 -f work.json

# 🚨 Примечание:
связка -e -ed -en в одной команде не поддерживается — будет выполнено только первое действие (дата или название).