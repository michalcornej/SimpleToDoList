import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class Application {

    /** Správce úkolů */
    private final TaskManager tasks = new TaskManager();
    /** Instance pro vstup od uživatele */
    private final UserInput userInput = new UserInput();

    /**
     * Spustí hlavní smyčku aplikace.
     * Program běží, dokud uživatel nezvolí ukončení.
     */
    public void run() {
        tasks.loadTasksFromCSV();
        System.out.println("VÍTEJTE V APLIKACI TO DO APP!");
        boolean running = true;
        while (running) {
            System.out.println("Dnes je " + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)) + ".");
            displayToDoTasks();
            printMenu();
            int choice = userInput.getIntInput("Vyberte možnost: ");

            switch (choice) {
                case 1:
                    createNewTask();
                    break;
                case 2:
                    displayAllTasks();
                    break;
                case 3:
                    setTaskAsCompleted();
                    break;
                case 4:
                    removeTask();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Neplatná volba, zkuste to znovu.");
            }
        }
        System.out.println("Na shledanou!");
    }

    /** Zobrazí možnosti pro uživatele */
    public void printMenu() {
        System.out.println("1. Vytvoření nového úkolu");
        System.out.println("2. Zobrazení seznamu všech úkolů");
        System.out.println("3. Označit úkol jako splněný");
        System.out.println("4. Smazat úkol");
        System.out.println("5. Konec");
    }

    /** Vytvoří nový úkol na základě vstupu od uživatele */
    public void createNewTask() {
        String name = userInput.getStringInput("Zadejte název úkolu: ");
        while (tasks.findTask(name) != null) {
            name = userInput.getStringInput("Úkol s tímto názvem už existuje. Zadejte jiný název: ");
        }
        LocalDateTime completionDate = userInput.getDateTimeInput("Zadejte datum a čas pro splnění úkolu ve formátu yyyy-MM-dd HH:mm: ");

        tasks.addTask(new Task(name, completionDate));
        tasks.saveTasksToCSV();
        System.out.println("Úkol byl úspěšně vytvořen.");
    }

    /** Zobrazí všechny úkoly */
    public void displayAllTasks() {
        System.out.println("SEZNAM VŠECH ÚKOLŮ");
        for (Task task : tasks.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println();
    }

    /** Označí úkol jako splněný, pokud je nalezen */
    public void setTaskAsCompleted() {
        String searchedName = userInput.getStringInput("Zadejte název úkolu: ");
        Task task = tasks.findTask(searchedName);

        if (task != null) {
            task.setStatus(TaskStatus.COMPLETED);
            tasks.saveTasksToCSV();
            System.out.println("Úkol splněn!");
        } else {
            System.out.println("Úkol nenalezen.");
        }
        System.out.println();
    }

    /** Smaže úkol podle názvu, pokud existuje */
    public void removeTask() {
        String searchedName = userInput.getStringInput("Zadejte název úkolu: ");
        if (tasks.removeTask(searchedName)) {
            tasks.saveTasksToCSV();
            System.out.println("Úkol smazán!");
        } else {
            System.out.println("Úkol nenalezen.");
        }
        System.out.println();
    }

    /** Zobrazí nesplněné úkoly, které jsou po termínu nebo naplánované na dnešek a zítřek */
    public void displayToDoTasks() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalDate tomorrow = today.plusDays(1);

        List<Task> overdue = new ArrayList<>();
        List<Task> forToday = new ArrayList<>();
        List<Task> forTomorrow = new ArrayList<>();
        for (Task task : tasks.getAllTasks()) {
            if (task.getStatus() == TaskStatus.COMPLETED) {
                continue;
            }
            LocalDateTime deadline = task.getCompletionDate();
            if (deadline.isBefore(now)) {
                overdue.add(task);
            } else if (deadline.toLocalDate().isEqual(today)) {
                forToday.add(task);
            } else if (deadline.toLocalDate().isEqual(tomorrow)) {
                forTomorrow.add(task);
            }
        }

        printTaskGroup("Úkoly po termínu:", overdue);
        printTaskGroup("Úkoly na dnes:", forToday);
        printTaskGroup("Úkoly na zítra:", forTomorrow);
    }

    /** Vypíše skupinu úkolů s nadpisem, pokud skupina není prázdná */
    private void printTaskGroup(String title, List<Task> group) {
        if (group.isEmpty()) {
            return;
        }
        System.out.println(title);
        for (Task task : group) {
            System.out.println(task);
        }
        System.out.println();
    }
}
