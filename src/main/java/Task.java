import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;

public class Task {

    /** Formát data a času pro zadávání a ukládání do CSV */
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    /** Formát data a času pro výpis uživateli */
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);

    /** Název úkolu */
    private final String name;
    /** Datum a čas splnění úkolu */
    private final LocalDateTime completionDate;
    /** Stav úkolu */
    private TaskStatus status;

    /**
     * Vytvoří nový nesplněný úkol.
     * @param name Název úkolu
     * @param completionDate Datum a čas splnění úkolu
     */
    public Task(String name, LocalDateTime completionDate) {
        this(name, completionDate, TaskStatus.NOT_COMPLETED);
    }

    public Task(String name, LocalDateTime completionDate, TaskStatus status) {
        this.name = name;
        this.completionDate = completionDate;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Převede úkol do formátu CSV.
     * Tento řetězec obsahuje název úkolu, datum a čas splnění úkolu a stav úkolu.
     *
     * @return Řetězec ve formátu CSV, kde jsou hodnoty odděleny čárkami.
     */
    public String toCSV() {
        return name + "," + completionDate.format(DATE_FORMAT) + "," + status;
    }

    /**
     * Vytvoří úkol z řádku ve formátu CSV.
     * Řádek se dělí od konce, takže název úkolu smí obsahovat čárky.
     *
     * @param csv Řetězec ve formátu CSV, který obsahuje informace o úkolu.
     * @return Novou instanci úkolu vytvořenou z CSV řádku.
     * @throws IllegalArgumentException Pokud řádek nemá platný formát nebo obsahuje neplatné datum či stav úkolu.
     */
    public static Task fromCSV(String csv) {
        int statusSeparator = csv.lastIndexOf(',');
        int dateSeparator = statusSeparator > 0 ? csv.lastIndexOf(',', statusSeparator - 1) : -1;
        if (dateSeparator < 0) {
            throw new IllegalArgumentException("Neplatný řádek CSV: " + csv);
        }

        String name = csv.substring(0, dateSeparator);
        LocalDateTime completionDate;
        try {
            completionDate = LocalDateTime.parse(csv.substring(dateSeparator + 1, statusSeparator), DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Neplatné datum v řádku CSV: " + csv, e);
        }
        TaskStatus status = TaskStatus.fromText(csv.substring(statusSeparator + 1));
        return new Task(name, completionDate, status);
    }

    /** Vrací textovou reprezentaci úkolu */
    @Override
    public String toString() {
        return "úkol: " + name + ", datum splnění: " + completionDate.format(DISPLAY_FORMAT) + ", stav: " + status;
    }
}
