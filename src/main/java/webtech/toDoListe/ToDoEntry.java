package webtech.toDoListe;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class ToDoEntry {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime dueTime;
    private boolean done;

    public ToDoEntry(){

    }

    public ToDoEntry( String name, String description, LocalDateTime dueTime, boolean done) {
        this.name = name;
        this.description = description;
        this.dueTime = dueTime;
        this.done = done;
    }


    // Getter – unbedingt notwendig für JSON (Jackson)
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getDueTime() { return dueTime; }
    public boolean isDone() { return done; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setDueTime(LocalDateTime dueTime) { this.dueTime = dueTime; }
    public void setDone(boolean done) { this.done = done; }
}



