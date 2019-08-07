package it.makeit.ormy;

import it.makeit.ormy.annotation.Column;
import it.makeit.ormy.annotation.Entity;
import it.makeit.ormy.annotation.PrimaryKey;
import it.makeit.ormy.model.EntityType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestColumnTypes {

	@Test
	public void testColumnTypes() {
		String expectedDDL = "CREATE TABLE ColumnEntity (id INT(11), boolCol INT(1) NOT NULL, doubleCol DOUBLE, floatCol FLOAT, uuidCol VARCHAR(45), bigDecimalCol DECIMAL(10,2), localDateCol DATE, localDateTimeCol DATETIME, PRIMARY KEY (id));";
		EntityType et = new EntityType(ColumnEntity.class);
		Assertions.assertEquals(expectedDDL, et.generateDDL());
	}

	@Test
	public void testNoColumnTypeEntity() {
		Assertions.assertThrows(RuntimeException.class, () -> new EntityType(NoColumnEntity.class));
	}

	@Entity
	class NoColumnEntity {

		@PrimaryKey
		private final Object object;

		public NoColumnEntity(Object object) {
			this.object = object;
		}

		public Object getObject() {
			return object;
		}
	}

	@Entity
	class ColumnEntity {

		@PrimaryKey
		private Integer id;

		@Column(mandatory = true)
		private Boolean boolCol;

		@Column
		private Double doubleCol;

		@Column
		private Float floatCol;

		@Column
		private UUID uuidCol;

		@Column
		private BigDecimal bigDecimalCol;

		@Column
		private LocalDate localDateCol;

		@Column
		private LocalDateTime localDateTimeCol;

	}

}
