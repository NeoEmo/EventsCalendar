package hexlet.pet.calendar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CalendarTest {
    private Calendar calendar;
    private Path tempFile;

    @BeforeEach
    public void setup() throws IOException {
        tempFile = Files.createTempFile("Calendar", ".json");
        calendar = new Calendar(tempFile.toString());
    }

    @AfterEach
    public void teardown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testAddEvent() throws IOException {
        var name = "my test name";
        var date =  LocalDateTime.now();

        assertTrue(calendar.add(name, date));
        assertTrue(calendar.clear());
    }

    @Test
    public void testRemoveBy() throws IOException {
        var name = "my test name";
        var date =  LocalDateTime.now();

        var name2 = "my test name2";
        var date2 =  LocalDateTime.now().minusDays(1);

        calendar.add(name, date);
        calendar.add(name2, date2);

        String id = calendar.getIdByName(name).orElseThrow();
        assertEquals(Optional.empty(), calendar.getIdByName(name + name2));
        assertTrue(calendar.removeById(id));
        assertFalse(calendar.removeById(id));

        assertTrue(calendar.removeByName(name2));
        assertFalse(calendar.removeByName(name2));
    }

    @Test
    public void getPastUpcomingTest() throws IOException {
        var name = "my past test name";
        var date =  LocalDateTime.now().minusMonths(1);
        var name2 = "my upcoming test name";
        var date2 =  LocalDateTime.now().plusMonths(1);
        calendar.add(name, date);
        calendar.add(name2, date2);

        String id = calendar.getIdByName(name).orElseThrow();
        String id2 = calendar.getIdByName(name2).orElseThrow();

        String toString = "[" + name + " (ID: " + id + ") - " + date + "]";
        String toString2 = "[" + name2 + " (ID: " + id2 + ") - " + date2 + "]";

        assertEquals(toString, calendar.getPast(1).toString());
        assertEquals(toString2, calendar.getUpcoming(1).toString());
    }
}
