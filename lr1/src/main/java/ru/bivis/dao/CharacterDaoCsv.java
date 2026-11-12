package ru.bivis.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bivis.dto.CharacterDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CharacterDaoCsv implements CharacterDao {
    private static final Logger log = LoggerFactory.getLogger(CharacterDaoCsv.class);

    private static final String SEPARATOR = ",";
    private static final String NAME_COLUMN = "name";
    private static final String SPECIES_COLUMN = "species";

    private final String resourceName;

    public CharacterDaoCsv(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public List<CharacterDto> findAll() {
        List<String> lines = readLines();
        if (lines.isEmpty()) {
            log.warn("Ресурс {} пустой", resourceName);
            return new ArrayList<>();
        }
        List<String> header = Arrays.asList(lines.get(0).split(SEPARATOR, -1));
        int nameIndex = columnIndex(header, NAME_COLUMN);
        int speciesIndex = columnIndex(header, SPECIES_COLUMN);

        List<CharacterDto> characters = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            String[] values = line.split(SEPARATOR, -1);
            if (values.length <= Math.max(nameIndex, speciesIndex)) {
                String message = "Строка " + (i + 1) + " в " + resourceName + " содержит слишком мало колонок: " + line;
                log.error(message);
                throw new IllegalStateException(message);
            }
            characters.add(new CharacterDto(values[nameIndex].trim(), values[speciesIndex].trim()));
        }
        log.info("Прочитано персонажей: {}", characters.size());
        return characters;
    }

    private int columnIndex(List<String> header, String column) {
        int index = header.indexOf(column);
        if (index < 0) {
            String message = "В заголовке " + resourceName + " нет колонки " + column;
            log.error(message);
            throw new IllegalStateException(message);
        }
        return index;
    }

    private List<String> readLines() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                String message = "Не найден ресурс " + resourceName;
                log.error(message);
                throw new IllegalStateException(message);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                return reader.lines().toList();
            }
        } catch (IOException e) {
            log.error("Не удалось прочитать ресурс {}", resourceName, e);
            throw new UncheckedIOException("Не удалось прочитать ресурс " + resourceName, e);
        }
    }
}
