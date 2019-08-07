package it.makeit.ormy.model;

import java.util.ArrayList;
import java.util.List;

public class PrimaryKeyType {

    private final List<ColumnType> columnTypes;

    public PrimaryKeyType(Iterable<ColumnType> columnTypes) {
        this.columnTypes = new ArrayList<>();
        for (ColumnType c : columnTypes)
            this.columnTypes.add(c);
    }

    // TODO ritornare una copia per evitare la modifica di qualche burlone a runtime
    public List<ColumnType> getColumnTypes() {
        return columnTypes;
    }
}
