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
     * Unit-Tests für die Klasse {@link ToDoEntryService}.
     * Diese Testklasse verwendet Mockito, um die Abhängigkeiten zu mocken,
     * und AssertJ für Assertions.
     */
    @ExtendWith(MockitoExtension.class)
    class ToDoEntryServiceTest {

        @Mock
        ToDoEntryRepository repo; // Gemocktes Repository für ToDoEntry

        @InjectMocks
        ToDoEntryService service; // Zu testender Service

        /**
         * Prüft, dass beim Aufruf von save die ID des Eintrags vor dem
         * Speichern auf null gesetzt wird und das Repository mit dem
         * erwarteten Objekt aufgerufen wird.
         */
        @Test
        void save_setsIdNullAndCallsRepoSave() {
            ToDoEntry entry = new ToDoEntry("A", "B", LocalDateTime.now().plusDays(1), false);
            entry.setId(99L); // ID absichtlich gesetzt, um die Nullsetzung zu testen

            when(repo.save(any(ToDoEntry.class))).thenAnswer(inv -> inv.getArgument(0));

            ToDoEntry saved = service.save(entry);

            ArgumentCaptor<ToDoEntry> captor = ArgumentCaptor.forClass(ToDoEntry.class);
            verify(repo).save(captor.capture());

            assertThat(captor.getValue().getId()).isNull(); // Verifiziert, dass ID null ist
            assertThat(saved.getName()).isEqualTo("A");
            assertThat(saved.isDone()).isFalse();
        }

        /**
         * Prüft, dass get die korrekte Entität zurückgibt, wenn das Repository
         * einen Eintrag für die angegebene ID findet.
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
         * Prüft, dass get eine RuntimeException wirft, wenn das Repository
         * keinen Eintrag für die angegebene ID findet.
         */
        @Test
        void get_throws_whenNotFound() {
            when(repo.findById(123L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.get(123L))
                    .isInstanceOf(RuntimeException.class);
        }

        /**
         * Prüft, dass getAll das vom Repository zurückgegebene Iterable
         * korrekt in eine List konvertiert.
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
         * Prüft, dass delete die Repository-Methode deleteById mit der
         * erwarteten ID aufruft.
         */
        @Test
        void delete_callsRepoDeleteById() {
            service.delete(7L);
            verify(repo).deleteById(7L);
        }
    }