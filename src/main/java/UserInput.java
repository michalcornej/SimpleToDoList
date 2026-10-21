import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UserInput {

    /** Čte vstupy od uživatele */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Získá vstup od uživatele jako řetězec.
     * Opakuje se, dokud není vstup validní.
     */
    public String getStringInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
        }
        while (input.isEmpty());
        return input;
    }

    /**
     * Získá vstup od uživatele jako celé číslo.
     * Opakuje se, dokud není vstup validní.
     */
    public int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Neplatný vstup. Zadejte číslo.");
            }
        }
    }

    /**
     * Získá vstup od uživatele jako datum a čas.
     * Opakuje se, dokud není vstup validní.
     */
    public LocalDateTime getDateTimeInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDateTime.parse(scanner.nextLine().trim(), Task.DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Neplatný vstup. Zadejte datum a čas ve formátu yyyy-MM-dd HH:mm.");
            }
        }
    }
}
