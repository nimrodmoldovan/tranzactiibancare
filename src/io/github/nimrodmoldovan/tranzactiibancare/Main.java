package io.github.nimrodmoldovan.tranzactiibancare;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%4$s] | %1$tF %1$tT | %5$s%n");
        LOG.info("Pornire aplicatie");
        String inputPath = "data/clienti.csv";
        String outputPath = "data/clienti-out.csv";
        ClientRepository repository = new ClientRepository();
        try {
            Map<Integer, BankClient> clients = repository.load(inputPath);
            for (BankClient client : clients.values()) {
                System.out.println(client);
            }
            repository.save(outputPath, clients.values());
        } catch (IOException e) {
            System.err.println("Eroare de fisier: " + e.getMessage());
        }
        LOG.info("Inchidere aplicatie");
    }
}
