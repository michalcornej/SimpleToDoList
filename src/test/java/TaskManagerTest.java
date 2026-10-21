import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TaskManagerTest {

    private static final LocalDateTime DATE = LocalDateTime.of(2026, 10, 21, 18, 0);

    @TempDir
    Path tempDir;

    @Test
    void duplicateNameIsRejectedIgnoringCase() {
        TaskManager manager = new TaskManager(tempDir.resolve("tasks.csv").toString());

        assertTrue(manager.addTask(new Task("Nákup", DATE)));
        assertFalse(manager.addTask(new Task("NÁKUP", DATE)));
        assertEquals(1, manager.getAllTasks().size());
    }

    @Test
    void findAndRemoveTaskByName() {
        TaskManager manager = new TaskManager(tempDir.resolve("tasks.csv").toString());
        manager.addTask(new Task("Nákup", DATE));

        assertNotNull(manager.findTask("nákup"));
        assertTrue(manager.removeTask("nákup"));
        assertNull(manager.findTask("nákup"));
        assertFalse(manager.removeTask("nákup"));
    }

    @Test
    void savedTasksCanBeLoadedAgain() {
        String file = tempDir.resolve("tasks.csv").toString();
        TaskManager manager = new TaskManager(file);
        manager.addTask(new Task("Nákup, mléko", DATE));
        manager.addTask(new Task("Úklid", DATE, TaskStatus.COMPLETED));
        manager.saveTasksToCSV();

        TaskManager loaded = new TaskManager(file);
        loaded.loadTasksFromCSV();

        assertEquals(2, loaded.getAllTasks().size());
        assertEquals(TaskStatus.COMPLETED, loaded.findTask("Úklid").getStatus());
        assertNotNull(loaded.findTask("Nákup, mléko"));
    }

    @Test
    void corruptedLinesAreSkipped() throws IOException {
        Path file = tempDir.resolve("tasks.csv");
        Files.writeString(file, "Nákup,2026-10-21 18:00,nesplněn\nnesmysl\n\nÚklid,2026-10-22 10:00,splněn\n", StandardCharsets.UTF_8);

        TaskManager manager = new TaskManager(file.toString());
        manager.loadTasksFromCSV();

        assertEquals(2, manager.getAllTasks().size());
    }
}
