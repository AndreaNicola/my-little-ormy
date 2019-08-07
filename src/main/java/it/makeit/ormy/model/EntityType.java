package it.makeit.ormy.model;

import it.makeit.ormy.annotation.Column;
import it.makeit.ormy.annotation.Entity;
import it.makeit.ormy.annotation.Relationship;
import it.makeit.ormy.annotation.PrimaryKey;
import it.makeit.ormy.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class EntityType {

	private final String table;
	private final PrimaryKeyType primaryKey;
	private final List<RelationshipType> foreignKeys = new ArrayList<>();
	private final List<ColumnType> columns = new ArrayList<>();

	public EntityType(Class clazz) {

		if (!clazz.isAnnotationPresent(Entity.class)) {
			throw new RuntimeException(clazz.getName() + " is not a managed entity");
		}

		table = extractTableName(clazz);


		Collection<ColumnType> pkColumns = extractPkColumns(clazz);
        this.primaryKey = new PrimaryKeyType(pkColumns);
        columns.addAll(pkColumns);

        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {

			Column columnAnnotation = f.getAnnotation(Column.class);
			Relationship relationshipAnnotation = f.getAnnotation(Relationship.class);

			if (relationshipAnnotation != null) {

				boolean isReferencedObjectAnEntity = checkReferencedObjectForEntity(f.getType());

				if (isReferencedObjectAnEntity) {

					Collection<ColumnType> referencedPkColumns = extractPkColumns(f.getType());

					List<ColumnType> fkColumns = referencedPkColumns.stream().map(rpc -> new ColumnType(
					 f.getName() + "_" + rpc.getColumnName(),
					 f.getName() + "_" + rpc.getFieldName(),
					 rpc.getFieldClass(),
					 rpc.getLength(),
					 relationshipAnnotation.mandatory(),
					 false)).collect(Collectors.toList());

					columns.addAll(fkColumns);

					RelationshipType relationshipType = new RelationshipType(
					 "fk_" + f.getName(),
					 extractTableName(f.getType()),
					 fkColumns,
					 referencedPkColumns,
					 relationshipAnnotation.mandatory()
					);

					foreignKeys.add(relationshipType);

				}

			}

			if (columnAnnotation != null) {
				ColumnType column = prepareColumn(f, columnAnnotation);
				columns.add(column);
			}

		}

		if (pkColumns.isEmpty()) {
			throw new RuntimeException(clazz.getName() + " does not declare a primary key");
		}

		if (columns.isEmpty()) {
			throw new RuntimeException(clazz.getName() + " does not declare any column");
		}

	}

	private ColumnType prepareColumn(Field f, Column columnAnnotation) {
		return new ColumnType(
		 StringUtils.isBlank(columnAnnotation.name()) ? f.getName() : columnAnnotation.name(),
		 f.getName(),
		 f.getType(),
		 columnAnnotation.length(),
		 columnAnnotation.mandatory(),
		 columnAnnotation.unique()
		);
	}

	private String extractTableName(Class<?> type) {
		Entity entityAnnotation = type.getAnnotation(Entity.class);
		if (entityAnnotation == null) {
			throw new RuntimeException("The class is not a managed Entity");
		}

		return StringUtils.isBlank(entityAnnotation.name()) ? type.getName() : entityAnnotation.name();

	}

	private boolean checkReferencedObjectForEntity(Class<?> type) {
		return type.getAnnotation(Entity.class) != null;
	}

	private Collection<ColumnType> extractPkColumns(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		Collection<ColumnType> result = new ArrayList<>();
		for (Field f : fields) {
			PrimaryKey primaryKeyAnnotation = f.getAnnotation(PrimaryKey.class);
			if (primaryKeyAnnotation != null) {
				Column primaryKeyColumn = primaryKeyAnnotation.column();
				ColumnType column = prepareColumn(f, primaryKeyColumn);
				result.add(column);
			}
		}
		return result;
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
			           c.getColType() +
			           (c.isUnique() ? " UNIQUE" : "") +
			           (c.isMandatory() ? " NOT NULL" : "")
			);

			sb.append(cIterator.hasNext() ? ", " : "");

		}

		return sb.toString();
	}

	public List<String> generateFKsDDL() {
		return this.foreignKeys.stream().map(this::generateFKDDL).collect(Collectors.toList());
	}

	private String generateFKDDL(RelationshipType relationshipType) {
		StringBuilder sb = new StringBuilder();
		sb.append("ALTER TABLE " + this.table);
		sb.append(" ADD CONSTRAINT " + relationshipType.getFkName());

		sb.append(" FOREIGN KEY (");
		Iterator<ColumnType> fkColsColumnTypeIterator = relationshipType.getFkColumns().iterator();
		while (fkColsColumnTypeIterator.hasNext()) {
			ColumnType col = fkColsColumnTypeIterator.next();
			sb.append(col.getColumnName());
			sb.append(fkColsColumnTypeIterator.hasNext() ? ", " : "");
		}
		sb.append(")");

		sb.append(" REFERENCES " + relationshipType.getReferencedTable() + " (");
		Iterator<ColumnType> referencedPkColsIterator = relationshipType.getReferencedPkColumns().iterator();
		while (referencedPkColsIterator.hasNext()) {
			ColumnType col = referencedPkColsIterator.next();
			sb.append(col.getColumnName());
			sb.append(referencedPkColsIterator.hasNext() ? ", " : "");
		}
		sb.append(");");

		return sb.toString();
	}

}
