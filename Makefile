#Объяснение команд и аргументов:
# add/win-add - создание события (по-умолчанию события будут создаваться в event.json)
# clear/win-clear - очистка файла с событиями (по-умолчанию в event.json)
# remove/win-remove - удаление события по ID (по-умолчанию в event.json)
# removeByName/win-removeByName - удаление события по имени (по-умолчанию в event.json)
# upcoming/win-upcoming - показать 3 события, которые скоро будут  (по-умолчанию в event.json)
# past/win-past - показать 3 предыдущих события (по-умолчанию в event.json)
# editName - отредактировать имя события по ID (по-умолчанию в event.json)
# editDate - отредактировать дату события по ID (по-умолчанию в event.json)
# help/win-help - отобразить справку
# test/ win-test - полностью самостоятельно вводите команды (полезно для отладки)

# АРГУМЕНТЫ:
# NAME="Имя события"
# REMOVE_NAME="Имя события, которое вы хотите удалить"
# EDIT_NAME="Новое имя события"
# EDIT_DATE= Новая дата события в виде YYYY-MM-DD (ГОД-МЕСЯЦ-ЧИСЛО)
# DATE= дата события в виде YYYY-MM-DD (ГОД-МЕСЯЦ-ЧИСЛО)
# ID= ID события, который вы хотите удалить
# SHOW= количество значений последующих событий, которые вы хотите отобразить (по-умолчанию 3)
# PAST= количество предыдущих событий, которые вы хотите отобразить (по-умолчанию 3)

# ЭКСПЕРИМЕНТАЛЬНЫЙ АРГУМЕНТЫ:
# OPTIONAL= Он позволяет сделать программу более гибкой к настройке, но если вы рядовой юзер, то лучше
# не использовать, т.к. есть шанс что вы что-то можете сломать. Как он работает:
# 1) Вам например хочется создать события не в event.json, а например в 123.json.
# для этого вам потребуется ввести:
# make add/make win-add NAME="*ИмяСобытия*" DATE=*Дата* OPTIONAL="-f 123.json" и всё.
# по сути аргумент OPTIONAL нужен только для того, чтобы взаимодействовать с недефолтным названием json`а.
# но благодаря этому, например, можно разделить условно на birthday.json и rockFestivalEvents.json
# и смотреть, редактировать, править их по-отдельности.
# Ниже представлены команды для linux и windows:
#
# AUTO_UPDATE= Необязательный аргумент, который добавляет функцию автообновления события на год
# (Например, если добавить 2025-12-25 с этим флагом, а сейчас 2026, то он сразу напишет 2026-12-25. Или другой
# пример, дни рождения, они происходят раз в год, т.е. вы добавили чей-то день рождения, отпраздновали его,
# а программа сама обновило в json дату на следующий год (Но, т.к. программа сейчас нигде не крутится,
# то всё завязано на обновлении json файла какое-либо)).
# ЧТО НУЖНО ПЕРЕДАВАТЬ В НЕГО: всё что угодно, т.к. за его логику отвечает boolean значение.
# Но я бы рекомендовал просто число 1 передавать, хотя подойдёт всё.
#
# ЧТО ТОЧНО НЕ ПЕРЕДАСТ ЕГО ЗНАЧЕНИЕ:
# AUTO_UPDATE=
# AUTO_UPDATE=*пробел*
# AUTO_UPDATE=" "
# AUTO_UPDATE
# ОН ТАК НЕ РАБОТАЕТ!!!
# Лучше передайте AUTO_UPDATE=1

# CHAT_ID= Идентификатор чата для интеграции с телеграм ботом, в теории можно и без него отлично работать с программой
# Про то, как работать с ботом - инициализация чата написана в helpIntegration, как в самом чате есть /help с командами

#for linux

.PHONY: add clear remove removeByName upcoming past help editName editDate
.PHONY: win-add win-clear win-remove win-removeByName win-upcoming win-past win-help win-editName win-editDate win-test

add:
	./build/install/EventsCalendar/bin/EventsCalendar -a -n "$(NAME)" -d "$(DATE)" $(if $(AUTO_UPDATE),-au,) $(OPTIONAL)

clear:
	./build/install/EventsCalendar/bin/EventsCalendar -c $(OPTIONAL)

remove:
	./build/install/EventsCalendar/bin/EventsCalendar -r "$(ID)" $(OPTIONAL)

removeByName:
	./build/install/EventsCalendar/bin/EventsCalendar -rn "$(REMOVE_NAME)" $(OPTIONAL)

upcoming:
	./build/install/EventsCalendar/bin/EventsCalendar -s $(or $(SHOW), 3) $(OPTIONAL)

past:
	./build/install/EventsCalendar/bin/EventsCalendar -p $(or $(PAST), 3) $(OPTIONAL)

editName:
	./build/install/EventsCalendar/bin/EventsCalendar -e "$(ID)" -en "$(EDIT_NAME)" $(OPTIONAL)

editDate:
	./build/install/EventsCalendar/bin/EventsCalendar -e "$(ID)" -ed "$(EDIT_DATE)" $(OPTIONAL)

integrationHelp:
	./build/install/EventsCalendar/bin/EventsCalendar -t

integration:
	./build/install/EventsCalendar/bin/EventsCalendar -t -i "$(CHAT_ID)"

help:
	./build/install/EventsCalendar/bin/EventsCalendar -h

test:
	./build/install/EventsCalendar/bin/EventsCalendar $(OPTIONAL)

#for windows

win-add:
	.\build\install\EventsCalendar\bin\EventsCalendar -a -n "$(NAME)" -d "$(DATE)" $(if $(AUTO_UPDATE),-au,) $(OPTIONAL)

win-clear:
	.\build\install\EventsCalendar\bin\EventsCalendar -c $(OPTIONAL)

win-remove:
	.\build\install\EventsCalendar\bin\EventsCalendar -r "$(ID)" $(OPTIONAL)

win-removeByName:
	.\build\install\EventsCalendar\bin\EventsCalendar -rn "$(REMOVE_NAME)" $(OPTIONAL)

win-upcoming:
	.\build\install\EventsCalendar\bin\EventsCalendar -s $(or $(SHOW), 3) $(OPTIONAL)

win-past:
	.\build\install\EventsCalendar\bin\EventsCalendar -p $(or $(PAST), 3) $(OPTIONAL)

win-editName:
	.\build\install\EventsCalendar\bin\EventsCalendar -e "$(ID)" -en "$(EDIT_NAME)" $(OPTIONAL)

win-editDate:

	.\build\install\EventsCalendar\bin\EventsCalendar -e "$(ID)" -ed "$(EDIT_DATE)" $(OPTIONAL)

win-integrationHelp:
	.\build\install\EventsCalendar\bin\EventsCalendar -t

win-integration:
	.\build\install\EventsCalendar\bin\EventsCalendar -t -i "$(CHAT_ID)"

win-help:
	.\build\install\EventsCalendar\bin\EventsCalendar -h

win-test:
	.\build\install\EventsCalendar\bin\EventsCalendar $(OPTIONAL)