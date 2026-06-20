package hexlet.pet;

import hexlet.pet.calendar.Calendar;
import hexlet.pet.calendar.Event;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
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
    private final String eventWithId = "Event with ID ";
    private final String notFound = " not found.";

    @Option(names = {"-a", "--add"}, description = "Add new event")
    private boolean add;

    @Option(names = {"-n", "--name"}, description = "Event name")
    private String name;

    @Option(names = {"-d", "--date"}, description = "Date (yyyy-MM-dd)")
    private LocalDate date;

    @Option(names = {"-s", "--show"}, description = "Show near events (default: 3 upcoming ones)")
    private int show = 3;

    @Option(names = {"-p", "--past"}, description = "Show past events (default: 3 past events)")
    private int past = 3;

    @Option(names = {"-f", "--file"}, description = "Path to JSON`s (advanced option, if you need)",
            defaultValue = "event.json")
    private String filePath;

    @Option(names = {"-r", "--remove"}, description = "Remove some event by ID")
    private String removeId;

    @Option(names = {"-rn", "--removeByName"}, description = "Remove some event by name")
    private String removeName;

    @Option(names = {"-c", "--clear"}, description = "Remove all events")
    private boolean clear;

    @Option(names = {"-e", "--edit"}, description = "The ID of the event to be edited")
    private String editId;

    @Option(names = {"-ed", "--editDate"}, description = "Edit some event Date")
    private LocalDate editDate;

    @Option(names = {"-en", "--editName"}, description = "Edit some event Name")
    private String editName;

    @Option(names = {"-au", "--autoUpdate"}, description = "Enable auto Update flag")
    private boolean autoUpdate;

    @Option(names = {"-t", "--telegram"}, description = "open telegram bot and help Integration with bot")
    private boolean telegram;

    @Option(names = {"-i", "--integrationID"}, description = "ID chat with bot")
    private String integrationID;

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
        } else if (editDate != null) {
            return "editDate";
        }  else if (editName != null && !editName.isBlank()) {
            return "editName";
        } else if (telegram) {
            return  "telegram";
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
        if (autoUpdate) {
            calendar.add(name, dateTime, true);
        } else {
            calendar.add(name, dateTime);
        }
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
            System.out.println(eventWithId + removeId + " removed.");
        } else  {
            System.out.println(eventWithId + removeId + notFound);
            System.exit(1);
        }
    }

    private void removeNameEvents() throws IOException {
        calendar = new Calendar(filePath);
        boolean removedName = calendar.removeByName(removeName);
        if (removedName) {
            System.out.println("Event with name " + removeName + " removed.");
        } else {
            System.out.println("Event with name " + removeName + notFound);
            System.exit(1);
        }
    }

    private void showEvents() throws IOException {
        calendar = new Calendar(filePath);
        List<Event> pastEvents = calendar.getPast(past);
        List<Event> upcomingEvents = calendar.getUpcoming(show);

        if (!pastEvents.isEmpty()) {
            System.out.println("Past " + pastEvents.size() + " events:");
            for (int i = 0; i < pastEvents.size(); i++) {
                Event pastEvent = pastEvents.get(i);
                System.out.println(i + 1 + ". " + pastEvent.toString());
            }
        } else {
            logger.warning("No past events found.");
        }

        if (!upcomingEvents.isEmpty()) {
            System.out.println("Upcoming " + upcomingEvents.size() + " events:");
            for (int i = 0; i < upcomingEvents.size(); i++) {
                Event upcomingEvent = upcomingEvents.get(i);
                System.out.println(i + 1 + ". " + upcomingEvent.toString());
            }
        } else  {
            logger.warning("No upcoming events found.");
        }
    }

    private void editDateEvent() throws IOException {
        calendar = new Calendar(filePath);
        if (editDate == null) {
            logger.warning("Error: --edit requires --editDate");
            System.exit(1);
        }
        var newDateTime = editDate.atStartOfDay();
        boolean updated = calendar.editDate(editId, newDateTime);

        if (updated) {
            System.out.println(eventWithId + editId + " updated.");
        } else  {
            System.out.println(eventWithId + editId + notFound);
            System.exit(1);
        }
    }

    private void editNameEvent() throws IOException {
        calendar = new Calendar(filePath);
        if (editName == null) {
            logger.warning("Error: --edit requires --editName");
            System.exit(1);
        }
        var newName = editName.trim();
        boolean updated = calendar.editName(editId, newName);

        if (updated) {
            System.out.println(eventWithId + editId + " updated.");
        } else   {
            System.out.println(eventWithId + editId + notFound);
            System.exit(1);
        }
    }

    private static String readFixtures(String fileName) throws IOException {
        try (InputStream is = App.class.getResourceAsStream("/fixtures/" + fileName)) {
            if (is == null) {
                throw new IOException("Не найдены фикстуры " + fileName);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
        }
    }

    private void telegramIntegration() throws IOException {
        calendar = new Calendar(filePath);
        if (telegram && (integrationID == null ||  integrationID.isBlank())) {
            int time = 15000;
            System.out.println("Внимание, включено ожидание прочтения helpIntegration на " + time + " мс");
            System.out.println(readFixtures("helpIntegration"));
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Ожидание прочтения helpIntegration закончено");
            }
            openChatWithBot();
        } else {
            boolean isSave = saveIntegrationId(integrationID);
            if (isSave) {
                System.out.println("Chat ID проверен и сохранён. Теперь уведомления будут приходить в Telegram.");
            } else {
                System.out.println("Неправильно введён Chat ID");
            }
        }
    }

    private static Path getConfigPath() {
        try {
            String jarPath = App.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            Path jarDir = Paths.get(jarPath).getParent();
            Path binDir = jarDir.resolveSibling("bin");
            return binDir.resolve("integration.properties");
        } catch (URISyntaxException e) {
            return Paths.get("integration.properties");
        }
    }

    private boolean saveIntegrationId(String chatID) throws IOException {
        if (chatID == null || chatID.isBlank()) {
            System.out.println("Chat ID не может быть пустым");
            return false;
        }

        Path configPath = getConfigPath();
        Files.createDirectories(configPath.getParent());

        Properties props = new Properties();
        if (Files.exists(configPath)) {
            try (InputStream is = Files.newInputStream(configPath)) {
                props.load(is);
            }
        }

        props.setProperty("chatID", chatID);
        try (OutputStream out = Files.newOutputStream(configPath)) {
            props.store(out, "Telegram Integration settings");
        }
        return true;
    }

    private void openChatWithBot() throws IOException {
        String botName = "BotCalendarEventBot";
        String url = "https://t.me/" + botName;
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
                System.out.println("Чат с ботом открыт в браузере.");
            } else {
                System.out.println("Не удалось открыть бота. попробуйте вручную " + url
                        + " или напишите автору в тг @ImLunev");
            }
        } catch (Exception e) {
            logger.warning("Ошибка при открытии чата " + e.getMessage());
            logger.warning("Перейдите по-ссылке вручную" + url + "или напишите автору в тг @ImLunev");
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

                case "editDate":
                    editDateEvent();
                    break;

                case "editName":
                    editNameEvent();
                    break;

                case "telegram":
                    telegramIntegration();
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
