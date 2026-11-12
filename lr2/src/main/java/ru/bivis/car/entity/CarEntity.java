package ru.bivis.car.entity;

import java.util.Objects;

public class CarEntity {

    private Integer id;

    private String model;

    private Integer year;

    public CarEntity(String model, Integer year) {
        this(null, model, year);
    }

    public CarEntity(Integer id, String model, Integer year) {
        this.id = id;
        this.model = model;
        this.year = year;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarEntity that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(model, that.model)
                && Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model, year);
    }

    @Override
    public String toString() {
        return "CarEntity{id=" + id + ", model='" + model + "', year=" + year + '}';
    }
}
