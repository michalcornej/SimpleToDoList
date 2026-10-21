import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void csvRoundTripKeepsAllValues() {
        Task task = new Task("Nákup", LocalDateTime.of(2026, 10, 21, 18, 0), TaskStatus.COMPLETED);

        Task loaded = Task.fromCSV(task.toCSV());

        assertEquals("Nákup", loaded.getName());
        assertEquals(LocalDateTime.of(2026, 10, 21, 18, 0), loaded.getCompletionDate());
        assertEquals(TaskStatus.COMPLETED, loaded.getStatus());
    }

    @Test
    void nameMayContainCommas() {
        Task task = new Task("Nákup, mléko, chleba", LocalDateTime.of(2026, 10, 21, 18, 0));

        Task loaded = Task.fromCSV(task.toCSV());

        assertEquals("Nákup, mléko, chleba", loaded.getName());
        assertEquals(TaskStatus.NOT_COMPLETED, loaded.getStatus());
    }

    @Test
    void invalidLineThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Task.fromCSV("bez čárek"));
        assertThrows(IllegalArgumentException.class, () -> Task.fromCSV("Úkol,2026-10-21 18:00,neznámý stav"));
        assertThrows(IllegalArgumentException.class, () -> Task.fromCSV("Úkol,špatné datum,splněn"));
    }
}
