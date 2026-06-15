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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventCalendarTest {

    private Calendar calendar;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("test", ".json");
        calendar = new Calendar(tempFile.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testEventCalendar() throws IOException {
        var name = "this is test event";
        var date = LocalDateTime.of(2000, 3, 12, 0, 0);

        calendar.add(name, date);
        List<Event> events = calendar.getPast(1);
        String expectedMessage = "this is test event (ID: " + events.get(0).getId() + ") - 2000-03-12T00:00";
        assertEquals(expectedMessage,  events.get(0).toString());
    }

    @Test
    public void testEventCalendar2() throws IOException {
        var name = "this is test event";
        var date = LocalDateTime.of(2050, 3, 12, 0, 0);


        calendar.add(name, date);
        List<Event> events = calendar.getUpcoming(1);
        String expectedMessage = "this is test event (ID: " + events.get(0).getId() + ") - 2050-03-12T00:00";
        assertEquals(expectedMessage,  events.get(0).toString());
    }

    @Test
    public void testEventCalendar3() throws IOException {
        var name = "this is test event";
        var date = LocalDateTime.of(2000, 3, 12, 0, 0);

        calendar.add(name, date);
        List<Event> events = calendar.getPast(1);
        String expectedMessage = "this is test event (ID: " + events.get(0).getId() + ") - 2000-03-12T00:00";
        assertEquals(expectedMessage,  events.get(0).toString());

        calendar.removeById(events.get(0).getId());
        List<Event> events2 = calendar.getPast(1);
        assertTrue(events2.isEmpty());
    }

    @Test
    public void testEventCalendar4() throws IOException {
        var name = "this is test event";
        var date = LocalDateTime.of(2000, 3, 12, 0, 0);

        calendar.add(name, date);
        List<Event> events = calendar.getPast(1);
        String expectedMessage = "this is test event (ID: " + events.get(0).getId() + ") - 2000-03-12T00:00";
        assertEquals(expectedMessage,  events.get(0).toString());

        calendar.removeByName(name);
        List<Event> events2 = calendar.getPast(1);
        assertTrue(events2.isEmpty());

    }

    @Test
    public void testEventCalendar5() throws IOException {
        calendar.add("one", LocalDateTime.of(2000, 3, 12, 0, 0));
        calendar.add("two", LocalDateTime.of(2004, 3, 12, 0, 0));
        calendar.add("three", LocalDateTime.of(2005, 3, 12, 0, 0));

        List<Event> events = calendar.getPast(3);
        String expectedMessage1 = events.get(0).toString();
        assertEquals(expectedMessage1, events.get(0).toString());

        String expectedMessage2 = events.get(1).toString();
        assertEquals(expectedMessage2, events.get(1).toString());

        String expectedMessage3 = events.get(2).toString();
        assertEquals(expectedMessage3, events.get(2).toString());

        calendar.clear();
        List<Event> events2 = calendar.getPast(3);
        assertTrue(events2.isEmpty());
    }
}
