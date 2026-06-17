package hexlet.pet;

import hexlet.pet.calendar.Calendar;
import hexlet.pet.calendar.Event;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Command(
        name = "EventCalendar",
        mixinStandardHelpOptions = true,
        version = "EventCalendar 0.5",
        description = "An events calendar where you can add events and see when the next one is"
)
public class App implements  Runnable {

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

    @Override
    public void run() {
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

        try {
            Calendar calendar = new Calendar(filePath);
            switch (mode) {
                case "add":
                    if (name == null || date == null) {
                        System.err.println("Error: --add requires --name and --date");
                        System.exit(1);
                    }
                    LocalDateTime dateTime = date.atStartOfDay();
                    calendar.add(name, dateTime);
                    System.out.println("Event added: " + name + " on " + dateTime);
                    break;

                case "clear":
                    calendar.clear();
                    System.out.println("All events are removed.");
                    break;

                case "removeId":
                    boolean removed = calendar.removeById(removeId);
                    if (removed) {
                        System.out.println("Event with ID " + removeId + " removed.");
                    } else  {
                        System.out.println("Event with ID " + removeId + " not found.");
                        System.exit(1);
                    }
                    break;

                case "removeName":
                    boolean removedName = calendar.removeByName(removeName);
                    if (removedName) {
                        System.out.println("Event with name " + removeName + " removed.");
                    } else {
                        System.out.println("Event with name " + removeName + " not found.");
                        System.exit(1);
                    }
                    break;

                case "show":
                    List<Event> pastEvents = calendar.getPast(past);
                    List<Event> upcomingEvents = calendar.getUpcoming(show);

                    if (!pastEvents.isEmpty()) {
                        System.out.println("Past " + pastEvents.size() + " events:");
                        for (int i = 0; i < pastEvents.size(); i++) {
                            Event pastEvent = pastEvents.get(i);
                            System.out.println(i + 1 + ". " + pastEvent.toString());
                        }
                    } else {
                        System.out.println("No past events found.");
                    }

                    if (!upcomingEvents.isEmpty()) {
                        System.out.println("Upcoming " + upcomingEvents.size() + " events:");
                        for (int i = 0; i < upcomingEvents.size(); i++) {
                            Event upcomingEvent = upcomingEvents.get(i);
                            System.out.println(i + 1 + ". " + upcomingEvent.toString());
                        }
                    } else  {
                        System.out.println("No upcoming events found.");
                    }
                    break;
                default:
                    System.err.println("Error: invalid command line options");
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
            System.exit(1);
        }
    }
}
