package hexlet.pet.calendar;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {

    @Test
    public void testEvent() {
        var testName = "this is a test name";
        var date = LocalDateTime.now();
        Event event = new Event(testName, date);
        assertEquals(testName, event.getName());
        assertEquals(date, event.getDate());

        var id = event.getId();
        var toString = testName + " (ID: " + id + ") - " + date;
        assertEquals(toString, event.toString());
    }

    @Test
    public void testEvent2() {
        String name = null;
        LocalDateTime date = null;
        Event event = new Event(name, date);
        assertEquals(name, event.getName());
        assertEquals(date, event.getDate());

        var id = event.getId();
        var toString = name + " (ID: " + id + ") - " + date;
        assertEquals(toString, event.toString());
    }

    @Test
    public void testEvent3() {
        String name = "";
        LocalDateTime date = null;
        Event event = new Event(name, date);
        assertEquals(name, event.getName());
        assertEquals(date, event.getDate());

        var id = event.getId();
        var toString = name + " (ID: " + id + ") - " + date;
        assertEquals(toString, event.toString());
    }

    @Test
    public void testEvent4() {
        var name = "this is a test name";
        var date = LocalDateTime.now();
        Event event = new Event();

        event.setName(name);
        event.setDate(date);
        assertEquals(name, event.getName());
        assertEquals(date, event.getDate());

        var id = "my personal ID";
        event.setId(id);
        var toString = name + " (ID: " + id + ") - " + date;
        assertEquals(toString, event.toString());
    }
}
