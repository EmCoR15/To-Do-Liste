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
         * Unit-Tests für {@link ToDoEntryService} (Mockito, ohne Spring Context).
         */
        @ExtendWith(MockitoExtension.class)
        class ToDoEntryServiceTest {

            @Mock
            ToDoEntryRepository repo; // Mock-Repository für ToDoEntry

            @InjectMocks
            ToDoEntryService service; // Zu testender Service

            /**
             * Erstellt ein gültiges ToDoEntry-Objekt mit den angegebenen Parametern.
             *
             * @param id   Die ID des ToDo-Eintrags.
             * @param done Der Status, ob der Eintrag abgeschlossen ist.
             * @return Ein gültiges ToDoEntry-Objekt.
             */
            private ToDoEntry validTodo(Long id, boolean done) {
                ToDoEntry todo = new ToDoEntry(
                        "TestTask",
                        "Beschreibung",
                        LocalDateTime.now().plusDays(1),
                        done
                );
                todo.setId(id);
                return todo;
            }

            /**
             * Testet, ob die Methode save die ID des Eintrags vor dem Speichern
             * auf null setzt und das Repository mit dem erwarteten Objekt aufruft.
             */
            @Test
            void save_setsIdNullAndCallsRepoSave() {
                ToDoEntry entry = validTodo(99L, false); // absichtlich ID gesetzt

                when(repo.save(any(ToDoEntry.class))).thenAnswer(inv -> inv.getArgument(0));

                ToDoEntry saved = service.save(entry);

                ArgumentCaptor<ToDoEntry> captor = ArgumentCaptor.forClass(ToDoEntry.class);
                verify(repo).save(captor.capture());

                assertThat(captor.getValue().getId()).isNull(); // Service setzt ID auf null
                assertThat(saved.getName()).isEqualTo("TestTask");
                assertThat(saved.isDone()).isFalse();
            }

            /**
             * Testet, ob die Methode get den korrekten Eintrag zurückgibt,
             * wenn das Repository einen Eintrag für die angegebene ID findet.
             */
            @Test
            void get_returnsEntry_whenFound() {
                ToDoEntry entry = validTodo(1L, false);
                when(repo.findById(1L)).thenReturn(Optional.of(entry));

                ToDoEntry result = service.get(1L);

                assertThat(result.getId()).isEqualTo(1L);
                assertThat(result.getName()).isEqualTo("TestTask");
            }

            /**
             * Testet, ob die Methode get eine RuntimeException wirft,
             * wenn das Repository keinen Eintrag für die angegebene ID findet.
             */
            @Test
            void get_throws_whenNotFound() {
                when(repo.findById(123L)).thenReturn(Optional.empty());

                assertThatThrownBy(() -> service.get(123L))
                        .isInstanceOf(RuntimeException.class);
            }

            /**
             * Testet, ob die Methode getAll das vom Repository zurückgegebene Iterable
             * korrekt in eine Liste konvertiert.
             */
            @Test
            void getAll_convertsIterableToList() {
                ToDoEntry a = validTodo(1L, false);
                ToDoEntry b = validTodo(2L, true);

                when(repo.findAll()).thenReturn(List.of(a, b));

                List<ToDoEntry> all = service.getAll();

                assertThat(all).hasSize(2);
                assertThat(all.get(0).getId()).isEqualTo(1L);
                assertThat(all.get(1).isDone()).isTrue();
            }

            /**
             * Testet, ob die Methode delete die Repository-Methode deleteById
             * mit der erwarteten ID aufruft, wenn der Eintrag existiert.
             */
            @Test
            void delete_callsRepoDeleteById_whenExists() {
                when(repo.existsById(7L)).thenReturn(true);

                service.delete(7L);

                verify(repo).deleteById(7L);
            }

            /**
             * Testet, ob die Methode delete eine RuntimeException wirft,
             * wenn der Eintrag mit der angegebenen ID nicht existiert.
             */
            @Test
            void delete_throws_whenNotFound() {
                when(repo.existsById(7L)).thenReturn(false);

                assertThatThrownBy(() -> service.delete(7L))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("7");

                verify(repo, never()).deleteById(anyLong());
            }

            /**
             * Testet, ob die Methode update einen bestehenden Eintrag aktualisiert
             * und das aktualisierte Objekt speichert.
             */
            @Test
            void update_updatesExistingEntityAndSaves() {
                ToDoEntry existing = validTodo(10L, false);
                when(repo.findById(10L)).thenReturn(Optional.of(existing));
                when(repo.save(any(ToDoEntry.class))).thenAnswer(inv -> inv.getArgument(0));

                ToDoEntry updated = new ToDoEntry(
                        "UpdatedTask",
                        "Neu",
                        LocalDateTime.now().plusDays(2),
                        true
                );

                ToDoEntry result = service.update(10L, updated);

                ArgumentCaptor<ToDoEntry> captor = ArgumentCaptor.forClass(ToDoEntry.class);
                verify(repo).save(captor.capture());

                ToDoEntry saved = captor.getValue();
                assertThat(saved.getId()).isEqualTo(10L);
                assertThat(saved.getName()).isEqualTo("UpdatedTask");
                assertThat(saved.getDescription()).isEqualTo("Neu");
                assertThat(saved.getDueTime()).isEqualTo(updated.getDueTime());
                assertThat(saved.isDone()).isTrue();

                assertThat(result.getName()).isEqualTo("UpdatedTask");
            }
        }