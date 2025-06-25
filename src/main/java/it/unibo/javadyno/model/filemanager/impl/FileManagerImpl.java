package it.unibo.javadyno.model.filemanager.impl;

public class FileManagerImpl implements it.unibo.javadyno.model.filemanager.api.FileManager {

    private it.unibo.javadyno.model.filemanager.api.FileStrategy strategy;

    @Override
    public void setStrategy(it.unibo.javadyno.model.filemanager.api.FileStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void exportDataToFile(java.util.Queue<it.unibo.javadyno.model.data.api.ElaboratedData> dataQueue, java.io.File file) throws java.io.IOException {
        if (strategy == null) {
            throw new java.io.IOException("No file strategy set.");
        }
        strategy.exportData(new java.util.ArrayList<>(dataQueue), file);
    }

    @Override
    public java.util.List<it.unibo.javadyno.model.data.api.ElaboratedData> importDataFromFile(java.io.File file) throws java.io.IOException {
        if (strategy == null) {
            throw new java.io.IOException("No file strategy set.");
        }
        return strategy.importData(file);
    }

}
