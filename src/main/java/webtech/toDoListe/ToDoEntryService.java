package webtech.toDoListe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToDoEntryService {

    @Autowired
    ToDoEntryRepository repo;

    public ToDoEntry save(ToDoEntry entry) {
        entry.setId(null);
        return repo.save(entry);
    }

    public ToDoEntry get(Long id) {
        return repo.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<ToDoEntry> getAll() {
        Iterable<ToDoEntry> iterator = repo.findAll();
        List<ToDoEntry> todos = new ArrayList<>();
        for (ToDoEntry e : iterator) {
            todos.add(e);
        }
        return todos;
    }
    public ToDoEntry update(Long id, ToDoEntry updatedEntry) {
        ToDoEntry existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("ToDo mit ID " + id + " nicht gefunden"));

        // Felder aktualisieren
        existing.setName(updatedEntry.getName());
        existing.setDescription(updatedEntry.getDescription());
        existing.setDueTime(updatedEntry.getDueTime());
        existing.setDone(updatedEntry.isDone());

        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("ToDo mit ID " + id + " nicht gefunden");
        }
        repo.deleteById(id);
    }
}
