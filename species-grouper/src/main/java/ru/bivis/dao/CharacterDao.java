package ru.bivis.dao;

import ru.bivis.dto.CharacterDto;

import java.util.List;

public interface CharacterDao {
    List<CharacterDto> findAll();
}
