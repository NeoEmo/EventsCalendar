package hexlet.pet.calendar;

import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    private String id;
    private String name;
    private LocalDateTime date;

    public  Event() { }

    public Event(String name, LocalDateTime date) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ") - " + date;
    }
}
