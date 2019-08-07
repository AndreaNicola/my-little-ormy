package it.makeit.ormy.model;

public class ColumnType {

	private final String columnName;
	private final String fieldName;
	private final String fieldClass;
	private final String colType;
	private final int length;
	private final boolean mandatory;
	private final boolean unique;

	public ColumnType(String columnName, String fieldName, Class fieldClass, int length, boolean mandatory, boolean unique) {
		this(columnName, fieldName, fieldClass.getSimpleName(), length, mandatory, unique);
	}

	public ColumnType(String columnName, String fieldName, String fieldClass, int length, boolean mandatory, boolean unique) {
		this.fieldClass = fieldClass;
		this.columnName = columnName;
		this.fieldName = fieldName;
		this.length = length;
		this.mandatory = mandatory;
		this.unique = unique;

		switch (fieldClass) {
			case "byte[]":
				this.colType = "LONGBLOB";
				break;
			case "Integer":
				this.colType = "INT(11)";
				break;
			case "Long":
				this.colType = "INT(19)";
				break;
			case "Boolean":
				this.colType = "INT(1)";
				break;
			case "Double":
				this.colType = "DOUBLE";
				break;
			case "Float":
				this.colType = "FLOAT";
				break;
			case "UUID":
				this.colType = "VARCHAR(45)";
				break;
			case "BigDecimal":
				this.colType = "DECIMAL(10,2)";
				break;
			case "String":
				this.colType = "VARCHAR(" + length + ")";
				break;
			case "LocalDate":
				this.colType = "DATE";
				break;
			case "LocalDateTime":
				this.colType = "DATETIME";
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

	public int getLength() {
		return length;
	}

	public String getColType() {
		return colType;
	}
}
