package it.makeit.ormy;

import it.makeit.ormy.annotation.Column;
import it.makeit.ormy.annotation.Entity;
import it.makeit.ormy.annotation.PrimaryKey;
import it.makeit.ormy.model.EntityType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestEntity {

    @Entity(name = "my_first_entity")
    class MyFirstEntity {

        @PrimaryKey(column = @Column(name = "col_1"))
        private final String column1;

        @Column(name = "col_2", mandatory = true, length = 100)
        private final byte[] column2;

        @Column(unique = true)
        private final String column3;

        public MyFirstEntity(String column1, byte[] column2, String column3) {
            this.column1 = column1;
            this.column2 = column2;
            this.column3 = column3;
        }

        public String getColumn1() {
            return column1;
        }

        public byte[] getColumn2() {
            return column2;
        }

        public String getColumn3() {
            return column3;
        }

    }

    @Entity(name = "my_second_entity")
    class MySecondEntity {

        @PrimaryKey
        private final String column1;

        @PrimaryKey
        private final String column2;

        @PrimaryKey
        private final String column3;

        public MySecondEntity(String column1, String column2, String column3) {
            this.column1 = column1;
            this.column2 = column2;
            this.column3 = column3;
        }

        public String getColumn1() {
            return column1;
        }

        public String getColumn2() {
            return column2;
        }

        public String getColumn3() {
            return column3;
        }
    }

    @Test
    public void myFirstEntityTest() {
        String expectedDDl = "CREATE TABLE my_first_entity (col_1 VARCHAR(45), col_2 LONGBLOB NOT NULL, column3 VARCHAR(45) UNIQUE, PRIMARY KEY (col_1));";
        EntityType et = new EntityType(MyFirstEntity.class);
        assertEquals(expectedDDl, et.generateDDL());
    }

    @Test
    public void mySecondEntityTest() {
        String expectedDDl = "CREATE TABLE my_second_entity (column1 VARCHAR(45), column2 VARCHAR(45), column3 VARCHAR(45), PRIMARY KEY (column1, column2, column3));";
        EntityType et = new EntityType(MySecondEntity.class);
        assertEquals(expectedDDl, et.generateDDL());
    }


}
