package it.makeit.ormy.model;

import it.makeit.ormy.annotation.Column;
import it.makeit.ormy.annotation.Entity;
import it.makeit.ormy.annotation.PrimaryKey;
import it.makeit.ormy.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityType {

    private final String table;
    private final PrimaryKeyType primaryKey;
    private final List<ColumnType> columns = new ArrayList<>();

    public EntityType(Class clazz) {

        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException(clazz.getName() + " is not a managed entity");
        }


        Entity entityAnnotation = (Entity) clazz.getAnnotation(Entity.class);
        table = StringUtils.isBlank(entityAnnotation.name()) ? clazz.getName() : entityAnnotation.name();

        Field[] fields = clazz.getDeclaredFields();
        List<ColumnType> pkColumns = new ArrayList<>();

        for (Field f : fields) {

            PrimaryKey pkAnnotation = f.getAnnotation(PrimaryKey.class);
            Column columnAnnotation = f.getAnnotation(Column.class);

            if (columnAnnotation != null) {

                ColumnType column = new ColumnType(
                        StringUtils.isBlank(columnAnnotation.name()) ? f.getName() : columnAnnotation.name(),
                        f.getName(),
                        f.getType(),
                        columnAnnotation.length(),
                        columnAnnotation.mandatory(),
                        columnAnnotation.unique()
                );

                if (pkAnnotation != null) {
                    pkColumns.add(column);
                }

                columns.add(column);

            }

        }

        if (pkColumns.isEmpty()) {
            throw new RuntimeException(clazz.getName() + " does not declare a primary key");
        }

        if (columns.isEmpty()) {
            throw new RuntimeException(clazz.getName() + " does not declare any column");
        }

        this.primaryKey = new PrimaryKeyType(pkColumns);

    }

    public String generateDDL() {
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE " + this.table + " (");
        sb.append(generateColumnsDDL());
        sb.append(generatePrimaryKeyDDL());
        sb.append(");");

        return sb.toString();
    }

    private String generatePrimaryKeyDDL() {
        StringBuilder sb = new StringBuilder(", PRIMARY KEY (");
        Iterator<ColumnType> pkIterator = primaryKey.getColumnTypes().iterator();

        while (pkIterator.hasNext()) {
            ColumnType pkCol = pkIterator.next();
            sb.append(pkCol.getColumnName());
            sb.append(pkIterator.hasNext() ? ", " : "");
        }

        sb.append(")");
        return sb.toString();
    }

    private String generateColumnsDDL() {
        StringBuilder sb = new StringBuilder();

        Iterator<ColumnType> cIterator = this.columns.iterator();
        while (cIterator.hasNext()) {

            ColumnType c = cIterator.next();
            sb.append(c.getColumnName() + " " +
                    c.getFieldClass() +
                    (c.isUnique() ? " UNIQUE" : "") +
                    (c.isMandatory() ? " NOT NULL" : "")
            );

            sb.append(cIterator.hasNext() ? ", " : "");

        }

        return sb.toString();
    }


}
