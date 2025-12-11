package webtech.toDoListe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ToDoEntryController {

    @Autowired
    ToDoEntryService service;

    Logger logger = LoggerFactory.getLogger(ToDoEntryController.class);

    @CrossOrigin
    @PostMapping("/todos")
    public ToDoEntry createTodo(@RequestBody ToDoEntry todo) {
        logger.info("POST /todos mit Name={}", todo.getName());
        return service.save(todo);
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
}
