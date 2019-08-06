package it.makeit.ormy.model;

import java.util.ArrayList;
import java.util.List;

public class PrimaryKeyType {

    private final List<ColumnType> columnTypes = new ArrayList<>();

    public PrimaryKeyType(Iterable<ColumnType> columnTypes) {
        for (ColumnType c : columnTypes)
            this.columnTypes.add(c);
    }

    public List<ColumnType> getColumnTypes() {
        return columnTypes;
    }
}
