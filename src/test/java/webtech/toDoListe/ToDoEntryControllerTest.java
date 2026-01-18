package webtech.toDoListe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit-Tests für die Klasse {@link ToDoEntryController}.
 * Diese Testklasse verwendet Spring's WebMvcTest, um die Controller-Schicht zu testen,
 * und MockMvc, um HTTP-Anfragen und -Antworten zu simulieren.
 */
@WebMvcTest(ToDoEntryController.class)
class ToDoEntryControllerTest {

    @Autowired
    MockMvc mvc; // MockMvc zum Simulieren von HTTP-Anfragen

    @MockitoBean
    ToDoEntryService service; // Gemockter Service als Abhängigkeit

    /**
     * Erstellt ein gültiges ToDoEntry-Objekt mit den angegebenen Parametern.
     *
     * @param id Die ID des ToDo-Eintrags.
     * @return Ein gültiges ToDoEntry-Objekt.
     */
    private ToDoEntry validTodo(Long id) {
        ToDoEntry todo = new ToDoEntry(
                "TestTask",                 // >= 3 Zeichen
                "Beschreibung",
                LocalDateTime.now().plusDays(1), // immer Zukunft
                false
        );
        todo.setId(id);
        return todo;
    }

    /**
     * Erstellt einen gültigen JSON-String, der ein ToDoEntry repräsentiert.
     *
     * @return Ein gültiger JSON-String für ein ToDoEntry.
     */
    private String validTodoJson() {
        return """
          {
            "name": "TestTask",
            "description": "Beschreibung",
            "dueTime": "%s",
            "done": false
          }
        """.formatted(LocalDateTime.now().plusDays(1));
    }

    /**
     * Testet, ob der Endpunkt getAllTodos eine Liste von ToDo-Einträgen
     * mit dem korrekten HTTP-Status und JSON-Format zurückgibt.
     */
    @Test
    void getAllTodos_returnsList() throws Exception {
        when(service.getAll()).thenReturn(List.of(validTodo(1L)));

        mvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("TestTask"));
    }

    /**
     * Testet, ob der Endpunkt getTodo eine spezifische ToDo-Eintragung
     * anhand der ID mit dem korrekten HTTP-Status und JSON-Format zurückgibt.
     */
    @Test
    void getTodo_byId_returnsEntry() throws Exception {
        when(service.get(5L)).thenReturn(validTodo(5L));

        mvc.perform(get("/todos/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("TestTask"));
    }

    /**
     * Testet, ob der Endpunkt createTodo den Service aufruft, um ein neues
     * ToDo-Objekt zu speichern, und das erstellte Objekt mit dem korrekten
     * HTTP-Status und JSON-Format zurückgibt.
     */
    @Test
    void createTodo_callsServiceAndReturnsCreatedObject() throws Exception {
        when(service.save(any(ToDoEntry.class))).thenReturn(validTodo(10L));

        mvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validTodoJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("TestTask"));
    }

    /**
     * Testet, ob der Endpunkt deleteTodo den Service aufruft, um ein ToDo-Objekt
     * anhand der ID zu löschen, und den HTTP-Status 204 No Content zurückgibt.
     */
    @Test
    void deleteTodo_returns204() throws Exception {
        doNothing().when(service).delete(7L);

        mvc.perform(delete("/todos/7"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(7L);
    }
}