package hexlet.pet;

import hexlet.pet.calendar.Calendar;
import hexlet.pet.calendar.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    private Path tempFile;
    private String filePath;

    @BeforeEach
    public void setup() throws IOException {
        tempFile = Files.createTempFile("Calendar", ".json");
        filePath = tempFile.toString();
    }

    @AfterEach
    public void teardown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testAddEvent() throws IOException {
        var name = "my test upcoming event";
        var date = LocalDate.now().plusMonths(1);
        String[] args = {"-a", "-n", name, "-d", date.toString(), "-f", filePath};

        int exitCode = new CommandLine(new App()).execute(args);
        Assertions.assertEquals(0, exitCode);

        Calendar calendar = new Calendar(filePath);
        List<Event> events = calendar.getUpcoming(5);
        assertEquals(1, events.size());
        assertEquals(name, events.get(0).getName());
    }

    @Test
    public void testAddEvent2() throws IOException {
        var name2 = "my test past event";
        var date2 = LocalDate.now().minusMonths(1);
        String[] args2 = {"-a", "-n", name2, "-d", date2.toString(), "-f", filePath};

        int exitCode2 = new CommandLine(new App()).execute(args2);
        Assertions.assertEquals(0, exitCode2);

        Calendar calendar2 = new Calendar(filePath);
        List<Event> events2 = calendar2.getPast(5);
        assertEquals(1, events2.size());
        assertEquals(name2, events2.get(0).getName());
    }

    @Test
    public void testAddEvent3() throws IOException {
        String name3 = null;
        String[] args3 = {"-a", "-n", name3, "-d", null, "-f", filePath};
        int exitCode3 = new CommandLine(new App()).execute(args3);
        Assertions.assertEquals(1, exitCode3);
    }

    @Test
    public void testClear() throws IOException {
        var name = "my test upcoming event";
        var date = LocalDate.now().plusMonths(1);
        String[] args = {"-a", "-n", name, "-d", date.toString(), "-f", filePath};

        int exitCode = new CommandLine(new App()).execute(args);
        Assertions.assertEquals(0, exitCode);

        Calendar calendar = new Calendar(filePath);
        List<Event> events = calendar.getUpcoming(5);
        assertEquals(1, events.size());

        String[] args2 = {"-c", "-f", filePath};
        int exitCode2 = new CommandLine(new App()).execute(args2);
        Assertions.assertEquals(0, exitCode2);

        events = calendar.getUpcoming(3);
        assertEquals(0, events.size());
    }

    @Test
    public void testRemoveBy() throws IOException {
        var name = "my test upcoming event";
        var date = LocalDate.now().plusMonths(1);
        String[] args = {"-a", "-n", name, "-d", date.toString(), "-f", filePath};
        int exitCode = new CommandLine(new App()).execute(args);
        Assertions.assertEquals(0, exitCode);

        var name2 = "my test past event";
        var date2 = LocalDate.now().minusMonths(1);
        String[] args2 = {"-a", "-n", name2, "-d", date2.toString(), "-f", filePath};
        int exitCode2 = new CommandLine(new App()).execute(args2);
        Assertions.assertEquals(0, exitCode2);

        Calendar calendar = new Calendar(filePath);
        List<Event> upcomingEvents = calendar.getUpcoming(5);
        List<Event> pastEvents = calendar.getPast(5);
        assertEquals(1, upcomingEvents.size());
        assertEquals(1, pastEvents.size());

        var id  = upcomingEvents.get(0).getId();
        String[] args3 = {"-r", id, "-f", filePath};
        int exitCode3 = new CommandLine(new App()).execute(args3);
        Assertions.assertEquals(0, exitCode3);
        upcomingEvents = calendar.getUpcoming(5);
        assertEquals(0, upcomingEvents.size());

        String[] args4 = {"-rn", name2, "-f", filePath};
        int exitCode4 = new CommandLine(new App()).execute(args4);
        Assertions.assertEquals(0, exitCode4);
        upcomingEvents = calendar.getUpcoming(5);
        assertEquals(0, upcomingEvents.size());
    }
}
