package it.makeit.ormy;

import it.makeit.ormy.annotation.Column;
import it.makeit.ormy.annotation.Entity;
import it.makeit.ormy.annotation.Relationship;
import it.makeit.ormy.annotation.PrimaryKey;
import it.makeit.ormy.model.EntityType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestRelationShip {

	@Test
	public void myFirstEntityTest() {
		String expectedDDl = "CREATE TABLE my_first_entity (column1 INT(19), column2 VARCHAR(45), PRIMARY KEY (column1));";
		EntityType et = new EntityType(MyFirstEntity.class);
		assertEquals(expectedDDl,et.generateDDL());
	}

	@Test
	public void mySecondEntityTest() {
		String expectedDDl = "CREATE TABLE my_second_entity (column1 INT(19), column2 VARCHAR(45), PRIMARY KEY (column1));";
		EntityType et = new EntityType(MySecondEntity.class);
		assertEquals(expectedDDl,et.generateDDL());
	}


	@Test
	public void relationShipTest(){
		EntityType et1 = new EntityType(MyFirstEntity.class);
		EntityType et2 = new EntityType(MySecondEntity.class);
		System.out.println(et1.generateDDL());
		System.out.println(et2.generateDDL());
		et1.generateFKsDDL().stream().forEach(System.out::println);
	}

	@Entity(name = "my_first_entity")
	class MyFirstEntity {

		@PrimaryKey
		private final Long column1;

		@Column
		private final String column2;

		@Relationship
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


}
