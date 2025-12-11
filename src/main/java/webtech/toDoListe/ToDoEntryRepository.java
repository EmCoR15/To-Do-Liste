package webtech.toDoListe;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface ToDoEntryRepository extends CrudRepository<ToDoEntry, Long> {
}
