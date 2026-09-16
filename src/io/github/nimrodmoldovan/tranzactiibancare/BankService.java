package io.github.nimrodmoldovan.tranzactiibancare;

import java.util.Map;
import java.util.NoSuchElementException;

public class BankService {
    private final Map<Integer, BankClient> clients;

    public BankService(Map<Integer, BankClient> clients) {
        if (clients == null) {
            throw new IllegalArgumentException("Lista de clienti nu poate fi nula");
        }
        this.clients = clients;
    }

    public boolean clientExists(int id) {
        return clients.containsKey(id);
    }

    public void execute(int id, Operation op, double amount) {
        BankClient client = clients.get(id);
        if (client == null) {
            throw new NoSuchElementException("Client inexistent: " + id);
        }
        if (op == null) {
            throw new IllegalArgumentException("Operatia nu poate fi nula");
        }
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("Suma trebuie sa fie > 0: " + amount);
        }
        switch (op) {
            case DEPOSIT -> client.deposit(amount);
            case WITHDRAW -> client.withdraw(amount);
        }
    }
}
