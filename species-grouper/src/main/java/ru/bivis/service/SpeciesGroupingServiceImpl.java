package ru.bivis.service;

import ru.bivis.dto.CharacterDto;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class SpeciesGroupingServiceImpl implements SpeciesGroupingService {
    @Override
    public TreeMap<String, List<String>> groupNamesBySpecies(List<CharacterDto> characters) {
        TreeMap<String, List<String>> namesBySpecies = new TreeMap<>();
        for (CharacterDto character : characters) {
            namesBySpecies.computeIfAbsent(character.getSpecies(), species -> new ArrayList<>())
                    .add(character.getName());
        }
        return namesBySpecies;
    }
}
