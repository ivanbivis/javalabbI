package ru.bivis.car.repository;

import ru.bivis.car.entity.CarEntity;

import java.util.List;
import java.util.Optional;

public interface CarRepository {

    int save(CarEntity car);


    Optional<CarEntity> findById(int id);


    Optional<CarEntity> findByField(String model);

    List<CarEntity> findAll();


    boolean update(CarEntity car);

    void deleteById(int id);
}
