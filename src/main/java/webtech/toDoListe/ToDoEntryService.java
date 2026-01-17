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

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
