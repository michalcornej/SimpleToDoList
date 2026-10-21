# Java Simple To-Do List

Jednoduchá konzolová aplikace pro správu úkolů napsaná v Javě. Projekt demonstruje základní principy objektově orientovaného programování (OOP) a ukládání dat do souboru.

## Klíčové vlastnosti
* **Správa úkolů:** přidání, výpis, označení jako splněný a smazání úkolu.
* **Přehled na úvodní obrazovce:** nesplněné úkoly po termínu, na dnešek a na zítřek.
* **Ukládání dat:** úkoly se automaticky ukládají do souboru `tasks.csv` a zůstanou zachované i po ukončení aplikace. Poškozené řádky v souboru se při načítání přeskočí.
* **Kontrola vstupů:** prázdný název, duplicitní název, neplatné číslo nebo datum aplikace odmítne a zeptá se znovu.

## Technologie
* **Jazyk:** Java 19+
* **Sestavení:** Maven
* **Testy:** JUnit 5
* **Úložiště:** CSV soubor (název úkolu, termín splnění, stav)

## Spuštění

```bash
mvn compile exec:java
```

Testy:

```bash
mvn test
```

## Ukázka

```
VÍTEJTE V APLIKACI TO DO APP!
Dnes je 21. října 2026 8:35.
Úkoly na zítra:
úkol: Nákup, mléko, datum splnění: 22. října 2026 18:00, stav: nesplněn

1. Vytvoření nového úkolu
2. Zobrazení seznamu všech úkolů
3. Označit úkol jako splněný
4. Smazat úkol
5. Konec
Vyberte možnost:
```

## Struktura
* `Main` – vstupní bod aplikace
* `Application` – hlavní smyčka a menu
* `UserInput` – čtení a kontrola vstupů od uživatele
* `Task`, `TaskStatus` – model úkolu a převod do/z CSV
* `TaskManager` – seznam úkolů, ukládání a načítání ze souboru
