package com.revature.model;

import java.util.Objects;

public class ReimbType {
    private int id;
    private String type;

    public ReimbType() {

    }

    public ReimbType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReimbType reimbType = (ReimbType) o;
        return id == reimbType.id && Objects.equals(type, reimbType.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return "ReimbType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}