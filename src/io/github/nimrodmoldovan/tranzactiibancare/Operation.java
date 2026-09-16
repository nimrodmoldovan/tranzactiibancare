package io.github.nimrodmoldovan.tranzactiibancare;

import java.util.Locale;

public enum Operation {
    DEPOSIT,
    WITHDRAW;

    public static Operation fromString(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Operatie invalida: null (folositi adauga/scade)");
        }
        String normalized = text.trim().toLowerCase(Locale.ROOT);
        switch (normalized) {
            case "adauga":
            case "adaugă":
                return DEPOSIT;
            case "scade":
                return WITHDRAW;
            default:
                throw new IllegalArgumentException("Operatie invalida: " + text.trim() + " (folositi adauga/scade)");
        }
    }
}
