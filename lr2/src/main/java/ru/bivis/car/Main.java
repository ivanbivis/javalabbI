package ru.bivis.car;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bivis.car.db.Database;
import ru.bivis.car.db.DatabaseProperties;
import ru.bivis.car.entity.CarEntity;
import ru.bivis.car.repository.CarRepository;
import ru.bivis.car.repository.JdbcCarRepository;

import java.util.List;
import java.util.Optional;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        DatabaseProperties properties = new DatabaseProperties("application.properties");

        try (Database database = new Database(properties)) {
            database.migrate();
            CarRepository repository = new JdbcCarRepository(database.getDataSource());

            // Чистка прошлых запусков
            List<CarEntity> oldCars = repository.findAll();
            oldCars.forEach(car -> repository.deleteById(car.getId()));
            log.info("Удалено записей от прошлых запусков: {}", oldCars.size());

            log.info("=== save ===");
            int camryId = repository.save(new CarEntity("Toyota Camry", 2018));
            int golfId = repository.save(new CarEntity("Volkswagen Golf", 2020));
            int vestaId = repository.save(new CarEntity("Lada Vesta", 2022));
            log.info("Созданы id: {}, {}, {}", camryId, golfId, vestaId);

            log.info("=== findAll ===");
            repository.findAll().forEach(car -> log.info("{}", car));

            log.info("=== findById ===");
            log.info("findById({}) = {}", golfId, show(repository.findById(golfId)));
            log.info("findById(-1) = {}", show(repository.findById(-1)));

            log.info("=== findByField (model) ===");
            log.info("findByField(\"Lada Vesta\") = {}", show(repository.findByField("Lada Vesta")));
            log.info("findByField(\"Tesla\") = {}", show(repository.findByField("Tesla")));

            log.info("=== update ===");
            CarEntity camry = repository.findById(camryId)
                    .orElseThrow(() -> new IllegalStateException("Только что созданная машина не найдена, id=" + camryId));
            camry.setModel("Toyota Camry XV70");
            camry.setYear(2019);
            log.info("update(camry) = {}", repository.update(camry));
            log.info("update(camry) повторно = {}", repository.update(camry));
            log.info("После update: {}", show(repository.findById(camryId)));
            log.info("update(несуществующий) = {}", repository.update(new CarEntity(-1, "Ghost", 1900)));

            log.info("=== deleteById ===");
            repository.deleteById(golfId);
            repository.deleteById(golfId);
            log.info("findById({}) после удаления = {}", golfId, show(repository.findById(golfId)));

            log.info("=== findAll (итог) ===");
            repository.findAll().forEach(car -> log.info("{}", car));
        }
    }

    private static String show(Optional<CarEntity> car) {
        return car.map(CarEntity::toString).orElse("не найдено");
    }
}
