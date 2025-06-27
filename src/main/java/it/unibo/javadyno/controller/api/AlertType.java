package it.unibo.javadyno.controller.api;

/**
 * Enum representing different types of alerts that can be shown in the application.
 */
public enum AlertType {

    INFORMATION("Information"),
    WARNING("Warning"),
    ERROR("Error");

    private final String type;

    /**
     * Constructor for AlertType enum.
     *
     * @param type the alert type
     */
    AlertType(final String type) {
        this.type = type;
    }

    /**
     * Gets the title of the alert type.
     *
     * @return the title string
     */
    public String getType() {
        return this.type;
    }
}
