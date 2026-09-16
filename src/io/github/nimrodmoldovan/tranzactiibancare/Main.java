package io.github.nimrodmoldovan.tranzactiibancare;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%4$s] | %1$tF %1$tT | %5$s%n");
        Level logLevel = Level.INFO;
        for (String arg : args) {
            switch (arg) {
                case "--quiet" -> logLevel = Level.WARNING;
                case "--verbose" -> logLevel = Level.FINE;
                case "--silent" -> logLevel = Level.OFF;
                default -> {
                    if (arg.startsWith("--")) {
                        LOG.warning("Argument necunoscut: " + arg + " (folositi --quiet, --verbose, --silent)");
                    }
                }
            }
        }
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(logLevel);
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(logLevel);
        }
        LOG.info("Pornire aplicatie");
        String inputPath = "data/clienti.csv";
        String outputPath = "data/clienti-out.csv";
        ClientRepository repository = new ClientRepository();
        try {
            Map<Integer, BankClient> clients = repository.load(inputPath);
            BankService service = new BankService(clients);
            for (BankClient client : clients.values()) {
                System.out.println(client);
            }
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.print("ID client (exit pentru salvare): ");
                    String idText = scanner.nextLine().trim();
                    if (idText.equalsIgnoreCase("exit")) {
                        break;
                    }
                    int id;
                    try {
                        id = Integer.parseInt(idText);
                    } catch (NumberFormatException e) {
                        LOG.warning("ID invalid: " + idText);
                        continue;
                    }
                    if (!service.clientExists(id)) {
                        LOG.warning("Client inexistent: " + id);
                        System.out.println("Esuat. Client inexistent: " + id);
                        continue;
                    }
                    System.out.print("Operatie (adauga/scade): ");
                    Operation operation;
                    try {
                        operation = Operation.fromString(scanner.nextLine());
                    } catch (IllegalArgumentException e) {
                        LOG.warning(e.getMessage());
                        System.out.println("Esuat. " + e.getMessage());
                        continue;
                    }
                    System.out.print("Suma: ");
                    double amount;
                    try {
                        amount = Double.parseDouble(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        LOG.warning("Suma invalida");
                        continue;
                    }
                    try {
                        service.execute(id, operation, amount);
                        LOG.fine("Tranzactie: id=" + id + " operatie=" + operation + " suma=" + amount);
                        System.out.println("Succes. Sold nou: "
                                + String.format(java.util.Locale.US, "%.2f", clients.get(id).getBalance()));
                    } catch (NoSuchElementException | IllegalArgumentException | IllegalStateException e) {
                        LOG.warning(e.getMessage());
                        BankClient failedClient = clients.get(id);
                        if (failedClient == null) {
                            System.out.println("Esuat. " + e.getMessage());
                        } else {
                            System.out.println("Esuat. Sold neschimbat: "
                                    + String.format(java.util.Locale.US, "%.2f", failedClient.getBalance())
                                    + " (" + e.getMessage() + ")");
                        }
                    }
                }
            }
            repository.save(outputPath, clients.values());
        } catch (IOException e) {
            LOG.severe("Eroare de fisier: " + e.getMessage());
        }
        LOG.info("Inchidere aplicatie");
    }
}
