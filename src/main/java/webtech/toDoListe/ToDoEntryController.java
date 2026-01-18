package webtech.toDoListe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
public class ToDoEntryController {

    @Autowired
    ToDoEntryService service;

    Logger logger = LoggerFactory.getLogger(ToDoEntryController.class);

    @CrossOrigin
    @PostMapping("/todos")
    public ResponseEntity<ToDoEntry> createTodo(@RequestBody ToDoEntry todo) {  // ResponseEntity
        logger.info("POST /todos mit Name={}", todo.getName());
        ToDoEntry saved = service.save(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);  // 201 Created
    }

    @CrossOrigin
    @GetMapping("/todos/{id}")
    public ToDoEntry getTodo(@PathVariable String id) {
        logger.info("GET /todos/{}", id);
        Long todoId = Long.parseLong(id);
        return service.get(todoId);
    }

    @CrossOrigin
    @GetMapping("/todos")
    public List<ToDoEntry> getAllTodos() {
        logger.info("GET /todos (alle)");
        return service.getAll();
    }

    @CrossOrigin
    @DeleteMapping("/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        logger.info("DELETE /todos/{}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
