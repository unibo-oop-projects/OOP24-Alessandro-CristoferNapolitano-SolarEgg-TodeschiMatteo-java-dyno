package it.unibo.javadyno.model.filemanager.impl;

public class CsvStrategy implements it.unibo.javadyno.model.filemanager.api.FileStrategy {

    @Override
    public void exportData(java.util.List<it.unibo.javadyno.model.data.api.ElaboratedData> data, java.io.File file) throws java.io.IOException {
        // Implementation for exporting data to CSV file
    }

    @Override
    public java.util.List<it.unibo.javadyno.model.data.api.ElaboratedData> importData(java.io.File file) throws java.io.IOException {
        // Implementation for importing data from CSV file
        return null; // Placeholder return statement
    }

}
