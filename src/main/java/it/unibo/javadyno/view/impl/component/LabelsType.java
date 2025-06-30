package it.unibo.javadyno.view.impl.component;

/**
 * Enum representing different types of buttons used in the JavaDyno application.
 */
public enum LabelsType {
    SIMULATED("Simulation", "Start Simulation", "Stop Simulation"),
    REAL("Real", "Start Acquisition", "Stop Acquisition"),
    OBD("OBD", "Start OBD Reading", "Stop OBD Reading");

    private final String title;
    private final String startButton;
    private final String stopButton;
    private final String backToMenu;
    private final String reloadButton;
    private final String saveButton;
    private final String loadButton;

    /**
     * Constructor for ButtonsType enum.
     *
     * @param title the title for the view
     * @param startButton the label for the start button
     * @param stopButton the label for the stop button
     */
    LabelsType(final String title, final String startButton, final String stopButton) {
        this.title = "JavaDyno - " + title;
        this.startButton = startButton;
        this.stopButton = stopButton;
        this.backToMenu = "Back to Menu";
        this.reloadButton = "Reload";
        this.saveButton = "Save datas";
        this.loadButton = "Import datas";
    }

    /**
     * Gets the title for the view.
     *
     * @return the title for the view
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the label for the start button.
     *
     * @return the label for the start button
     */
    public String getStartButton() {
        return this.startButton;
    }

    /**
     * Gets the label for the stop button.
     *
     * @return the label for the stop button
     */
    public String getStopButton() {
        return this.stopButton;
    }

    /**
     * Gets the label for the back to menu button.
     *
     * @return the label for the back to menu button
     */
    public String getBackToMenu() {
        return this.backToMenu;
    }

    /**
     * Gets the label for the reload button.
     *
     * @return the label for the reload button
     */
    public String getReloadButton() {
        return this.reloadButton;
    }

    /**
     * Gets the label for the save button.
     *
     * @return the label for the save button
     */
    public String getSaveButton() {
        return this.saveButton;
    }

    /**
     * Gets the label for the load button.
     *
     * @return the label for the load button
     */
    public String getLoadButton() {
        return this.loadButton;
    }
}
