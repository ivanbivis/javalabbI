package ru.bivis.service;

import java.util.List;
import java.util.TreeMap;

public interface SpeciesReportWriter {
    void write(TreeMap<String, List<String>> namesBySpecies);
}
