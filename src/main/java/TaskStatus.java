/**
 * Výčtový typ pro stav úkolu.
 * Obsahuje možné stavy úkolu - splněn nebo nesplněn.
 */

public enum TaskStatus {

    COMPLETED ("splněn"),
    NOT_COMPLETED ("nesplněn");

    /** Textová reprezentace stavu úkolu */
    private final String text;

    /**
     * Konstruktor výčtového typu TaskStatus.
     * @param text Textová reprezentace stavu úkolu
     */
    TaskStatus(String text){
        this.text = text;
    }

    /**  Převede textovou reprezentaci stavu úkolu zpět na odpovídající hodnotu TaskStatus.
     * Tato metoda prohledá všechny hodnoty výčtového typu (enum) TaskStatus a pokusí se najít
     * odpovídající stav úkolu na základě zadaného textu (ignoruje velikost písmen).
     *
     * @param text Textová reprezentace stavu úkolu, např. "splněn" nebo "nesplněn".
     * @return Odpovídající hodnota TaskStatus, pokud je nalezena.
     * @throws IllegalArgumentException pokud zadaný text neodpovídá žádné hodnotě TaskStatus.
     */
    public static TaskStatus fromText(String text) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.text.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Neplatný stav úkolu: " + text);
    }

    /**
     * Vrací textovou reprezentaci stavu úkolu.
     * @return Text stavu úkolu
     */
    @Override
    public String toString() {
        return text;
    }

}

