package hexlet.pet;

import hexlet.pet.calendar.Calendar;
import hexlet.pet.calendar.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventCalendarTest {

    private Calendar calendar;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("calendar", ".json");
        calendar = new Calendar(tempFile.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    // ========== 1. Пустой календарь ==========
    @Test
    void emptyCalendarReturnsEmptyLists() throws IOException {
        assertTrue(calendar.getPast(10).isEmpty());
        assertTrue(calendar.getUpcoming(10).isEmpty());
    }

    // ========== 2. Добавление одного события ==========
    @Test
    void addSinglePastEvent() throws IOException {
        LocalDateTime date = LocalDateTime.of(2000, 1, 1, 0, 0);
        calendar.add("New Year", date);
        List<Event> past = calendar.getPast(5);
        assertEquals(1, past.size());
        assertEquals("New Year", past.get(0).getName());
        assertEquals(date, past.get(0).getDate());
    }

    @Test
    void addSingleUpcomingEvent() throws IOException {
        LocalDateTime date = LocalDateTime.now().plusDays(5);
        calendar.add("Meeting", date);
        List<Event> upcoming = calendar.getUpcoming(5);
        assertEquals(1, upcoming.size());
        assertEquals("Meeting", upcoming.get(0).getName());
        assertEquals(date, upcoming.get(0).getDate());
    }

    // ========== 3. Порядок сортировки ==========
    @Test
    void pastEventsOrderedNewestFirst() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        calendar.add("old", now.minusYears(2));
        calendar.add("middle", now.minusYears(1));
        calendar.add("recent", now.minusDays(1));
        List<Event> past = calendar.getPast(10);
        assertEquals(3, past.size());
        assertEquals("recent", past.get(0).getName());
        assertEquals("middle", past.get(1).getName());
        assertEquals("old", past.get(2).getName());
    }

    @Test
    void upcomingEventsOrderedSoonestFirst() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        calendar.add("far", now.plusMonths(2));
        calendar.add("soon", now.plusDays(1));
        calendar.add("mid", now.plusWeeks(1));
        List<Event> upcoming = calendar.getUpcoming(10);
        assertEquals(3, upcoming.size());
        assertEquals("soon", upcoming.get(0).getName());
        assertEquals("mid", upcoming.get(1).getName());
        assertEquals("far", upcoming.get(2).getName());
    }

    // ========== 4. Лимиты ==========
    @Test
    void limitPastEvents() throws IOException {
        for (int i = 1; i <= 5; i++) {
            calendar.add("event" + i, LocalDateTime.now().minusDays(i));
        }
        List<Event> past = calendar.getPast(2);
        assertEquals(2, past.size());
    }

    @Test
    void limitUpcomingEvents() throws IOException {
        for (int i = 1; i <= 5; i++) {
            calendar.add("event" + i, LocalDateTime.now().plusDays(i));
        }
        List<Event> upcoming = calendar.getUpcoming(3);
        assertEquals(3, upcoming.size());
    }

    @Test
    void zeroLimit() throws IOException {
        calendar.add("Test", LocalDateTime.now().plusDays(1));
        assertTrue(calendar.getUpcoming(0).isEmpty());
        assertTrue(calendar.getPast(0).isEmpty());
    }

    @Test
    void limitGreaterThanEventCount() throws IOException {
        calendar.add("One", LocalDateTime.now().plusDays(1));
        List<Event> upcoming = calendar.getUpcoming(100);
        assertEquals(1, upcoming.size());
    }

    // ========== 5. Удаление по ID ==========
    @Test
    void removeById() throws IOException {
        calendar.add("toRemove", LocalDateTime.now());
        Event event = calendar.getPast(10).get(0);
        assertTrue(calendar.removeById(event.getId()));
        assertTrue(calendar.getPast(10).isEmpty());
    }

    @Test
    void removeByIdNotFound() throws IOException {
        boolean removed = calendar.removeById("non-existent-id");
        assertFalse(removed);
    }

    // ========== 6. Удаление по имени ==========
    @Test
    void removeByName() throws IOException {
        calendar.add("unique", LocalDateTime.now().plusDays(1));
        assertTrue(calendar.removeByName("unique"));
        assertTrue(calendar.getUpcoming(10).isEmpty());
    }

    @Test
    void removeByNameNotFound() throws IOException {
        boolean removed = calendar.removeByName("not-there");
        assertFalse(removed);
    }

    @Test
    void removeByNameRemovesAllWithSameName() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        calendar.add("birthday", now);
        calendar.add("birthday", now.plusDays(1));
        calendar.add("other", now.plusDays(2));
        assertEquals(2, calendar.getUpcoming(10).size());
        calendar.removeByName("birthday");
        List<Event> remaining = calendar.getUpcoming(10);
        assertEquals(1, remaining.size());
        assertEquals("other", remaining.get(0).getName());
    }

    // ========== 7. Очистка ==========
    @Test
    void clearNonEmptyCalendar() throws IOException {
        calendar.add("test", LocalDateTime.now());
        assertFalse(calendar.getPast(10).isEmpty());
        calendar.clear();
        assertTrue(calendar.getPast(10).isEmpty());
    }

    @Test
    void clearEmptyCalendar() throws IOException {
        calendar.clear();
        assertTrue(calendar.getPast(10).isEmpty());
    }

    // ========== 8. Дубликаты имён ==========
    @Test
    void duplicateNamesAllowed() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        calendar.add("meeting", now.plusDays(1));
        calendar.add("meeting", now.plusDays(2));
        List<Event> upcoming = calendar.getUpcoming(10);
        assertEquals(2, upcoming.size());
        assertEquals("meeting", upcoming.get(0).getName());
        assertEquals("meeting", upcoming.get(1).getName());
    }

    // ========== 9. Обработка пустого файла (MismatchedInputException) ==========
    @Test
    void loadEventsWithEmptyFile() throws IOException {
        // Очищаем временный файл, делаем его пустым
        Files.writeString(tempFile, "");
        // Любой метод, вызывающий loadEvents, должен вернуть пустой список, а не упасть
        List<Event> events = calendar.getPast(10);
        assertTrue(events.isEmpty());
    }
}
