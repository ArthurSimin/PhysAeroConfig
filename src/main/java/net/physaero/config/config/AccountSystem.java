package net.physaero.config.config;

public enum AccountSystem {
    E4ALL("e4all"),
    ESSENTIALS("Essentials");

    private final String displayName;

    AccountSystem(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
