package hexlet.pet;

import hexlet.pet.calendar.Calendar;
import hexlet.pet.calendar.Event;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Command(
        name = "EventCalendar",
        mixinStandardHelpOptions = true,
        version = "EventCalendar 0.5",
        description = "An events calendar where you can add events and see when the next one is"
)
public class App implements  Runnable {
    Logger logger =  Logger.getLogger(App.class.getName());
    Calendar calendar;

    @Option(names = {"-a", "--add"}, description = "add new event")
    private boolean add;

    @Option(names = {"-n", "--name"}, description = "Event name")
    private String name;

    @Option(names = {"-d", "--date"}, description = "Date (yyyy-MM-dd)")
    private LocalDate date;

    @Option(names = {"-s", "--show"}, description = "show near events (default: 3 upcoming ones)")
    private int show = 3;

    @Option(names = {"-p", "--past"}, description = "show past events (default: 3 past events)")
    private int past = 3;

    @Option(names = {"-f", "--file"}, description = "path to JSON`s (advanced option, if you need)",
            defaultValue = "event.json")
    private String filePath;

    @Option(names = {"-r", "--remove"}, description = "remove some event by ID")
    private String removeId;

    @Option(names = {"-rn", "--removeByName"}, description = "remove some event by name")
    private String removeName;

    @Option(names = {"-c", "--clear"}, description = "Remove all events")
    private boolean clear;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    private String mode() {
        String mode;

        if (add) {
            mode = "add";
        } else if (clear) {
            mode = "clear";
        } else if (removeId != null && !removeId.isBlank()) {
            mode = "removeId";
        } else if (removeName != null && !removeName.isBlank()) {
            mode = "removeName";
        } else {
            mode = "show";
        }
        return mode;
    }

    private void addEvent() throws IOException {
        calendar = new Calendar(filePath);

        if (name == null || date == null) {
            logger.warning("Error: --add requires --name and --date");
            System.exit(1);
        }
        LocalDateTime dateTime = date.atStartOfDay();
        calendar.add(name, dateTime);
        System.out.println("Event added: " + name + " on " + dateTime);
    }

    private void clearEvents() throws IOException {
        calendar = new Calendar(filePath);
        calendar.clear();
        logger.info("All events are removed.");
    }

    private void removeIdEvents() throws IOException {
        calendar = new Calendar(filePath);
        boolean removed = calendar.removeById(removeId);
        if (removed) {
            logger.info("Event with ID " + removeId + " removed.");
        } else  {
            logger.warning("Event with ID " + removeId + " not found.");
            System.exit(1);
        }
    }

    private void removeNameEvents() throws IOException {
        calendar = new Calendar(filePath);
        boolean removedName = calendar.removeByName(removeName);
        if (removedName) {
            logger.info("Event with name " + removeName + " removed.");
        } else {
            logger.warning("Event with name " + removeName + " not found.");
            System.exit(1);
        }
    }

    private void showEvents() throws IOException {
        calendar = new Calendar(filePath);
        List<Event> pastEvents = calendar.getPast(past);
        List<Event> upcomingEvents = calendar.getUpcoming(show);

        if (!pastEvents.isEmpty()) {
            logger.info("Past " + pastEvents.size() + " events:");
            for (int i = 0; i < pastEvents.size(); i++) {
                Event pastEvent = pastEvents.get(i);
                System.out.println(i + 1 + ". " + pastEvent.toString());
            }
        } else {
            logger.warning("No past events found.");
        }

        if (!upcomingEvents.isEmpty()) {
            logger.info("Upcoming " + upcomingEvents.size() + " events:");
            for (int i = 0; i < upcomingEvents.size(); i++) {
                Event upcomingEvent = upcomingEvents.get(i);
                System.out.println(i + 1 + ". " + upcomingEvent.toString());
            }
        } else  {
            logger.warning("No upcoming events found.");
        }
    }

    @Override
    public void run() {
        try {
            switch (mode()) {
                case "add":
                    addEvent();
                    break;

                case "clear":
                    clearEvents();
                    break;

                case "removeId":
                    removeIdEvents();
                    break;

                case "removeName":
                    removeNameEvents();
                    break;

                case "show":
                    showEvents();
                    break;

                default:
                    logger.warning("Error: invalid command line options");
            }
        } catch (Exception e) {
            logger.warning("Error " + e.getMessage());
            System.exit(1);
        }
    }
}
