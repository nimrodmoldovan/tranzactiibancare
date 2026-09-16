package io.github.nimrodmoldovan.tranzactiibancare;

import java.util.Locale;

public class BankClient {
    private final int id;
    private final String name;
    private double balance;

    public BankClient(int id, String name, double balance) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID-ul trebuie sa fie pozitiv: " + id);
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Numele nu poate fi gol");
        }
        if (!Double.isFinite(balance) || balance < 0) {
            throw new IllegalArgumentException("Soldul trebuie sa fie >= 0: " + balance);
        }
        this.id = id;
        this.name = name.trim();
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("Suma trebuie sa fie > 0: " + amount);
        }
        balance += amount;
    }

    public void withdraw(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("Suma trebuie sa fie > 0: " + amount);
        }
        if (amount > balance) {
            throw new IllegalStateException("Fonduri insuficiente pentru clientul " + id);
        }
        balance -= amount;
    }

    public static BankClient fromCsv(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Linie invalida: null");
        }
        String[] parts = line.split(",", -1);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Linie invalida (format asteptat id,nume,sold): " + line);
        }
        int parsedId;
        double parsedBalance;
        try {
            parsedId = Integer.parseInt(parts[0].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID invalid: " + parts[0].trim());
        }
        String parsedName = parts[1].trim();
        try {
            parsedBalance = Double.parseDouble(parts[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Sold invalid: " + parts[2].trim());
        }
        return new BankClient(parsedId, parsedName, parsedBalance);
    }

    public String toCsv() {
        return String.format(Locale.US, "%d,%s,%.2f", id, name, balance);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "[%d] %s - %.2f", id, name, balance);
    }
}
