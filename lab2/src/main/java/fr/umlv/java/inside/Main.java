package fr.umlv.java.inside;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import static java.util.stream.Collectors.joining;

public class Main {
    /*
    public static String toJSON(Person person) {
        return
                "{\n" +
                        "  \"firstName\": \"" + person.getFirstName() + "\"\n" +
                        "  \"lastName\": \"" + person.getLastName() + "\"\n" +
                        "}\n";
    }

    public static String toJSON(Alien alien) {
        return
                "{\n" +
                        "  \"planet\": \"" + alien.getPlanet() + "\"\n" +
                        "  \"members\": \"" + alien.getAge() + "\"\n" +
                        "}\n";
    }
  */
    private static String propertyName(String name) {
        return Character.toLowerCase(name.charAt(3)) + name.substring(4);
    }



    private static Object getter(Object object, Method method) {
        try {
            return method.invoke(object);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e) ;
        } catch (InvocationTargetException e) {
            var cause = e.getCause() ;
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause ;
            }
            if (cause instanceof Error) {
                throw (Error) cause ;
            }
            throw new UndeclaredThrowableException(cause) ;
        }
    }

    public static String toJSON(Object object) {
        return Arrays.stream(object.getClass().getMethods())
                .filter(method -> method.getName().startsWith("get"))
                .map(method -> propertyName (method.getName()) + " : " + getter(object, method))
                .collect(joining(",","{","}"));
    }

    public static void main(String[] args) {
        var person = new Person("John", "Doe");
        System.out.println(toJSON(person));
        var alien = new Alien("E.T.", 100);
        System.out.println(toJSON(alien));
    }
}
