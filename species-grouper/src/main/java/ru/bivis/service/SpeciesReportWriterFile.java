package ru.bivis.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SpeciesReportWriterFile implements SpeciesReportWriter {
    private static final String ARROW = " → ";

    private final Path outputPath;

    public SpeciesReportWriterFile(Path outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public void write(TreeMap<String, List<String>> namesBySpecies) {
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            for (Map.Entry<String, List<String>> entry : namesBySpecies.entrySet()) {
                writer.write(formatLine(entry.getKey(), entry.getValue()));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось записать файл " + outputPath, e);
        }
    }

    private String formatLine(String species, List<String> names) {
        return species + ARROW + names;
    }
}
