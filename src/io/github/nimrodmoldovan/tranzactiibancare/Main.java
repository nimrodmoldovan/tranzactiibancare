package io.github.nimrodmoldovan.tranzactiibancare;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String inputPath = "data/clienti.csv";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputPath))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    BankClient client = BankClient.fromCsv(line);
                    System.out.println(client);
                } catch (IllegalArgumentException e) {
                    System.err.println("Linia " + lineNumber + " ignorata: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea fisierului " + inputPath + ": " + e.getMessage());
        }
    }
}
