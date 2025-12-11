package webtech.toDoListe;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

import java.util.List;

@RestController
public class ToDoEntryController {

    @GetMapping("/todos")
    public List<ToDoEntry> getTodoEntries() {
        return List.of(
                new ToDoEntry(
                        "L1",
                        "Beispielbeschreibung",
                        LocalDateTime.now().plusDays(1),
                        false
                )
        );
    }
}


