package ru.bivis.service;

import ru.bivis.dto.CharacterDto;

import java.util.List;
import java.util.TreeMap;

public interface SpeciesGroupingService {
    TreeMap<String, List<String>> groupNamesBySpecies(List<CharacterDto> characters);
}
