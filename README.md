# Tranzacții Bancare

**Curs Java Online – Atelierele ILBAH**
*Proiect final*

## Descrierea problemei

Se citesc dintr-un fișier datele unor clienți bancari (id - care e un număr de ordine-, nume, suma de bani în cont). De la tastatură se citește un id al unui client, o operație (adaugă/scade) și o valoare numerică. Clientului respectiv i se vor adăuga sau scădea banii respectivi. La sfârșit se vor scrie noile detalii într-un alt fișier.

## Funcționalități

- **Citire clienți din CSV**: (`data/clienti.csv`, format `id,nume,sold`) liniile goale sunt sărite, iar liniile invalide și ID-urile duplicate sunt raportate fără oprirea programului.
- **Tranzacții interactive în buclă**: se cere repetat `id + operațiune (adaugă/scade) + sumă`, până la cuvântul `exit`.
- **Validare la fiecare pas**: ID-ul inexistent este depistat imediat după primul prompt (fail-fast); operațiunea și suma sunt validate înainte de aplicare; sumele trebuie să fie `> 0`.
- **Fără descoperit de cont**: o retragere mai mare decât soldul este refuzată, iar soldul rămâne neschimbat.
- **Verdict clar pentru utilizator**: fiecare tranzacție primește un răspuns pe ecran: `Succes. Sold nou: X` sau `Esuat. ... (motivul)`.
- **Salvare sortată după ID**: în `data/clienti-out.csv`, cu soldul formatat la 2 zecimale.
- **Jurnalizare pe consolă**: în formatul `[NIVEL] | timestamp | mesaj`, cu nivel ajustabil din argumente: `--quiet` (doar avertismente și erori), `--verbose` (inclusiv detalii per tranzacție), `--silent` (fără jurnal).
- **Configurații de rulare partajate**: în IntelliJ (meniul Run) pentru fiecare nivel de jurnalizare.

## Clase

- **`BankClient`**: modelul unui client (`id`, `name`, `balance`). Metodele `deposit`/`withdraw` aplică regulile unui singur client (sumă `> 0`, fără sold negativ); `fromCsv`/`toCsv` convertesc linia `id,nume,sold`.
- **`ClientRepository`**:  persistența: `load` citește fișierul într-un `Map` (sare peste liniile invalide, raportează duplicatele), `save` scrie colecția sortată după ID.
- **`BankService`**:  logica de tranzacționare: `clientExists` verifică existența ID-ului, iar `execute(id, operațiune, sumă)` caută clientul și rutează către `deposit`/`withdraw`.
- **`Operation`**:  enum cu valorile `DEPOSIT`/`WITHDRAW`; parsarea strictă `fromString` acceptă doar `adauga`/`adaugă` → depunere și `scade` → retragere, orice altceva fiind invalid.
- **`Main`**: bucla de consolă, încarcă clienții, afișează lista, cere tranzacții până la `exit`, apoi salvează. Singurul loc cu `Scanner`/`System.out`; erorile ajung și în jurnal.

## Format fișiere

Intrare (`data/clienti.csv`) și ieșire (`data/clienti-out.csv`). Câte un client pe linie, `id,nume,sold`:

```csv
1,Popescu Ion,1500.50
2,Ionescu Maria,0.00
```

Reguli: fără virgulă în nume, soldul cu 2 zecimale, ID-urile unice și pozitive. Fișierul de intrare din proiect conține intenționat și linii invalide (ID nemumeric, sold negativ) pentru testarea validării.

## Rulare

Din rădăcina proiectului:

```bash
javac -d out $(find src -name "*.java")
java -cp out io.github.nimrodmoldovan.tranzactiibancare.Main [--quiet|--verbose|--silent]
```

În IntelliJ: configurațiile partajate `Main`, `Main --quiet`, `Main --verbose`, `Main --silent` din meniul Run (directorul de lucru este rădăcina proiectului, pentru calea relativă `data/`).

Exemplu de sesiune:

```text
--------LISTA CLIENTILOR--------
[1] Popescu Ion - 1500.50
[2] Ionescu Maria - 0.00
--------TRANZACTIE--------
ID client (exit pentru salvare): 1
Operatie (adauga/scade): adauga
Suma: 100
Succes. Sold nou: 1600.50
--------TRANZACTIE--------
ID client (exit pentru salvare): 2
Operatie (adauga/scade): scade
Suma: 10
Esuat. Sold neschimbat: 0.00 (Fonduri insuficiente pentru clientul 2)
--------TRANZACTIE--------
ID client (exit pentru salvare): exit
```

## Cerințe

- Java 26 (sau o versiune JDK compatibilă)
- IntelliJ IDEA (recomandat) sau orice mediu care poate compila și rula cod Java

## Autor

Nimrod Moldovan ([@nimrodmoldovan](https://github.com/nimrodmoldovan))
