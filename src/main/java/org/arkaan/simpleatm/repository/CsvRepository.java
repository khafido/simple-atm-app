package org.arkaan.simpleatm.repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class CsvRepository<T> {
    
    protected final List<T> data;
    protected final String csvPath;
    
    public CsvRepository(List<T> data, String csvPath) {
        this.data = data;
        this.csvPath = csvPath;
        initData(csvPath);
    }
    
    protected abstract void initData(String csvPath);
    
    public void saveAll() {
        try (FileWriter fileWriter = new FileWriter(csvPath, false)) {
            BufferedWriter writer = new BufferedWriter(fileWriter);
            for (T d : data) {
                writer.append(d.toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}
