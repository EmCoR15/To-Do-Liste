package webtech.toDoListe;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ToDoEntryService} class.
 * This test class uses Mockito to mock dependencies and AssertJ for assertions.
 */
@ExtendWith(MockitoExtension.class)
class ToDoEntryServiceTest {

    @Mock
    ToDoEntryRepository repo; // Mocked repository for ToDoEntry

    @InjectMocks
    ToDoEntryService service; // Service under test

    /**
     * Tests that the save method sets the ID of the entry to null
     * before calling the repository's save method.
     */
    @Test
    void save_setsIdNullAndCallsRepoSave() {
        ToDoEntry entry = new ToDoEntry("A", "B", LocalDateTime.now().plusDays(1), false);
        entry.setId(99L); // ID is intentionally set to test if the service nullifies it

        when(repo.save(any(ToDoEntry.class))).thenAnswer(inv -> inv.getArgument(0));

        ToDoEntry saved = service.save(entry);

        ArgumentCaptor<ToDoEntry> captor = ArgumentCaptor.forClass(ToDoEntry.class);
        verify(repo).save(captor.capture());

        assertThat(captor.getValue().getId()).isNull(); // Verify ID is nullified
        assertThat(saved.getName()).isEqualTo("A");
        assertThat(saved.isDone()).isFalse();
    }

    /**
     * Tests that the get method returns the correct entry
     * when the repository finds it by ID.
     */
    @Test
    void get_returnsEntry_whenFound() {
        ToDoEntry entry = new ToDoEntry("A", "B", LocalDateTime.now().plusDays(1), false);
        entry.setId(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(entry));

        ToDoEntry result = service.get(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("A");
    }

    /**
     * Tests that the get method throws a RuntimeException
     * when the repository does not find an entry by ID.
     */
    @Test
    void get_throws_whenNotFound() {
        when(repo.findById(123L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(123L))
                .isInstanceOf(RuntimeException.class);
    }

    /**
     * Tests that the getAll method converts the repository's
     * Iterable result into a List.
     */
    @Test
    void getAll_convertsIterableToList() {
        ToDoEntry a = new ToDoEntry("A", "B", LocalDateTime.now().plusDays(1), false);
        a.setId(1L);
        ToDoEntry b = new ToDoEntry("C", "D", LocalDateTime.now().plusDays(2), true);
        b.setId(2L);

        when(repo.findAll()).thenReturn(List.of(a, b));

        List<ToDoEntry> all = service.getAll();

        assertThat(all).hasSize(2);
        assertThat(all.get(0).getId()).isEqualTo(1L);
        assertThat(all.get(1).isDone()).isTrue();
    }

    /**
     * Tests that the delete method calls the repository's
     * deleteById method with the correct ID.
     */
    @Test
    void delete_callsRepoDeleteById() {
        service.delete(7L);
        verify(repo).deleteById(7L);
    }
}