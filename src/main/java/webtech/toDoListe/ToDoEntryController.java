package webtech.toDoListe;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

public class ToDoEntryController {

    public List<ToDoEntry> getTodoEntries() {
        return List.of(new ToDoEntry("L1"));

    }
}
