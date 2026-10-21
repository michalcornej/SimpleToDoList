import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    /** Výchozí soubor pro ukládání úkolů */
    public static final String DEFAULT_FILE_NAME = "tasks.csv";

    /** Seznam úkolů */
    private final List<Task> tasks = new ArrayList<>();
    /** Soubor, do kterého se úkoly ukládají */
    private final String fileName;

    public TaskManager() {
        this(DEFAULT_FILE_NAME);
    }

    /** @param fileName Soubor, do kterého se úkoly ukládají */
    public TaskManager(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Přidá nový úkol do seznamu.
     * @return True, pokud byl úkol přidán, false, pokud už úkol se stejným názvem existuje
     */
    public boolean addTask(Task task) {
        if (findTask(task.getName()) != null) {
            return false;
        }
        tasks.add(task);
        return true;
    }

    /** Vrátí kopii seznamu všech úkolů, kterou nelze měnit */
    public List<Task> getAllTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Najde úkol podle názvu.
     * @param searchedName Název úkolu k nalezení
     * @return Nalezený úkol nebo null, pokud neexistuje
     */
    public Task findTask(String searchedName) {
        for (Task task : tasks) {
            if (task.getName().equalsIgnoreCase(searchedName)) {
                return task;
            }
        }
        return null;
    }

    /**
     * Smaže úkol podle názvu.
     * @param searchedName Název úkolu k smazání
     * @return True, pokud byl úkol smazán, jinak false
     */
    public boolean removeTask(String searchedName) {
        return tasks.removeIf(task -> task.getName().equalsIgnoreCase(searchedName));
    }

    /**
     * Uloží seznam úkolů do souboru ve formátu CSV.
     * Soubor se vytvoří nebo přepíše a každý úkol se uloží jako jeden řádek.
     * Chyba při zápisu se vypíše uživateli.
     */
    public void saveTasksToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, StandardCharsets.UTF_8))) {
            for (Task task : tasks) {
                writer.println(task.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Došlo k chybě při ukládání úkolů do souboru: " + e.getMessage());
        }
    }

    /**
     * Načte seznam úkolů ze souboru ve formátu CSV.
     * Pokud soubor neexistuje, seznam úkolů bude prázdný.
     * Poškozené řádky se přeskočí, aby kvůli nim nespadla celá aplikace.
     */
    public void loadTasksFromCSV() {
        tasks.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                try {
                    addTask(Task.fromCSV(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("Přeskakuji poškozený řádek v souboru s úkoly: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Soubor s úkoly nebyl nalezen. Začínáme s prázdným správcem úkolů.");
        } catch (IOException e) {
            System.err.println("Došlo k chybě při načítání úkolů ze souboru: " + e.getMessage());
        }
    }
}
