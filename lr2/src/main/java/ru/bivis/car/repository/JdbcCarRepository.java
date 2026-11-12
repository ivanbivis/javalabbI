package ru.bivis.car.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bivis.car.entity.CarEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class JdbcCarRepository implements CarRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcCarRepository.class);

    private static final String INSERT = "INSERT INTO car (model, year) VALUES (?, ?)";
    private static final String SELECT_BY_ID = "SELECT id, model, year FROM car WHERE id = ?";
    private static final String SELECT_BY_MODEL = "SELECT id, model, year FROM car WHERE model = ?";
    private static final String SELECT_ALL = "SELECT id, model, year FROM car ORDER BY id";
    private static final String UPDATE = "UPDATE car SET model = ?, year = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM car WHERE id = ?";

    private final DataSource dataSource;

    public JdbcCarRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int save(CarEntity car) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, car.getModel());
            ps.setInt(2, car.getYear());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    String message = "Не удалось получить id созданной записи " + car;
                    log.error(message);
                    throw new RepositoryException(message);
                }
                int id = keys.getInt(1);
                car.setId(id);
                return id;
            }
        } catch (SQLException e) {
            throw error("Ошибка при сохранении " + car, e);
        }
    }

    @Override
    public Optional<CarEntity> findById(int id) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            return querySingle(ps);
        } catch (SQLException e) {
            throw error("Ошибка при поиске по id=" + id, e);
        }
    }

    @Override
    public Optional<CarEntity> findByField(String model) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_MODEL)) {
            ps.setString(1, model);
            return querySingle(ps);
        } catch (SQLException e) {
            throw error("Ошибка при поиске по model=" + model, e);
        }
    }

    @Override
    public List<CarEntity> findAll() {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            List<CarEntity> result = new ArrayList<>();
            while (rs.next()) {
                result.add(map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw error("Ошибка при получении всех записей", e);
        }
    }

    @Override
    public boolean update(CarEntity car) {
        if (car.getId() == null) {
            return false;
        }
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(UPDATE)) {
            ps.setString(1, car.getModel());
            ps.setInt(2, car.getYear());
            ps.setInt(3, car.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw error("Ошибка при обновлении " + car, e);
        }
    }

    @Override
    public void deleteById(int id) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(DELETE_BY_ID)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw error("Ошибка при удалении id=" + id, e);
        }
    }


    private RepositoryException error(String message, SQLException e) {
        log.error(message, e);
        return new RepositoryException(message, e);
    }

    private Optional<CarEntity> querySingle(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() ? Optional.of(map(rs)) : Optional.empty();
        }
    }

    private CarEntity map(ResultSet rs) throws SQLException {
        return new CarEntity(rs.getInt("id"), rs.getString("model"), rs.getInt("year"));
    }
}
