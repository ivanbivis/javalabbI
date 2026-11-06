package ru.bivis.dao;

import ru.bivis.dto.CharacterDto;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CharacterDaoCsv implements CharacterDao {
    private static final String SEPARATOR = ",";
    private static final String NAME_COLUMN = "name";
    private static final String SPECIES_COLUMN = "species";

    private final Path csvPath;

    public CharacterDaoCsv(Path csvPath) {
        this.csvPath = csvPath;
    }

    @Override
    public List<CharacterDto> findAll() {
        List<String> lines = readLines();
        if (lines.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> header = Arrays.asList(lines.get(0).split(SEPARATOR, -1));
        int nameIndex = header.indexOf(NAME_COLUMN);
        int speciesIndex = header.indexOf(SPECIES_COLUMN);
        List<CharacterDto> characters = new ArrayList<>();
        for (String line : lines.subList(1, lines.size())) {
            if (line.isBlank()) {
                continue;
            }
            String[] values = line.split(SEPARATOR, -1);
            characters.add(new CharacterDto(values[nameIndex].trim(), values[speciesIndex].trim()));
        }
        return characters;
    }

    private List<String> readLines() {
        try {
            return Files.readAllLines(csvPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось прочитать файл " + csvPath, e);
        }
    }
}
