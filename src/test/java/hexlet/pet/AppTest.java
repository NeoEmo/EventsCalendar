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
import java.time.LocalDateTime;
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
        assertEquals(0, exitCode);

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
    public void testAddEvent3() {
        String name3 = null;
        String[] args3 = {"-a", "-n", name3, "-d", null, "-f", filePath};
        int exitCode3 = new CommandLine(new App()).execute(args3);
        assertEquals(1, exitCode3);
    }

    @Test
    public void testClear() throws IOException {
        var name = "my test upcoming event";
        var date = LocalDate.now().plusMonths(1);
        String[] args = {"-a", "-n", name, "-d", date.toString(), "-f", filePath};

        int exitCode = new CommandLine(new App()).execute(args);
        assertEquals(0, exitCode);

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
    public void testRemoveById() throws IOException {
        var name = "my test upcoming event";
        var date = LocalDate.now().plusMonths(1);
        String[] args = {"-a", "-n", name, "-d", date.toString(), "-f", filePath};
        int exitCode = new CommandLine(new App()).execute(args);
        assertEquals(0, exitCode);

        Calendar calendar = new Calendar(filePath);
        List<Event> upcomingEvents = calendar.getUpcoming(5);
        assertEquals(1, upcomingEvents.size());

        var id  = upcomingEvents.get(0).getId();
        String[] args3 = {"-r", id, "-f", filePath};
        int exitCode3 = new CommandLine(new App()).execute(args3);
        assertEquals(0, exitCode3);
        upcomingEvents = calendar.getUpcoming(5);
        assertEquals(0, upcomingEvents.size());

        upcomingEvents = calendar.getUpcoming(5);
        assertEquals(0, upcomingEvents.size());
    }

    @Test
    public void testRemoveByName() throws IOException {
        var name2 = "my test past event";
        var date2 = LocalDate.now().minusMonths(1);
        String[] args2 = {"-a", "-n", name2, "-d", date2.toString(), "-f", filePath};
        int exitCode2 = new CommandLine(new App()).execute(args2);
        assertEquals(0, exitCode2);

        Calendar calendar = new Calendar(filePath);
        List<Event> pastEvents = calendar.getPast(5);
        assertEquals(1, pastEvents.size());

        String[] args4 = {"-rn", name2, "-f", filePath};
        int exitCode4 = new CommandLine(new App()).execute(args4);
        assertEquals(0, exitCode4);

        pastEvents = calendar.getPast(5);
        assertEquals(0, pastEvents.size());

        String removeName = "I don't exist";
        String[] args = {"-rn", removeName, "-f", filePath};
    }

    @Test
    public void testShow() throws IOException {
        String[] names = {"my test upcoming event", "my test upcoming event2", "my test upcoming event3"};
        LocalDate[] dates = {LocalDate.now().plusMonths(1), LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(3)};

        for (int i = 0; i < 3; i++) {
            String[] args = {"-a", "-n", names[i], "-d", dates[i].toString(), "-f", filePath};
            int exitCode = new CommandLine(new App()).execute(args);
            assertEquals(0, exitCode);
        }

        Calendar calendar = new Calendar(filePath);
        List<Event> events = calendar.getUpcoming(5);
        assertEquals(3, events.size());

        String[] args2 = {"-s", "3", "-f", filePath};
        int exitCode2 = new CommandLine(new App()).execute(args2);
        assertEquals(0, exitCode2);
    }

    @Test
    public void testPast() throws IOException {
        String[] names = {"my test past event", "my test past event2", "my test past event3"};
        LocalDate[] dates = {LocalDate.now().minusMonths(1), LocalDate.now().minusMonths(2),
                LocalDate.now().minusMonths(3)};

        for (int i = 0; i < 3; i++) {
            String[] args = {"-a", "-n", names[i], "-d", dates[i].toString(), "-f", filePath};
            int exitCode = new CommandLine(new App()).execute(args);
            assertEquals(0, exitCode);
        }

        Calendar calendar = new Calendar(filePath);
        List<Event> pastEvents = calendar.getPast(5);
        assertEquals(3, pastEvents.size());

        String[] args2 = {"-p", "3", "-f", filePath};
        int exitCode2 = new CommandLine(new App()).execute(args2);
        assertEquals(0, exitCode2);
    }

    @Test
    public void testEditEvent() throws IOException {
        var name = "my test event";
        var date = LocalDate.now().minusMonths(1);

        var name2 = "my test edit event";
        var date2 = LocalDate.now().minusMonths(2);

        String[] args = {"-a", "-n", name, "-d", date.toString(), "-f", filePath};
        int exitCode = new CommandLine(new App()).execute(args);
        assertEquals(0, exitCode);

        Calendar calendar = new Calendar(filePath);
        List<Event> events = calendar.getPast(5);
        String id = events.get(0).getId();
        assertEquals(1, events.size());

        String[] args2 = {"-e", id, "-en", name2, "-f", filePath};
        int exitCode2 = new CommandLine(new App()).execute(args2);
        assertEquals(0, exitCode2);

        events = calendar.getPast(5);
        assertEquals(1, events.size());
        assertEquals(name2, events.get(0).getName());

        String[] args3 = {"-e", id, "-ed", date2.toString(), "-f", filePath};
        int exitCode3 = new CommandLine(new App()).execute(args3);
        assertEquals(0, exitCode3);

        events = calendar.getPast(5);
        var dateTime = date2.atStartOfDay();
        LocalDateTime dateTimeEvent = events.get(0).getDate();
        assertEquals(1, events.size());
        assertEquals(name2, events.get(0).getName());
        assertEquals(dateTime.toString(), dateTimeEvent.toString());
    }

    @Test
    void testTelegramIntegrationPrintsHelp() {
        String[] args = {"-t"};
        int exitcode = new CommandLine(new App()).execute(args);
        assertEquals(0, exitcode);
    }

//     Эти два теста на данный момент не рабочие, но они добавлены, мб потом перепишу App
//    @Test
//    public void testRemoveByName2() throws IOException {
//        App app = new App();
//        String removeName = "I don't exist";
//        String[] args = {"-rn", removeName , "-f", filePath};
//        new CommandLine(new App()).execute(args);
//
//        assertThrows(Exception.class, app::run);
//    }
//
//    @Test
//    public void testRemoveById2() throws IOException {
//        App app = new App();
//        String removeId = "I don't exist";
//        String[] args = {"-r", removeId, "-f", filePath};
//        new CommandLine(new App()).execute(args);
//
//        assertThrows(Exception.class, app::run);
//    }
}
