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
}
