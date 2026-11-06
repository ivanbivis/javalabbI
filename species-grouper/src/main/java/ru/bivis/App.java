package ru.bivis;

import ru.bivis.dao.CharacterDao;
import ru.bivis.dao.CharacterDaoCsv;
import ru.bivis.dto.CharacterDto;
import ru.bivis.service.SpeciesGroupingService;
import ru.bivis.service.SpeciesGroupingServiceImpl;
import ru.bivis.service.SpeciesReportWriter;
import ru.bivis.service.SpeciesReportWriterFile;

import java.nio.file.Path;
import java.util.List;
import java.util.TreeMap;

public class App {
    private static final String DEFAULT_INPUT_PATH = "characters.csv";
    private static final String DEFAULT_OUTPUT_PATH = "species.txt";

    public static void main(String[] args) {
        Path inputPath = Path.of(args.length > 0 ? args[0] : DEFAULT_INPUT_PATH);
        Path outputPath = Path.of(args.length > 1 ? args[1] : DEFAULT_OUTPUT_PATH);

        CharacterDao characterDao = new CharacterDaoCsv(inputPath);
        SpeciesGroupingService groupingService = new SpeciesGroupingServiceImpl();
        SpeciesReportWriter reportWriter = new SpeciesReportWriterFile(outputPath);

        List<CharacterDto> characters = characterDao.findAll();
        TreeMap<String, List<String>> namesBySpecies = groupingService.groupNamesBySpecies(characters);
        reportWriter.write(namesBySpecies);
    }
}
