package fr.umlv.java.inside;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static fr.umlv.java.inside.Main.toJSON;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class toJsonTest {

    @Test
    public void person() {
      var person = new Person("John", "Doe");
      assertEquals("{class : class fr.umlv.java.inside.toJsonTest$Person,firstName : John,lastName : Doe}", toJSON(person));
    }
    @Test
    public void alien() {
        var alien = new Alien("E.T.", 100);
        assertEquals("{age : 100,class : class fr.umlv.java.inside.toJsonTest$Alien,planet : E.T.}", toJSON(alien));
    }

    public static class Person {
        private final String firstName;
        private final String lastName;

        public Person(String firstName, String lastName) {
            this.firstName = Objects.requireNonNull(firstName);
            this.lastName = Objects.requireNonNull(lastName);
        }

        public String getFirstName() {
            return firstName;
        }
        public String getLastName() {
            return lastName;
        }
    }

    public static class Alien {
        private final String planet;
        private final int age;

        public Alien(String planet, int age) {
            if (age <= 0) {
                throw new IllegalArgumentException("Too young...");
            }
            this.planet = Objects.requireNonNull(planet);
            this.age = age;
        }

        public String getPlanet() {
            return planet;
        }

        public int getAge() {
            return age;
        }
    }

}


