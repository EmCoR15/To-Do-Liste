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
     * Testet, ob der Endpunkt getAllTodos eine Liste von ToDo-Einträgen
     * mit dem korrekten HTTP-Status und JSON-Format zurückgibt.
     */
    @Test
    void getAllTodos_returnsList() throws Exception {
        ToDoEntry a = new ToDoEntry("A", "B", LocalDateTime.now().plusDays(1), false);
        a.setId(1L);
        when(service.getAll()).thenReturn(List.of(a));

        mvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("A"));
    }

    /**
     * Testet, ob der Endpunkt getTodo eine spezifische ToDo-Eintragung
     * anhand der ID mit dem korrekten HTTP-Status und JSON-Format zurückgibt.
     */
    @Test
    void getTodo_byId_returnsEntry() throws Exception {
        ToDoEntry a = new ToDoEntry("A", "B", LocalDateTime.now().plusDays(1), false);
        a.setId(5L);
        when(service.get(5L)).thenReturn(a);

        mvc.perform(get("/todos/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("A"));
    }

    /**
     * Testet, ob der Endpunkt createTodo den Service aufruft, um ein neues
     * ToDo-Objekt zu speichern, und das erstellte Objekt mit dem korrekten
     * HTTP-Status und JSON-Format zurückgibt.
     */
    @Test
    void createTodo_callsServiceAndReturnsCreatedObject() throws Exception {
        ToDoEntry saved = new ToDoEntry("A", "B", LocalDateTime.now().plusDays(1), false);
        saved.setId(10L);

        when(service.save(any(ToDoEntry.class))).thenReturn(saved);

        String body = """
          {"name":"A","description":"B","dueTime":"2026-01-20T12:00:00","done":false}
        """;

        mvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())  // 201 wenn du Controller geändert hast
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("A"));
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