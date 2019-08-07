package it.makeit.ormy;

import it.makeit.ormy.annotation.Column;
import it.makeit.ormy.annotation.Entity;
import it.makeit.ormy.annotation.Relationship;
import it.makeit.ormy.annotation.PrimaryKey;
import it.makeit.ormy.model.EntityType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestRelationShip {

	@Test
	public void myFirstEntityTest() {
		String expectedDDl = "CREATE TABLE my_first_entity (column1 INT(19), column2 VARCHAR(45), mySecondEntity_column1 INT(19) NOT NULL, PRIMARY KEY (column1));";
		EntityType et = new EntityType(MyFirstEntity.class);
		Assertions.assertEquals(expectedDDl, et.generateDDL());
	}

	@Test
	public void mySecondEntityTest() {
		String expectedDDl = "CREATE TABLE my_second_entity (column1 INT(19), column2 VARCHAR(45), PRIMARY KEY (column1));";
		EntityType et = new EntityType(MySecondEntity.class);
		Assertions.assertEquals(expectedDDl, et.generateDDL());
	}


	@Test
	public void relationShipTest() {
		String expectedDDL = "ALTER TABLE my_first_entity ADD CONSTRAINT fk_mySecondEntity FOREIGN KEY (mySecondEntity_column1) REFERENCES my_second_entity (column1);";
		EntityType et1 = new EntityType(MyFirstEntity.class);
		List<String> fksAlterTable = et1.generateFKsDDL();
		Assertions.assertEquals(expectedDDL,fksAlterTable.get(0));
	}

	@Test
	public void noEntityTest(){
		Assertions.assertThrows(RuntimeException.class, () -> new EntityType(MyFifthEntity.class));
	}

	@Entity(name = "my_first_entity")
	class MyFirstEntity {

		@PrimaryKey
		private final Long column1;

		@Column
		private final String column2;

		@Relationship(mandatory = true)
		private final MySecondEntity mySecondEntity;

		public MyFirstEntity(Long column1, String column2, MySecondEntity mySecondEntity) {
			this.column1 = column1;
			this.column2 = column2;
			this.mySecondEntity = mySecondEntity;
		}

	}

	@Entity(name = "my_second_entity")
	class MySecondEntity {

		@PrimaryKey
		private final Long column1;

		@Column
		private final String column2;

		public MySecondEntity(Long column1, String column2) {
			this.column1 = column1;
			this.column2 = column2;
		}
	}

	@Entity
	class MyFouthEntity {

		@PrimaryKey
		private final Long column1;

		@Relationship
		private MyFifthEntity fifthEntity;

		public MyFouthEntity(Long column1) {
			this.column1 = column1;
		}

		public Long getColumn1() {
			return column1;
		}

	}

	// NO ENTITY ANNOTATION!!
	class MyFifthEntity {

		@PrimaryKey
		private final Long column1;

		public MyFifthEntity(Long column1) {
			this.column1 = column1;
		}

		public Long getColumn1() {
			return column1;
		}

	}


}
