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

# АРГУМЕНТЫ:
# NAME="Имя события"
# REMOVE_NAME="Имя события, которое вы хотите удалить"
# EDIT_NAME="Новое имя события"
# EDIT_DATE= Новая дата события в виде YYYY-MM-DD (ГОД-МЕСЯЦ-ЧИСЛО)
# DATE= дата события в виде YYYY-MM-DD (ГОД-МЕСЯЦ-ЧИСЛО)
# ID= ID события, который вы хотите удалить
# SHOW= количество значений последующих событий, которые вы хотите отобразить (по-умолчанию 3)
# PAST= количество предыдущих событий, которые вы хотите отобразить (по-умолчанию 3)

# ЭКСПЕРИМЕНТАЛЬНЫЙ АРГУМЕНТ:
# OPTIONAL= Он позволяет сделать программу более гибкой к настройке, но если вы рядовой юзер, то лучше
# не использовать, т.к. есть шанс что вы что-то можете сломать. Как он работает:
# 1) Вам например хочется создать события не в event.json, а например в 123.json.
# для этого вам потребуется ввести:
# make add/make win-add NAME="*ИмяСобытия*" DATE=*Дата* OPTIONAL="-f 123.json" и всё.
# по сути аргумент OPTIONAL нужен только для того, чтобы взаимодействовать с недефолтным названием json`а.
# но благодаря этому, например, можно разделить условно на birthday.json и rockFestivalEvents.json
# и смотреть, редактировать, править их по-отдельности.
# Ниже представлены команды для linux и windows:

#for linux

.PHONY: add clear remove removeByName upcoming past help
.PHONY: win-add win-clear win-remove win-removeByName win-upcoming win-past win-help

add:
	./build/install/EventsCalendar/bin/EventsCalendar -a -n "$(NAME)" -d "$(DATE)" $(OPTIONAL)

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

help:
	./build/install/EventsCalendar/bin/EventsCalendar -h

#for windows

win-add:
	.\build\install\EventsCalendar\bin\EventsCalendar -a -n "$(NAME)" -d "$(DATE)" $(OPTIONAL)

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

editName:
	.\build\install\EventsCalendar\bin\EventsCalendar -e "$(ID)" -en "$(EDIT_NAME)" $(OPTIONAL)

editDate:

	.\build\install\EventsCalendar\bin\EventsCalendar -e "$(ID)" -ed "$(EDIT_DATE)" $(OPTIONAL)

win-help:
	.\build\instaldl\EventsCalendar\bin\EventsCalendar -h