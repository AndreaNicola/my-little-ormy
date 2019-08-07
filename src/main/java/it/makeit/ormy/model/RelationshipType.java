package it.makeit.ormy.model;

import java.util.Collection;
import java.util.List;

public class RelationshipType {

	private final String fkName;
	private final String referencedTable;
	private final  List<ColumnType> fkColumns;
	private final boolean mandatory;
	private final Collection<ColumnType> referencedPkColumns;

	public RelationshipType(String fkName, String referencedTable, List<ColumnType> fkColumns, Collection<ColumnType> referencedPkColumns, boolean mandatory) {
		this.fkName = fkName;
		this.referencedTable = referencedTable;
		this.fkColumns = fkColumns;
		this.referencedPkColumns = referencedPkColumns;
		this.mandatory = mandatory;
	}

	public String getFkName() {
		return fkName;
	}

	public String getReferencedTable() {
		return referencedTable;
	}

	public List<ColumnType> getFkColumns() {
		return fkColumns;
	}

	public Collection<ColumnType> getReferencedPkColumns() {
		return referencedPkColumns;
	}

	public boolean isMandatory() {
		return mandatory;
	}
}
