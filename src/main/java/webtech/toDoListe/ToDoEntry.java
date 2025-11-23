package webtech.toDoListe;

import java.time.LocalDateTime;

public class ToDoEntry {

    private long id;
    private String name;
    private String description;
    private LocalDateTime dueTime;
    private boolean done;

    // Konstruktor zum einfachen Erstellen eines Beispiels
    public ToDoEntry(long id, String name, String description, LocalDateTime dueTime, boolean done) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dueTime = dueTime;
        this.done = done;
    }

    // Getter – unbedingt notwendig für JSON (Jackson)
    public long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getDueTime() { return dueTime; }
    public boolean isDone() { return done; }
}

