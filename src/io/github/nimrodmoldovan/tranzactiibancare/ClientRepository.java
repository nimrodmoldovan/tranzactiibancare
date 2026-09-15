package io.github.nimrodmoldovan.tranzactiibancare;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ClientRepository {
    private static final Logger LOG = Logger.getLogger(ClientRepository.class.getName());

    public Map<Integer, BankClient> load(String path) throws IOException {
        LOG.info("Citire fisier: " + path);
        Map<Integer, BankClient> clients = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    BankClient client = BankClient.fromCsv(line);
                    if (clients.containsKey(client.getId())) {
                        LOG.warning("Linia " + lineNumber + " ignorata: ID duplicat " + client.getId());
                        continue;
                    }
                    clients.put(client.getId(), client);
                } catch (IllegalArgumentException e) {
                    LOG.warning("Linia " + lineNumber + " ignorata: " + e.getMessage());
                }
            }
        }
        LOG.info("Cititi " + clients.size() + " clienti din " + path);
        return clients;
    }

    public void save(String path, Collection<BankClient> clients) throws IOException {
        LOG.info("Scriere fisier: " + path + " (" + clients.size() + " clienti)");
        List<BankClient> sorted = new ArrayList<>(clients);
        sorted.sort(Comparator.comparingInt(BankClient::getId));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))) {
            for (BankClient client : sorted) {
                writer.write(client.toCsv());
                writer.newLine();
            }
        }
    }
}
