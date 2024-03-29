package fr.umlv.java.inside;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Comparator;

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
    private final static ClassValue<Method[]> cache = new ClassValue<Method[]>() {
        @Override
        protected Method[] computeValue(Class<?> type) {
            return type.getMethods() ;
        }
    };


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

    private static String annotationHasValue (Method e) {
        var value = e.getAnnotation(JSONProperty.class).value();
        return value.isEmpty() ? propertyName (e.getName()) : value ;
    }

    public static String toJSON(Object object) {
        return Arrays.stream(cache.get(object.getClass()))
                .filter(method -> method.getName().startsWith("get"))
                .filter(method -> method.isAnnotationPresent(JSONProperty.class))
                .sorted(Comparator.comparing(Method::getName))
                .map(method -> annotationHasValue (method) + " : " + getter(object, method))
                .collect(joining(",","{","}"));
    }

    public static void main(String[] args) {
        var person = new Person("John", "Doe");
        System.out.println(toJSON(person));
        var alien = new Alien("E.T.", 100);
        System.out.println(toJSON(alien));
    }
}
