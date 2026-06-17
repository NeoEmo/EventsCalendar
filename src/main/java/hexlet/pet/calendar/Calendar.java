package hexlet.pet.calendar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Calendar {
    private final ObjectMapper mapper;
    private final String filePath;

    public Calendar(String filePath) {
        this.filePath = filePath;
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private List<Event> loadEvents() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            return mapper.readValue(file, new TypeReference<List<Event>>() { });
        } catch (MismatchedInputException e) {
            return new ArrayList<>();
        }
    }

    private void saveEvents(List<Event> events) throws IOException {
        mapper.writeValue(new File(filePath), events);
    }

    public boolean add(String name, LocalDateTime date) throws IOException {
        List<Event> events = loadEvents();
        events.add(new Event(name, date));
        saveEvents(events);
        return true;
    }

    public boolean removeById(String id) throws IOException {
        List<Event> events = loadEvents();
        boolean removed = events.removeIf(event -> event.getId().equals(id));
        if (removed) {
            saveEvents(events);
        }
        return removed;
    }

    public boolean removeByName(String name) throws IOException {
        List<Event> events = loadEvents();
        boolean removed = events.removeIf(event -> event.getName().equals(name));
        if (removed) {
            saveEvents(events);
        }
        return removed;
    }

    public boolean clear() throws IOException {
        saveEvents(new ArrayList<>());
        return true;
    }

    public List<Event> getUpcoming(int limit) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        List<Event> events = loadEvents();
        return events.stream()
                .filter(e -> e.getDate().isAfter(now))
                .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                .limit(limit)
                .toList();
    }

    public List<Event> getPast(int limit) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        List<Event> events = loadEvents();
        return events.stream()
                .filter(e -> e.getDate().isBefore(now))
                .sorted((e1, e2) -> e2.getDate().compareTo(e1.getDate()))
                .limit(limit)
                .toList();
    }

    public Optional<String> getIdByName(String name) throws IOException {
        List<Event> events = loadEvents();
        return events.stream()
                .filter(e -> e.getName().equals(name))
                .findFirst()
                .map(Event::getId);
    }
}
