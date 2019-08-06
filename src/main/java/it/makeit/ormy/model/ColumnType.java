package it.makeit.ormy.model;

public class ColumnType {

    private final String columnName;
    private final String fieldName;
    private final String fieldClass;
    private final int length;
    private final boolean mandatory;
    private final boolean unique;

    public ColumnType(String columnName, String fieldName, Class fieldClass, int length, boolean mandatory, boolean unique) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.length = length;
        this.mandatory = mandatory;
        this.unique = unique;

        switch (fieldClass.getSimpleName()) {
            case "String":
                this.fieldClass = "VARCHAR(" + length +")";
                break;
            default:
                throw new RuntimeException("This kind of field is not yet implemented");
        }


    }

    public String getColumnName() {
        return columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isUnique() {
        return unique;
    }

    public String getFieldClass() {
        return fieldClass;
    }
}
